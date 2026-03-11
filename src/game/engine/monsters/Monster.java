package game.engine.monsters;
import game.engine.Constants;
import game.engine.Role;
public abstract class Monster implements Comparable <Monster> {

    //Read only attributes 
    private final String name; 
    private final String description;
    private final Role originalRole; 

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

        if(energy>=0) //small safety line to make sure energy isnt initialised as less than 0
            this.energy = energy;
        else
            this.energy = 0;

        this.role = this.originalRole; //starts as initial role 

        this.position = Constants.STARTING_POSITION; //initially start at first index
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
        if(energy>=0)
            this.energy = energy;
        else
            this.energy = 0;
       
    }


    public int getPosition() {
        return position;
    }


    public void setPosition(int position) { //added constraints where postion must be between 0-99
        if(position>= Constants.STARTING_POSITION &&
        position<= Constants.WINNING_POSITION) 
            {
                this.position = position;
            }
        else
            {
                this.position = position % 100;
            }
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
        if(confusionTurns>=0)
            this.confusionTurns = confusionTurns;
        else
            this.confusionTurns = 0;
       
    }

    @Override
    public int compareTo(Monster o){
        return (Integer.compare(this.getPosition(), o.getPosition())); //if this.pos < o.pos --> -1  if this.pos> o.pos --> 1 else 0

    }

    


    



    



}
