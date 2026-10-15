package src.Game;

import javax.swing.Timer;

public class GameTimer {
    private int seconds = 0;
    private Timer timer;

    public GameTimer(Runnable updateScreen) {
        timer = new Timer(1000, e -> {
            seconds++;
            updateScreen.run();
        });
    }

    public void start() { timer.start(); }
    public void stop() { timer.stop(); }
    public void reset() { stop(); seconds = 0; }
    public int getSeconds() { return seconds; }
    public String getTimeText() {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}
