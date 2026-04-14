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
    @Override
    public void onLand(Monster landingMonster, Monster opponentMonster){
        //if the landing monster has the same role as the cell monster, it executes its powerup effect on the opponent monster
      if(landingMonster.getRole()==cellMonster.getRole()){
        landingMonster.executePowerupEffect(opponentMonster);
      }
      //if the landing monster has a different role than the cell monster
       else{
        //
        if(landingMonster.getEnergy()>cellMonster.getEnergy()){
            //if the landing monster is shielded, it loses its shield and the cell monster's energy becomes the landing monster's energy
            if(landingMonster.isShielded()==true){
                landingMonster.setShielded(false);
                cellMonster.setEnergy(landingMonster.getEnergy());
            }
            //if the landing monster is not shielded,they swap their energy values
                int tmpenergy=landingMonster.getEnergy();
                landingMonster.setEnergy(cellMonster.getEnergy());
                cellMonster.setEnergy(tmpenergy);
            }
           
        }
    }
}
     

    


