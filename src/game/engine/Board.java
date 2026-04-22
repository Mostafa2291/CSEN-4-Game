package game.engine;


import java.util.ArrayList;
import java.util.Collections;


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
    setCardsByRarity();
    reloadCards();
    
   }

   private void setCardsByRarity(){
    ArrayList <Card> fullDeck = new ArrayList<>();
    for(int i = 0;i<originalCards.size();i++){
        Card currentCard = originalCards.get(i);

        int copies = currentCard.getRarity(); //how many copies of each card

        for(int j = 0; j<copies;j++){
            fullDeck.add(currentCard);
        }

    }
    originalCards = fullDeck;
    
   }

   public static void reloadCards(){
   cards.addAll(originalCards);
    Collections.shuffle(cards);
   }

   public static Card drawCard(){
        if(cards.isEmpty()){
            reloadCards();
        }
        return cards.removeFirst();
   }

   private int [] indexToRowCol(int index){
    int size = 10;
    int row = index/size;
    int col;
    if(row%2 ==0){
        col = index %size;
    }
    else
        col = size -1-(index%size);
    return new int[]{row,col};
    
   }
   private Cell getCell(int index) {
    int[]pos = indexToRowCol(index);
    int row = pos[0];
    int col = pos[1];
   
    return boardCells[row][col];
}

    private void setCell(int index, Cell cell) {
    int[] pos = indexToRowCol(index);
    int row = pos[0];
    int col = pos[1];
    boardCells[row][col] = cell;
}


void initializeBoard(ArrayList<Cell> specialCells){
    int doorplace = 1;
    int sockcount = 0;
    int beltcount = 0;
    int countmonster = 0;
    for(int i = 0; i<specialCells.size();i++){
        Cell temp = specialCells.get(i);
       
        if(temp instanceof DoorCell){
            setCell(doorplace, temp);
            doorplace+= 2;
        }
        else if(temp instanceof ContaminationSock){
            setCell(Constants.SOCK_CELL_INDICES[sockcount], temp);
            sockcount++;
        }
        else if (temp instanceof ConveyorBelt){
            setCell(Constants.CONVEYOR_CELL_INDICES[beltcount], temp);
            beltcount++;
        }

    }

    for(int i = 0; i<Constants.BOARD_SIZE;i++){
        if(belongs(i, Constants.MONSTER_CELL_INDICES)){
            Monster tempMonster = stationedMonsters.get(countmonster);
            setCell(i,new MonsterCell(tempMonster.getName(),tempMonster));
            countmonster++;

        }
        else if(belongs(i,Constants.CARD_CELL_INDICES)){
            
            setCell(i,new CardCell(null));
        }
        else if(getCell(i) ==  null) { //doesnt belong anywhere so normal cell
             setCell(i, new Cell(null));
        }
    }
}

private boolean belongs(int n , int [] arr){
    for(int i= 0;i<arr.length;i++){
        if(n == arr[i]){
            return true;
        }
    }
    return false;
}


private void updateMonsterPositions(Monster player, Monster opponent){

    for(int i = 0; i<Constants.BOARD_SIZE;i++){
        Cell tempCell = getCell(i);
        if(tempCell.isOccupied()){
            tempCell.setMonster(null);
        }
        
    }
    int playerPos = player.getPosition();
    int oppPos = opponent.getPosition();

    Cell playerCell = getCell(playerPos);
    Cell oppCell = getCell(oppPos);

    playerCell.setMonster(player);
    oppCell.setMonster(opponent);

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



        

    
    

   




