package game.engine.monsters;

import game.engine.Role;

public class Dasher extends Monster {

    private int momentumTurns; //number of turns dasher will have ,momentum' it is R/W 


    public Dasher(String name, String description, Role originalRole, int energy) {
        super(name, description, originalRole, energy);
        
        this.momentumTurns = 0;
    }


    public int getMomentumTurns() {
        return momentumTurns;
    }


    public void setMomentumTurns(int momentumTurns) {
        if(momentumTurns>=0)
            this.momentumTurns = momentumTurns;
        else  
            this.momentumTurns = 0;
    }

    

}
