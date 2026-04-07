package game.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import game.engine.dataloader.DataLoader;
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
