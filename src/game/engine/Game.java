package game.engine;

import java.io.IOException;
import java.util.ArrayList;

import game.engine.monsters.Monster;

public class Game {
//read only
     private final Board board;
     private final ArrayList<Monster> allMonsters;
     private final Monster player;
     private final Monster opponent;


//read and write
    private Monster current;


//work in progress waiting 4 dataloader to be done
     //Game(Role playerRole) throws IOException{

    // }

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

     
    // private Monster selectRandomMonsterByRole(Role role){

     //}


}
