package game.engine.cells;
import game.engine.monsters.*;
public abstract class TransportCell extends Cell {
    
    private int effect;//read only

    public TransportCell(String name, int effect) {
        super(name);
        this.effect = effect;
    }

    public void transport(Monster monster){

    }

    public int getEffect() {
        return effect;
    }

}
