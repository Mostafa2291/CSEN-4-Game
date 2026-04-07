package game.engine.monsters;
import game.engine.Constants;
import game.engine.Role;
public abstract class Monster implements Comparable <Monster> {

    //Read only attributes 
    private String name; 
    private String description;
    private Role originalRole; 

    //Read & Write attributes
    private Role role;
    private int energy; //must be >= 0
    private int position; // must be 0-99
    private boolean frozen;
    private boolean shielded; 
    private int confusionTurns;  //number of turns monster will be confused (becomes other role)


    public Monster(String name, String description, Role originalRole, int energy) {

    
        this.name = name;
        this.description = description;
        this.originalRole = originalRole;
        this.energy = energy;
        this.role = originalRole; //starts as initial role 
        this.position = 0;
        this.confusionTurns = 0; 
        this.frozen = false;
        this.shielded = false;

    }

    //GETTERS for read only fields
    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public Role getOriginalRole() {
        return originalRole;
    }

    //Getters & Setters for Read/Write fields
    public Role getRole() {
        return role;
    }


    public void setRole(Role role) {
        this.role = role;
    }


    public int getEnergy() {
        return energy;
    }


    public void setEnergy(int energy) { //added constraint where energy if negative is set to 0
       this.energy = Math.max(Constants.MIN_ENERGY, energy);
       
    }


    public int getPosition() {
        return position;
    }


    public void setPosition(int position) { //added constraints where postion must be between 0-99
        
        this.position = position % Constants.BOARD_SIZE;

    }


    public boolean isFrozen() {
        return frozen;
    }


    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }


    public boolean isShielded() {
        return shielded;
    }


    public void setShielded(boolean shielded) {
        this.shielded = shielded;
    }


    public int getConfusionTurns() {
        return confusionTurns;
    }


    public void setConfusionTurns(int confusionTurns) {
      
        this.confusionTurns = confusionTurns;
    }

    @Override
    public int compareTo(Monster other){
        return this.position - other.position ;  //if this.pos < o.pos --> -1  if this.pos> o.pos --> 1 else 0

    }

    


    



    



}
