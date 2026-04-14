package game.engine.cells;

import game.engine.monsters.Monster;

public class ConveyorBelt extends TransportCell {

    public ConveyorBelt(String name, int effect) {
        super(name, effect); 
    }
    
    @Override
    //transports the monster by changing its position according to the effect of the conveyor belt
    public void transport(Monster monster){
            monster.move(this.getEffect()+monster.getPosition());
    }
    @Override
    //when a monster lands on the conveyor belt, it gets transported
    public void onLand(Monster landingMonster, Monster opponentMonster){
        this.transport(landingMonster);
    }

}
