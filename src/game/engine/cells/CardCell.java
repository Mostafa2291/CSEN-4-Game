package game.engine.cells;
import game.engine.Board;
import game.engine.cards.Card;
import game.engine.monsters.Monster;

public class CardCell extends  Cell  {

    public CardCell(String name) {
        super(name);
    }
    

//i think i did this correct? honestly no idea
@Override
    public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster);
        Card landedCard = Board.drawCard();
        landedCard.performAction(landingMonster, opponentMonster);
        }


    }
    
