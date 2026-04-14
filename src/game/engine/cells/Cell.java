package game.engine.cells;

import game.engine.monsters.Monster;

public class Cell {
    
    private String name;//read 
    private Monster monster;//read and write



    public Cell(String name) {
        this.name = name;
        this.monster=null;
    }    

    public String getName() {
        return name;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }
    //checks if cell is occupied by a monster or not
    public boolean isOccupied() {
        if(this.getMonster()==null){
            return false;
        }
        else{
            return true;
        }
    }
    public void onLand(Monster landingMonster, Monster opponentMonster){
        this.setMonster(landingMonster);
    }
    public void transport(Monster monster){
        this.setMonster(monster);
    }
    
}
