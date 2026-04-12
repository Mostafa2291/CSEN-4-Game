package game.engine.cards;

import game.engine.monsters.Monster;

public class StartOverCard extends Card {

    public StartOverCard(String name, String description, int rarity, boolean lucky) {
        super(name, description, rarity, lucky);
    }
//method that executes the card’s unique effect on the player and/or opponent.
    @Override
    public void performAction(Monster player, Monster opponent) {
        //if card is lucky, opponent goes back to start 
       if(this.isLucky()==true) {
           opponent.setPosition(0);
       }
       //if card is not lucky, player goes back to start
       else {
           player.setPosition(0);
       }
}
}