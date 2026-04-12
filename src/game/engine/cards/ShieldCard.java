package game.engine.cards;

import game.engine.monsters.Monster;

public class ShieldCard extends Card{

    public ShieldCard(String name, String description, int rarity) {
        super(name, description, rarity, true);
    }
    //method that executes the card’s unique effect on the player and/or opponent.
    @Override
    public void performAction(Monster player, Monster opponent) {
        //remove shield from opponent and add shield to player
        opponent.setShielded(false);    
        player.setShielded(true);
    }

}
