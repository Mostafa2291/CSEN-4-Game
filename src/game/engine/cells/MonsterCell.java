package game.engine.cells;

import javax.management.relation.Role;

import game.engine.monsters.*;

public class MonsterCell extends Cell {

    private Monster cellMonster;//read only

    
    public MonsterCell(String name, Monster cellMonster) {
        super(name);
        this.cellMonster = cellMonster;
    }

    public Monster getCellMonster() {
        return cellMonster;
    }
    public void onLand(Monster landingMonster, Monster opponentMonster){
      if(landingMonster.getRole()==cellMonster.getRole()){
        landingMonster.executePowerupEffect
        
       
    }
 
     

    

}
