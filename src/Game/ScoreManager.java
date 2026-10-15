package src.Game;

public class ScoreManager {
    private int score = 100;

    public void onMove(int moves) {
        if (moves >= 9) score = Math.max(0, score - 3);
    }

    public int getScore() { return score; }
    public boolean isOutOfPoints() { return score == 0; }
    public void reset() { score = 100; }
}
