package game.engine.dataloader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import game.engine.Role;
import game.engine.cards.Card;
import game.engine.cards.ConfusionCard;
import game.engine.cards.EnergyStealCard;
import game.engine.cards.ShieldCard;
import game.engine.cards.StartOverCard;
import game.engine.cards.SwapperCard;
import game.engine.cells.Cell;
import game.engine.cells.ContaminationSock;
import game.engine.cells.ConveyorBelt;
import game.engine.cells.DoorCell;
import game.engine.monsters.Dasher;
import game.engine.monsters.Dynamo;
import game.engine.monsters.Monster;
import game.engine.monsters.MultiTasker;
import game.engine.monsters.Schemer;
public class DataLoader {


    private static final String CARDS_FILE_NAME = "cards.csv";
    private static final String  CELLS_FILE_NAME = "cells.csv";
    private static final String  MONSTERS_FILE_NAME = "monsters.csv"; 
    

    
    /*Brief explanation regarding how readCards handles cards.csv

    The line represents the cards’ data as follows: (cardType, name, description, rarity) for
    SwapperCard or ShieldCard OR (cardType, name, description, rarity, energy) for EnergyStealCard OR (cardType, name, description, rarity, lucky) for StartOverCard OR (cardType,
    name, description, rarity, duration) for ConfusionCard.

    */ 

    public static ArrayList<Card> readCards() throws IOException{ 

        ArrayList<Card> cards = new ArrayList<>();

        FileReader fileReader = new FileReader(CARDS_FILE_NAME);
        BufferedReader br = new BufferedReader(fileReader);
        
        String line;
        while ((line =br.readLine())!=null) {
            String [] data = line.split(",");

            //all data shares these 4 attributes
            String type = data[0];
            String name = data[1];
            String desc = data[2];
            int rarity = Integer.parseInt(data[3]);

            if(type.equals("SWAPPER")){
            cards.add(new SwapperCard(name, desc, rarity));

            }
            else if(type.equals("STARTOVER")){
                boolean lucky = Boolean.parseBoolean(data[4]);
                cards.add(new StartOverCard(name, desc, rarity, lucky));
            } 
            else if (type.equals("ENERGYSTEAL")){
                int energy = Integer.parseInt(data[4]);
                cards.add(new EnergyStealCard(name, desc, rarity, energy));
            }
            else if(type.equals("SHIELD")){
                
                cards.add(new ShieldCard(name, desc, rarity));
            }
            else if(type.equals("CONFUSION")){

                int duration = Integer.parseInt(data[4]);
                cards.add(new ConfusionCard(name, desc, rarity, duration));
            }
        }
        br.close();
        return cards;
    }




    /*
    Small background for readCells method and how it handles cells.csv file:

    The line represents the cells’ data as follows: (name, role, energy) (for DoorCell)
     OR (name,effect) (for TransportCells such as ConveyorBelt or ContaminationSock). 
     The effect can be positive (Conveyor Belt) or negative (Contamination Sock)
    */

    

    public static ArrayList<Cell> readCells() throws IOException{   

        ArrayList<Cell> cells = new ArrayList<>();

        FileReader fileReader = new FileReader(CELLS_FILE_NAME);   
        BufferedReader br = new BufferedReader(fileReader);
        String line;

        while((line = br.readLine())!= null){
            String[] data = line.split(",");
            String name = data[0];


            if(data.length == 3){ //it belongs to DoorCell subclass 
                String role = data[1];
                int energy = Integer.parseInt(data[2]);
                Role monsterRole = Role.valueOf(role);

                cells.add(new DoorCell(name,monsterRole,energy));

            }
            else if(data.length ==2){ //it belongs to ConveyorBelt(+ve effect) OR ContaminationSock (-ve effect)

                //check if effect +ve or -ve
                int effect = Integer.parseInt(data[1]);
                if(effect>0){ //if +ve add as conveyerbelt
                    cells.add(new ConveyorBelt(name, effect));
                }
                //it is -ve so add as Contamination sock
                else{

                    cells.add(new ContaminationSock(name,effect ));

                }
            }
        }
        br.close();
        return cells;
    }

    public static ArrayList<Monster> readMonsters() throws IOException{

        ArrayList<Monster> monsters = new ArrayList<>();

        FileReader fileReader = new FileReader(MONSTERS_FILE_NAME);   
        BufferedReader br = new BufferedReader(fileReader);
        String line;

        while ((line = br.readLine()) != null) {
            String [] data = line.split(",");

            String type = data[0];
            String name = data[1];
            String desc = data[2];
            String role = data[3];
            int energy = Integer.parseInt(data[4]);
            Role monsterRole = Role.valueOf(role);

            if(type.equals("DYNAMO")){
                monsters.add(new Dynamo(name, desc, monsterRole, energy));
            }
            else if(type.equals("DASHER")){
                monsters.add(new Dasher(name, desc, monsterRole, energy));
            }
            else if(type.equals("SCHEMER")){
                monsters.add(new Schemer(name, desc, monsterRole, energy));
            }
            else if(type.equals("MULTITASKER")){
                monsters.add(new MultiTasker(name, desc, monsterRole, energy));
            }
        }

        br.close();
        return monsters;

    }


   
    
    


}
