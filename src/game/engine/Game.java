package game.engine;

import java.io.IOException;
import java.util.ArrayList;

import game.engine.dataloader.DataLoader;
import game.engine.monsters.Monster;

public class Game {
//read only
     private final Board board;
     private final ArrayList<Monster> allMonsters;
     private final Monster player;
     private final Monster opponent;


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
    if(playerRole == Role.LAUGHER){ //assign opponent random scarer
        opponent = selectRandomMonsterByRole(Role.SCARER);
    }
    else //assign opponent random laugher 
        opponent = selectRandomMonsterByRole(Role.LAUGHER);

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

     
    private Monster selectRandomMonsterByRole(Role role){
        int size = allMonsters.size();
        ArrayList <Monster> matchingMonster = new ArrayList<>();
        for (int i = 0; i < size; i++) { //add all monsters that match given role to matchingMonster arraylist
            if(allMonsters.get(i).getRole() == role ){
                matchingMonster.add(allMonsters.get(i));
            }
        }

        int randomIndex = (int) (Math.random()* matchingMonster.size());

        return matchingMonster.get(randomIndex);

    }


}
