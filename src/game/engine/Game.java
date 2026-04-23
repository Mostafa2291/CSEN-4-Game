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


}
