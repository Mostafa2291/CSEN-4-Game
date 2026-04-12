package game.engine.monsters;
import game.engine.Constants;
import game.engine.Role;

public class Schemer extends Monster {

    public Schemer(String name, String description, Role role, int energy) {
        super(name, description, role, energy);
    }

    
    private int stealEnergyFrom(Monster target){ //disregards target shield
        int stolen = Math.min(target.getEnergy(), Constants.SCHEMER_STEAL); //steal whatevers lower the target or schemer steal
        
        target.setEnergy(target.getEnergy() - stolen);
        return stolen;

    }
}
