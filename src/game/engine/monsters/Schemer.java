package game.engine.monsters;
import java.util.ArrayList;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Role;

public class Schemer extends Monster {

    public Schemer(String name, String description, Role role, int energy) {
        super(name, description, role, energy);
    }




    public void executePowerupEffect(Monster opponentMonster){
        int stolen = stealEnergyFrom(opponentMonster);

        ArrayList <Monster> stationed  = Board.getStationedMonsters();

        for (int i = 0; i < stationed.size() ; i++) {
            Monster current = stationed.get(i);
            stolen = stolen + stealEnergyFrom(current);
        }
        setEnergy(getEnergy() + stolen);
    }


    private int stealEnergyFrom(Monster target){ //disregards target shield
        int stolen = Math.min(target.getEnergy(), Constants.SCHEMER_STEAL); //steal whatevers lower the target or schemer steal
        
        target.setEnergy(target.getEnergy() - stolen);
        return stolen;

    }

    @Override
    public void setEnergy(int energy){ //add 10 energy to all energy CHANGES

       super.setEnergy(energy + 10);
        }
            
    }

