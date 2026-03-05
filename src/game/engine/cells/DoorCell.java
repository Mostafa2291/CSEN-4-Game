package game.engine.cells;
import game.engine.*;

public class DoorCell extends Cell {
    private final Role  role;//read only
    private final int  energy;//read only
    private boolean activated;//read and write
    public Role getRole() {
        return role;
    }
    public int getEnergy() {
        return energy;
    }
    public boolean isActivated() {
        return activated;
    }
    public void setActivated(boolean activated) {
        this.activated = activated;
    }
    public DoorCell(String name, Role role, int energy) {
        super(name);
        this.role = role;
        this.energy = energy;
        this.activated=false;
    }
    


}
