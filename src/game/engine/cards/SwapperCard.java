package game.engine.cards;

import game.engine.monsters.Monster;

public class SwapperCard extends Card {

    public SwapperCard(String name, String description, int rarity) {
        super(name, description, rarity,  true);
        
    }
   //If the player is behind the opponent in position, the two monsters swap their positions.
    @Override
    public void performAction(Monster player, Monster opponent) {
        //swap positions of player and opponent if player is behind opponent
        if(player.getPosition() < opponent.getPosition()) {
            //swap positions
            int tempPosition = player.getPosition();
            player.setPosition(opponent.getPosition());
            opponent.setPosition(tempPosition);
        }
    }
 

}
