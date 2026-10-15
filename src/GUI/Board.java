package src.GUI;

import java.awt.*;
import javax.swing.*;
import src.Card.Card;
import src.Card.CardSet;
import src.Card.SelectCardSet;
import src.Game.GameLogic;
import src.Game.GameTimer;
import src.Login.Player;
import src.Login.ScoreCSV;

public class Board extends JFrame {
    private Player player;
    private CardSet cardSet;
    private GameLogic logic;
    private GameTimer clock;
    private JButton[] all_BT;
    private JLabel timeLB;
    private JLabel movesLB;
    private JLabel scoreLB;
    private Timer flipTimer;

    public Board(Player player, CardSet cardSet) {
        super("Memory Card Game");
        this.player = player;
        this.cardSet = cardSet;
        logic = new GameLogic(cardSet.createCards());
        Init();
        setComponent();
        clock = new GameTimer(() -> timeLB.setText("Time: " + clock.getTimeText()));
        Final();
        clock.start();
    }

    public void Init() { setLayout(new BorderLayout(5, 5)); }

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
        JButton leaderboardBT = new JButton("Leaderboard");
        newGameBT.addActionListener(e -> resetGame());
        selectSetBT.addActionListener(e -> {
            stopTimers();
            new SelectCardSet(player);
            dispose();
        });
        leaderboardBT.addActionListener(e -> {
            stopTimers();
            new LeaderBoard(player);
            dispose();
        });
        p.add(playerLB);
        p.add(setLB);
        p.add(timeLB);
        p.add(movesLB);
        p.add(newGameBT);
        p.add(selectSetBT);
        p.add(leaderboardBT);
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
        // ตรวจรูปก่อนเปลี่ยนสถานะ เพื่อไม่ให้นับใบที่ไม่มีภาพ
        Card card = logic.getCards().get(index);
        if (logic.isGameOver() || logic.isWaiting() || card.isMatched()
                || index == logic.getFirstIndex()) return;
        ImageIcon icon = card.getImage();
        if (icon.getIconWidth() <= 0) {
            JOptionPane.showMessageDialog(this, "Image not found: " + card.getImagePath());
            return;
        }

        int first = logic.getFirstIndex();
        String result = logic.flip(index);
        if (result.equals("BLOCKED")) return;
        showCard(index, icon);
        movesLB.setText("Moves: " + logic.getMoves());
        scoreLB.setText("Score: " + logic.getScore());

        if (result.equals("MATCH") || result.equals("WIN")) {
            all_BT[first].setEnabled(false);
            all_BT[index].setEnabled(false);
        }
        if (result.equals("WRONG")) {
            int second = index;
            flipTimer = new Timer(800, e -> {
                hideCard(first);
                hideCard(second);
                logic.hideWrongPair();
                flipTimer.stop();
            });
            flipTimer.setRepeats(false);
            flipTimer.start();
        }
        if (result.equals("WIN")) finishGame();
        if (result.equals("LOSE")) loseGame();
    }

    private void showCard(int index, ImageIcon icon) {
        Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        all_BT[index].setText("");
        all_BT[index].setIcon(new ImageIcon(image));
    }

    private void hideCard(int index) {
        all_BT[index].setIcon(null);
        all_BT[index].setText("?");
    }

    private void finishGame() {
        stopTimers();
        ScoreCSV.saveScore(player.getUsername(), logic.getScore(), clock.getSeconds());
        JOptionPane.showMessageDialog(this,
            "Congratulations! You matched all pairs!\n" +
            "Player: " + player.getUsername() + "\n" +
            "Score: " + logic.getScore() + "\n" +
            "Time: " + clock.getTimeText() + "\n" +
            "Moves: " + logic.getMoves(),
            "Game Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loseGame() {
        stopTimers();
        for (JButton button : all_BT) button.setEnabled(false);
        JOptionPane.showMessageDialog(this,
            "Game Over! Your score is 0.\nTry again with New Game.",
            "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetGame() {
        stopTimers();
        logic.reset(cardSet.createCards());
        timeLB.setText("Time: 00:00");
        movesLB.setText("Moves: 0");
        scoreLB.setText("Score: 100");
        for (JButton button : all_BT) {
            button.setIcon(null);
            button.setText("?");
            button.setEnabled(true);
        }
        clock.reset();
    }

    private void stopTimers() {
        if (clock != null) clock.stop();
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
