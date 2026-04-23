package game.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import game.engine.dataloader.DataLoader;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.Monster;


public class Game {
//read only
     private Board board;
     private ArrayList<Monster> allMonsters;
     private Monster player;
     private Monster opponent;


//read and write
    private Monster current;


//constructor 
public Game(Role playerRole) throws IOException{
    
    //create board with loaded cards from csv
    this.board = new Board(DataLoader.readCards());

    //set all monsters with loaded monsters from csv
    allMonsters = DataLoader.readMonsters();

    //random selection of player 
     player = selectRandomMonsterByRole(playerRole);

    //random selection of opponent
    this.opponent = selectRandomMonsterByRole(playerRole == Role.SCARER ? Role.LAUGHER : Role.SCARER);
    this.current = player;    

    ArrayList<Monster> stationedMonsters = new ArrayList<>(allMonsters);
    stationedMonsters.remove(player);
    stationedMonsters.remove(opponent);
    Board.setStationedMonsters(stationedMonsters);

    board.initializeBoard(DataLoader.readCells());
}

private Monster getCurrentOpponent(){
    //A method that returns the monster that is not the current active monster
    if(current == player){
        return opponent;
    } else {
        return player;
    }
}


private int rollDice(){
// A method that returns a uniformly random integer between 1 and 6 inclusive
    return (int)(Math.random() * 6) + 1;
}

 void usePowerup() throws OutOfEnergyException {
// A method that activates the current monster’s powerup if the current monster has sufficient energy

    int currentEnergy = current.getEnergy();

    if (currentEnergy < Constants.POWERUP_COST) {
        throw new OutOfEnergyException("Not enough energy to use powerup.");
    }
    else{
    Monster opponent = getCurrentOpponent();

    current.setEnergy(currentEnergy - Constants.POWERUP_COST);
    current.executePowerupEffect(opponent); 
    }
 }



     public Board getBoard() {
         return board;
     }

     public ArrayList<Monster> getAllMonsters() {
         return allMonsters;
     }

     public Monster getPlayer() {
         return player;
     }

     public Monster getOpponent() {
         return opponent;
     }

     public Monster getCurrent() {
         return current;
     }

     public void setCurrent(Monster current) {
         this.current = current;
     }

     
   private Monster selectRandomMonsterByRole(Role role) {
		Collections.shuffle(allMonsters);
	    return allMonsters.stream()
	    		.filter(m -> m.getRole() == role)
	    		.findFirst()
	    		.orElse(null);
	}

    /**
     * A method that performs one full turn for the current monster.
     * If the monster is frozen, its turn is skipped and it is unfrozen.
     * Otherwise, the dice is rolled and the monster is moved on the board.
     * The turn is then switched to the other monster.
     */
    public void playTurn() throws InvalidMoveException {
        // If the monster is frozen, its turn is skipped and it is unfrozen.
        if (current.isFrozen()) {
            current.setFrozen(false);
            switchTurn();
        } else {
            // Otherwise, the dice is rolled and the monster is moved on the board.
            int roll = rollDice();
            try {
                board.moveMonster(current, roll, getCurrentOpponent());
            } catch (InvalidMoveException e) {
                // Case if player lands on opponent: the move is invalid, but turn still switches.
                throw e;
            } finally {
                // The turn is then switched to the other monster.
                switchTurn();
            }
        }
    }

    /**
     * A method that transfers turn to the other monster.
     */
    private void switchTurn() {
        if (current == player) {
            current = opponent;
        } else {
            current = player;
        }
    }

    /**
     * A method that evaluates whether the given monster satisfies all conditions required to win the game.
     */
    private boolean checkWinCondition(Monster monster) {
        // A monster wins if it reaches the winning position and has energy greater than or equal to the winning energy.
        return monster.getPosition() == Constants.WINNING_POSITION && monster.getEnergy() >= Constants.WINNING_ENERGY;
    }

    /**
     * A method that determines if either monster has met the win condition.
     * Returns the winning monster if one exists, otherwise returns null.
     */
    public Monster getWinner() {
        if (checkWinCondition(player)) {
            return player;
        } else if (checkWinCondition(opponent)) {
            return opponent;
        }
        return null;
    }

}
