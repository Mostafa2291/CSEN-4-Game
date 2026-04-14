package game.engine.cards;
import game.engine.monsters.Monster;
import game.engine.*;
public class ConfusionCard extends Card {
    private int duration;//read only

    public ConfusionCard(String name, String description, int rarity, int duration) {
        super(name, description, rarity, false);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public void performAction(Monster player, Monster opponent) {
        //swap roles of both player and opponent
        Role oppRole = opponent.getRole();
        opponent.setRole(player.getRole());
        player.setRole(oppRole);

        player.setConfusionTurns(duration);
        opponent.setConfusionTurns(duration);
    }
   
    

      
}
