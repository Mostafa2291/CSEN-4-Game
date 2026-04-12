package game.engine.cards;

import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class EnergyStealCard extends Card implements CanisterModifier {
    private int energy;//read only

    public EnergyStealCard(String name, String description, int rarity, int energy) {
        super(name, description, rarity, true);
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }
   //method that executes the card’s unique effect on the player and/or opponent.
     @Override
    public void performAction(Monster player, Monster opponent) {
        //if opponent is shielded, shield is removed and no energy is stolen
        if(opponent.isShielded()==true){
            opponent.setShielded(false);
        }
        //if opponent is not shielded, player steals energy from opponent
        else{
            if(opponent.getEnergy() < this.energy){
                player.alterEnergy(this.energy);
                opponent.setEnergy(0);
            }
            //if opponent has enough energy to steal
            else{
                player.alterEnergy(this.energy);
                opponent.alterEnergy(-energy);
            }
    }
}
}