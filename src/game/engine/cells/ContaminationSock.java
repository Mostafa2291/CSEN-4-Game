package game.engine.cells;

import game.engine.Constants;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class ContaminationSock extends TransportCell implements CanisterModifier {

    public ContaminationSock(String name, int effect) {
        super(name, effect); 
    }
    @Override
    public void modifyCanisterEnergy(Monster monster, int canisterValue){
        monster.alterEnergy(canisterValue);
    }
    @Override
   public void transport(Monster monster){
            monster.move(-Math.abs(this.getEffect()));
    } 
    @Override
    public void onLand(Monster landingMonster, Monster opponentMonster){
        super.onLand(landingMonster, opponentMonster);
        this.transport(landingMonster);
        this.modifyCanisterEnergy(landingMonster,-Constants.SLIP_PENALTY);
    }
    
}
