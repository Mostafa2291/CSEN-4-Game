package game.engine.monsters;

import game.engine.Role;

public class Dynamo extends Monster {

    public Dynamo(String name, String description, Role role, int energy) {
        super(name, description, role, energy);
    }

    @Override
    public void executePowerupEffect(Monster opponentMonster ){ //freeze opponent for 1 turn
        opponentMonster.setFrozen(true);
    }

    @Override
    public void setEnergy(int energy){ //doubles all energy change
        int currentEnergy = getEnergy();
        int difference = energy - currentEnergy;
        if(difference != 0){ //there is change so we double the change
            super.setEnergy(currentEnergy + 2 * difference);

        }
        else{ //there is no energy change
            super.setEnergy(energy);
        }
    }   
}