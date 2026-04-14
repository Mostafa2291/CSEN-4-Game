package game.engine.cells;
import java.util.ArrayList;
import game.engine.Board;
import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;


public class DoorCell extends Cell implements CanisterModifier {

    //Read only attributes
    private Role  role;
    private int  energy;

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
    public void setActivated(boolean isActivated) {
        this.activated = isActivated;
    }
     @Override
    public void modifyCanisterEnergy(Monster monster, int canisterValue){
        monster.alterEnergy(canisterValue);
    }
   @Override
    public void onLand(Monster landingMonster, Monster opponentMonster){
    if(this.isActivated()==false){
        ArrayList<Monster> stationedMonsters = Board.getStationedMonsters();
        //if the landing monster is of the same role as the door, it gets a boost in energy
        if(landingMonster.getRole()==this.getRole()){
            this.setActivated(true);
            this.modifyCanisterEnergy(landingMonster,this.energy);
            //monsters of the same role as the door get a boost as well in energy
            for(int i=0;i<stationedMonsters.size();i++){
                if((stationedMonsters.get(i)).getRole()==landingMonster.getRole()){
                    this.modifyCanisterEnergy(stationedMonsters.get(i),this.energy);
                }
            }
        }
        else{
            //if the landing monster is of a different role than the door
            if(landingMonster.getRole()!=this.getRole()){
                //if the landing monster is not shielded, it gets a decrease in energy and the door gets activated
                if(landingMonster.isShielded()==false){
                    this.setActivated(true);
                    this.modifyCanisterEnergy(landingMonster,-this.energy);
                    //monsters of a different role than the door get a decrease in energy as well
                    for(int i=0;i<stationedMonsters.size();i++){
                        if((stationedMonsters.get(i)).getRole()!=landingMonster.getRole()){
                            this.modifyCanisterEnergy(stationedMonsters.get(i),-this.energy);
                    }
                }
                }
                //if the landing monster is shielded, it loses its shield but does not get a decrease in energy and the door does not get activated
                else{
                    if(landingMonster.isShielded()==true){
                        landingMonster.setShielded(false);
                }
            }
        }
        
        }
    }
    }
}