package game.engine.monsters;

import game.engine.Role;

public class MultiTasker extends Monster {

    private int normalSpeedTurns; //no. of turns MT moves at normals speed,,, R/W

    public MultiTasker(String name, String description, Role role, int energy) {
        super(name, description, role, energy);
        this.normalSpeedTurns = 0;
    }

    @Override
    public void executePowerupEffect(Monster opponentMonster){
        setNormalSpeedTurns(2);
    }

    @Override
    public void move(int distance){
        if(normalSpeedTurns > 0){
            super.move(distance);
            normalSpeedTurns--;

        }
        else{ //NST = 0
            super.move(distance/2);
        }
    }

    @Override
    public void setEnergy(int energy){
        int difference = getEnergy() - energy;
        if(difference != 0){ //if there is change
            super.setEnergy(energy + 200);
        }
        else{ //no change 
            super.setEnergy(energy);
        }


    }
    public int getNormalSpeedTurns() {
        return normalSpeedTurns;
    }

    public void setNormalSpeedTurns(int normalSpeedTurns) { 
        this.normalSpeedTurns = normalSpeedTurns;
        
    }

    
    
    



}
