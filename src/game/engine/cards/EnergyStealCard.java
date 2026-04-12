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

    @Override
    public void modifyCanisterEnergy(Monster monster, int canisterValue){
        monster.alterEnergy(canisterValue);
    }

     @Override
    public void performAction(Monster player, Monster opponent) {
        //if opponent is shielded, shield is removed and no energy is stolen
        if(opponent.isShielded()){
            opponent.setShielded(false);
            return;
        }
        //if opponent is not shielded, player steals energy from opponent
        int stolen = Math.min(opponent.getEnergy(), energy);
        this.modifyCanisterEnergy(opponent,-stolen);
        this.modifyCanisterEnergy(player, stolen);
   
}
}