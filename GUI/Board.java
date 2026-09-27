package GUI;

import Card.Card;
import Card.Animation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * มีหน้าที่เดียว: เป็นหน้าต่างเกม + คุมกติกาการเล่นทั้งหมด
 * (สุ่มการ์ด, เช็คจับคู่, นับ moves, จับเวลา, อัปเดตป้ายข้อความ)
 */
public class Board extends JFrame {

    private static final String[] SYMBOLS = {
            "🍎", "🍌", "🍇", "🍉", "🍓", "🍒", "🍍", "🥝"
    };

    private final int rows = 4;
    private final int cols = 4;
    private final int totalPairs = (rows * cols) / 2;

    private Card[] cards;
    private int firstIndex = -1;
    private int secondIndex = -1;
    private boolean inputLocked = false;
    private int moves = 0;
    private int matchedPairs = 0;

    private JLabel movesLabel;
    private JLabel timeLabel;
    private JLabel statusLabel;

    private Timer clock;
    private int secondsElapsed = 0;

    public Board() {
        super("เกมจับคู่ภาพ - Memory Matching Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initTopPanel();
        initBoard();
        initStatusBar();
        startClock();

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        movesLabel = new JLabel("Moves: 0");
        timeLabel = new JLabel("Time: 0 seconds");
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restartGame());

        
        topPanel.add(movesLabel);
        topPanel.add(timeLabel);
        topPanel.add(restartButton);

        add(topPanel, BorderLayout.NORTH);
    }

    private void initBoard() {
        JPanel boardPanel = new JPanel(new GridLayout(rows, cols, 8, 8));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        int totalCards = rows * cols;
        String[] values = generateShuffledSymbols(totalCards);
        cards = new Card[totalCards];

        for (int i = 0; i < totalCards; i++) {
            final int index = i;
            Card card = new Card(values[i]);
            card.setPreferredSize(new Dimension(90, 90));
            card.setOnClick(() -> handleCardClick(index));
            cards[i] = card;
            boardPanel.add(card);
        }

        add(boardPanel, BorderLayout.CENTER);
    }

    private void initStatusBar() {
        statusLabel = new JLabel("Select 2 cards to match", SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private String[] generateShuffledSymbols(int totalCards) {
        List<String> pool = new ArrayList<>();
        int pairsNeeded = totalCards / 2;
        for (int i = 0; i < pairsNeeded; i++) {
            String symbol = SYMBOLS[i % SYMBOLS.length];
            pool.add(symbol);
            pool.add(symbol);
        }
        Collections.shuffle(pool);
        return pool.toArray(new String[0]);
    }

    private void handleCardClick(int index) {
        if (inputLocked) return;
        Card card = cards[index];
        if (card.isMatched() || card.isFaceUp()) return;

        Animation.pressBounce(card);
        inputLocked = true;

        Animation.flip(card, () -> card.setFaceUp(true), () -> {
            if (firstIndex == -1) {
                firstIndex = index;
                inputLocked = false; // ปลดล็อกให้เลือกใบที่สองได้
            } else {
                secondIndex = index;
                moves++;
                movesLabel.setText("Moves: " + moves);
                checkMatch();
            }
        });
    }

    private void checkMatch() {
        Card c1 = cards[firstIndex];
        Card c2 = cards[secondIndex];

        if (c1.getSymbol().equals(c2.getSymbol())) {
            c1.setMatched(true);
            c2.setMatched(true);
            Animation.matchPulse(c1);
            Animation.matchPulse(c2);
            matchedPairs++;
            statusLabel.setText("Correct match! 🎉");

            resetSelection();
            inputLocked = false;

            if (matchedPairs == totalPairs) {
                onGameWon();
            }
        } else {
            statusLabel.setText("No match. Try again.");

            Animation.shake(c1);
            Animation.shake(c2, () -> {
                // สั่นเสร็จจริง ๆ แล้ว ค่อยหน่วงสั้น ๆ ให้เห็นภาพก่อนพลิกกลับ (ไม่ค้างกลางอากาศ)
                javax.swing.Timer pause = new javax.swing.Timer(400, e -> {
                    Animation.flip(c1, () -> c1.setFaceUp(false), null);
                    Animation.flip(c2, () -> c2.setFaceUp(false), () -> {
                        resetSelection();
                        inputLocked = false;
                    });
                });
                pause.setRepeats(false);
                pause.start();
            });
        }
    }

    private void resetSelection() {
        firstIndex = -1;
        secondIndex = -1;
    }

    private void onGameWon() {
        stopClock();
        statusLabel.setText("🎉 Congratulations! You matched all pairs!");
        JOptionPane.showMessageDialog(this,
                String.format("You won!\nMoves used: %d\nTime used: %d seconds", moves, secondsElapsed),
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private void startClock() {
        secondsElapsed = 0;
        timeLabel.setText("Time: 0 seconds");
        clock = new Timer();
        clock.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsElapsed++;
                SwingUtilities.invokeLater(() -> timeLabel.setText("Time: " + secondsElapsed + " seconds"));
            }
        }, 1000, 1000);
    }

    private void stopClock() {
        if (clock != null) clock.cancel();
    }

    private void restartGame() {
        stopClock();
        moves = 0;
        matchedPairs = 0;
        firstIndex = -1;
        secondIndex = -1;
        inputLocked = false;
        movesLabel.setText("Moves: 0");
        statusLabel.setText("Select 2 cards to match");

        String[] values = generateShuffledSymbols(cards.length);
        for (int i = 0; i < cards.length; i++) {
            cards[i].reset(values[i]);
        }

        startClock();
    }
}