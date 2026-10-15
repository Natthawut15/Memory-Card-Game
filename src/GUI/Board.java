package src.GUI;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import src.Card.Card;
import src.Card.CardSet;
import src.Card.SelectCardSet;
import src.Login.Player;
import src.Login.ScoreCSV;

public class Board extends JFrame {

    private Player player;
    private CardSet cardSet;
    private ArrayList<Card> cards;
    private JButton[] all_BT;
    private JLabel timeLB;
    private JLabel movesLB;
    private JLabel scoreLB;
    private Timer gameTimer;
    private Timer flipTimer;

    private int seconds = 0;
    private int moves = 0;
    private int firstIndex = -1;
    private int secondIndex = -1;
    private int matchedPairs = 0;
    private int score = 100;
    private boolean waiting = false;
    private boolean gameOver = false;

    public Board(Player player, CardSet cardSet) {
        super("Memory Card Game");
        this.player = player;
        this.cardSet = cardSet;
        cards = cardSet.createCards();
        Init();
        setComponent();
        Final();
        startTimer();
    }

    public void Init() {
        setLayout(new BorderLayout(5, 5));
    }

    public void setComponent() {
        add(GamePanel(), BorderLayout.NORTH);
        add(CardPanel(), BorderLayout.CENTER);
    }

    public JPanel GamePanel() {
        JPanel p = new JPanel(new FlowLayout());
        JLabel playerLB = new JLabel("Player: " + player.getUsername());
        JLabel setLB = new JLabel("Set: " + cardSet.getName());
        timeLB = new JLabel("Time: 00:00");
        movesLB = new JLabel("Moves: 0");
        scoreLB = new JLabel("Score: 100");
        JButton newGameBT = new JButton("New Game");
        JButton selectSetBT = new JButton("Select Set");

        newGameBT.addActionListener(e -> resetGame());
        selectSetBT.addActionListener(e -> {
            stopTimers();
            new SelectCardSet(player);
            dispose();
        });

        p.add(playerLB);
        p.add(setLB);
        p.add(timeLB);
        p.add(movesLB);
        p.add(newGameBT);
        p.add(selectSetBT);
        p.add(scoreLB);
        return p;
    }

    public JPanel CardPanel() {
        JPanel p = new JPanel(new GridLayout(4, 4, 5, 5));
        all_BT = new JButton[16];

        for (int i = 0; i < all_BT.length; i++) {
            final int index = i;
            all_BT[i] = new JButton("?");
            all_BT[i].setFont(new Font("Arial", Font.BOLD, 28));
            all_BT[i].setPreferredSize(new Dimension(100, 100));
            all_BT[i].addActionListener(e -> flipCard(index));
            p.add(all_BT[i]);
        }
        return p;
    }

    private void flipCard(int index) {
        if (gameOver || waiting) return;
        if (cards.get(index).isMatched()) return;
        if (index == firstIndex) return;

        showCard(index);

        if (firstIndex == -1) {
            firstIndex = index;
        } else {
            secondIndex = index;
            moves++;
            movesLB.setText("Moves: " + moves);

            if (moves >= 9) {
                score = Math.max(0, score - 3);
                scoreLB.setText("Score: " + score);
            }

            checkMatch();

            
            if (!gameOver && score == 0) {
                loseGame();
            }
        }
    }

    private void showCard(int index) {
        ImageIcon icon = cards.get(index).getImage();
        if (icon.getIconWidth() <= 0) {
            JOptionPane.showMessageDialog(this,
                "Image not found: " + cards.get(index).getImagePath());
            all_BT[index].setText("?");
            return;
        }
        Image scaledImage = icon.getImage().getScaledInstance(
            100, 100, Image.SCALE_SMOOTH);
        all_BT[index].setText("");
        all_BT[index].setIcon(new ImageIcon(scaledImage));
    }

    private void hideCard(int index) {
        all_BT[index].setIcon(null);
        all_BT[index].setText("?");
    }

    private void checkMatch() {
        String firstImage = cards.get(firstIndex).getImagePath();
        String secondImage = cards.get(secondIndex).getImagePath();

        if (firstImage.equals(secondImage)) {
            cards.get(firstIndex).setMatched(true);
            cards.get(secondIndex).setMatched(true);
            all_BT[firstIndex].setEnabled(false);
            all_BT[secondIndex].setEnabled(false);
            matchedPairs++;
            firstIndex = -1;
            secondIndex = -1;

            if (matchedPairs == cards.size() / 2 && score > 0) {
                finishGame();
            }
        } else {
            waiting = true;
            flipTimer = new Timer(800, e -> {
                hideCard(firstIndex);
                hideCard(secondIndex);
                firstIndex = -1;
                secondIndex = -1;
                waiting = false;
                flipTimer.stop();
            });
            flipTimer.setRepeats(false);
            flipTimer.start();
        }
    }

    private void startTimer() {
        gameTimer = new Timer(1000, e -> {
            seconds++;
            int minutes = seconds / 60;
            int remainingSeconds = seconds % 60;
            timeLB.setText(String.format("Time: %02d:%02d", minutes, remainingSeconds));
        });
        gameTimer.start();
    }

    private void finishGame() {
        gameOver = true;
        stopTimers();
        ScoreCSV.saveScore(player.getUsername(), score, seconds);
        JOptionPane.showMessageDialog(this,
            "Congratulations! You matched all pairs!\n" +
            "Player: " + player.getUsername() + "\n" +
            "Score: " + score + "\n" +
            "Time: " + timeLB.getText().replace("Time: ", "") + "\n" +
            "Moves: " + moves,
            "Game Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loseGame() {
        gameOver = true;
        stopTimers();
        waiting = false;
        for (JButton button : all_BT) button.setEnabled(false);
        JOptionPane.showMessageDialog(this,
            "Game Over! Your score is 0.\nTry again with New Game.",
            "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetGame() {
        stopTimers();
        cards = cardSet.createCards();
        seconds = 0;
        moves = 0;
        matchedPairs = 0;
        score = 100;
        firstIndex = -1;
        secondIndex = -1;
        waiting = false;
        gameOver = false;
        timeLB.setText("Time: 00:00");
        movesLB.setText("Moves: 0");
        scoreLB.setText("Score: 100");

        for (int i = 0; i < all_BT.length; i++) {
            all_BT[i].setIcon(null);
            all_BT[i].setText("?");
            all_BT[i].setEnabled(true);
        }
        startTimer();
    }

    private void stopTimers() {
        if (gameTimer != null) gameTimer.stop();
        if (flipTimer != null) flipTimer.stop();
    }

    public void Final() {
        pack();
        setMinimumSize(new Dimension(700, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}