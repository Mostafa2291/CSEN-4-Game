package game.engine.cells;

import game.engine.monsters.Monster;

public class Cell {
    private final String name;//read 
    private Monster monster;//read and write

    public String getName() {
        return name;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public Cell(String name) {
        this.name = name;
        this.monster=null;
    }
    
    
    

}
