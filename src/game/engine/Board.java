package game.engine;

import java.util.ArrayList;

import game.engine.cards.Card;
import game.engine.cells.*;
import game.engine.monsters.Monster;


public class Board {
    //Read only
   private Cell[][] boardCells;
   private static ArrayList<Card> originalCards; //removed final on purpose, as it cannot be initialised twice. (once in attribute and once in constructor)

   //Read & Write
   private static ArrayList<Monster> stationedMonsters;
   public static ArrayList<Card> cards;

   
   public Board(ArrayList<Card> readCards){
    this.boardCells = new Cell [Constants.BOARD_ROWS][Constants.BOARD_COLS];

    stationedMonsters = new ArrayList<Monster>();
    cards = new ArrayList<Card>();
    originalCards = readCards;

   }


   public  Cell[][] getBoardCells() {
    return boardCells;
   }
   public static ArrayList<Card> getOriginalCards() {
    return originalCards;
   }
   public static ArrayList<Monster> getStationedMonsters() {
    return stationedMonsters;
   }
   public static void setStationedMonsters(ArrayList<Monster> stationedMonsters) {
    Board.stationedMonsters = stationedMonsters;
   }
   public static ArrayList<Card> getCards() {
    return cards;
   }
   public static void setCards(ArrayList<Card> cards) {
    Board.cards = cards;
   }
   

   



}
