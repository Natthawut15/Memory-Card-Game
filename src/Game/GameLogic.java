package src.Game;

import java.util.ArrayList;
import src.Card.Card;

public class GameLogic {
    private ArrayList<Card> cards;
    private ScoreManager score = new ScoreManager();
    private int firstIndex = -1;
    private int secondIndex = -1;
    private int matchedPairs = 0;
    private int moves = 0;
    private boolean waiting = false;
    private boolean gameOver = false;

    public GameLogic(ArrayList<Card> cards) { this.cards = cards; }

    // คืนค่า FIRST, MATCH, WRONG, WIN, LOSE หรือ BLOCKED
    public String flip(int index) {
        if (gameOver || waiting || index < 0 || index >= cards.size()
                || cards.get(index).isMatched() || index == firstIndex) return "BLOCKED";

        if (firstIndex == -1) {
            firstIndex = index;
            return "FIRST";
        }

        secondIndex = index;
        moves++;
        score.update(moves);
        boolean same = cards.get(firstIndex).getImagePath()
                .equals(cards.get(secondIndex).getImagePath());
        if (same) {
            cards.get(firstIndex).setMatched(true);
            cards.get(secondIndex).setMatched(true);
            matchedPairs++;
            firstIndex = -1;
            secondIndex = -1;
            if (matchedPairs == cards.size() / 2 && score.getScore() > 0) {
                gameOver = true;
                return "WIN";
            }
            if (score.getScore() == 0) {
                gameOver = true;
                return "LOSE";
            }
            return "MATCH";
        }
        waiting = true;
        if (score.getScore() == 0) {
            gameOver = true;
            return "LOSE";
        }
        return "WRONG";
    }

    public void hideWrongPair() {
        firstIndex = -1;
        secondIndex = -1;
        waiting = false;
    }

    public void reset(ArrayList<Card> newCards) {
        cards = newCards;
        score.reset();
        firstIndex = -1;
        secondIndex = -1;
        matchedPairs = 0;
        moves = 0;
        waiting = false;
        gameOver = false;
    }

    public ArrayList<Card> getCards() { return cards; }
    public int getMoves() { return moves; }
    public int getScore() { return score.getScore(); }
    public int getMatchedPairs() { return matchedPairs; }
    public int getFirstIndex() { return firstIndex; }
    public int getSecondIndex() { return secondIndex; }
    public boolean isWaiting() { return waiting; }
    public boolean isGameOver() { return gameOver; }
}
