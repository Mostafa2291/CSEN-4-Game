package game.engine.monsters;

import game.engine.Role;

public class Dasher extends Monster {

    private int momentumTurns; //number of turns dasher will have ,momentum' it is R/W 


    public Dasher(String name, String description, Role role, int energy) {
        super(name, description, role, energy);
        
        this.momentumTurns = 0;
    }

    @Override
    public void executePowerupEffect(Monster opponentMonster){ //sets momentum turns to 3
        setMomentumTurns(3);
    }  
    @Override
    public void move(int distance){
        if(getMomentumTurns() >0){ //multiply distance by 3
            super.move(distance * 3);
            setMomentumTurns(getMomentumTurns()-1);
        }
        else{ //momentum turns = 0
            super.move(distance * 2);
            }
    }

    public int getMomentumTurns() {
        return momentumTurns;
    }


    public void setMomentumTurns(int momentumTurns) {
        this.momentumTurns = momentumTurns;
      
    }

    

}
