package game.engine.cells;
import game.engine.*;

public class DoorCell extends Cell {

    //Read only attributes
    private final Role  role;
    private final int  energy;

    //R&W attributes
    private boolean activated;


    public DoorCell(String name, Role role, int energy) {
        super(name);
        this.role = role;
        this.energy = energy;
        this.activated=false;
    }


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
   
    


}
