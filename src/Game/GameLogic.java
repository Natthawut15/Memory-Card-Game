package src.Game;

import java.util.ArrayList;
import src.Card.Card;

public class GameLogic {
    public enum Result { BLOCKED, FIRST, MATCH, WRONG, WIN, LOSE }

    private ArrayList<Card> cards;
    private int firstIndex = -1;
    private int secondIndex = -1;
    private int moves = 0;
    private int matchedPairs = 0;
    private boolean waiting = false;
    private boolean gameOver = false;
    private ScoreManager scoreManager = new ScoreManager();

    public GameLogic(ArrayList<Card> cards) { this.cards = cards; }

    public Result selectCard(int index) {
        if (gameOver || waiting || index < 0 || index >= cards.size() ||
                cards.get(index).isMatched() || index == firstIndex) return Result.BLOCKED;

        if (firstIndex == -1) {
            firstIndex = index;
            return Result.FIRST;
        }

        secondIndex = index;
        moves++;
        scoreManager.onMove(moves);
        boolean same = cards.get(firstIndex).getImagePath()
                .equals(cards.get(secondIndex).getImagePath());

        if (same) {
            cards.get(firstIndex).setMatched(true);
            cards.get(secondIndex).setMatched(true);
            matchedPairs++;
            firstIndex = -1;
            secondIndex = -1;
            if (scoreManager.isOutOfPoints()) {
                gameOver = true;
                return Result.LOSE;
            }
            if (matchedPairs == cards.size() / 2) {
                gameOver = true;
                return Result.WIN;
            }
            return Result.MATCH;
        }
        if (scoreManager.isOutOfPoints()) {
            gameOver = true;
            return Result.LOSE;
        }
        waiting = true;
        return Result.WRONG;
    }

    public void finishWrongPair() {
        if (waiting) {
            firstIndex = -1;
            secondIndex = -1;
            waiting = false;
        }
    }

    public void reset(ArrayList<Card> newCards) {
        cards = newCards;
        firstIndex = -1;
        secondIndex = -1;
        moves = 0;
        matchedPairs = 0;
        waiting = false;
        gameOver = false;
        scoreManager.reset();
    }

    public ArrayList<Card> getCards() { return cards; }
    public int getMoves() { return moves; }
    public int getScore() { return scoreManager.getScore(); }
    public int getMatchedPairs() { return matchedPairs; }
    public int getFirstIndex() { return firstIndex; }
    public int getSecondIndex() { return secondIndex; }
    public boolean isWaiting() { return waiting; }
    public boolean isGameOver() { return gameOver; }
}
