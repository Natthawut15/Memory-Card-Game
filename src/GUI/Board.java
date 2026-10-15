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
        int first = logic.getFirstIndex();
        GameLogic.Result result = logic.selectCard(index);
        if (result == GameLogic.Result.BLOCKED) return;
        showCard(index);
        movesLB.setText("Moves: " + logic.getMoves());
        scoreLB.setText("Score: " + logic.getScore());

        if (result == GameLogic.Result.MATCH || result == GameLogic.Result.WIN) {
            all_BT[first].setEnabled(false);
            all_BT[index].setEnabled(false);
        }
        if (result == GameLogic.Result.WRONG) {
            int second = index;
            flipTimer = new Timer(800, e -> {
                hideCard(first);
                hideCard(second);
                logic.finishWrongPair();
            });
            flipTimer.setRepeats(false);
            flipTimer.start();
        }
        if (result == GameLogic.Result.WIN) finishGame();
        if (result == GameLogic.Result.LOSE) loseGame();
    }

    private void showCard(int index) {
        Card card = logic.getCards().get(index);
        ImageIcon icon = card.getImage();
        if (icon.getIconWidth() <= 0) {
            JOptionPane.showMessageDialog(this, "Image not found: " + card.getImagePath());
            all_BT[index].setText("?");
            return;
        }
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
        clock.reset();
        timeLB.setText("Time: 00:00");
        movesLB.setText("Moves: 0");
        scoreLB.setText("Score: 100");
        for (JButton button : all_BT) {
            button.setIcon(null);
            button.setText("?");
            button.setEnabled(true);
        }
        clock.start();
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
