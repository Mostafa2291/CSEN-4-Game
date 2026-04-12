package game.engine.cards;
import game.engine.monsters.Monster;
public class ConfusionCard extends Card {
    private int duration;//read only

    public ConfusionCard(String name, String description, int rarity, int duration) {
        super(name, description, rarity, false);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }


      

}
