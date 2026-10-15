package src.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import src.Login.Player;
import src.Login.ScoreCSV;

public class LeaderBoard extends JFrame implements ActionListener {
    private Player player;
    private JButton backBT;

    public LeaderBoard(Player player) {
        super("Leaderboard");
        this.player = player;

        Init();
        setComponent();
        Final();
    }

    public void Init() {

        setLayout(new BorderLayout(10, 10));
    }

    public void setComponent() {
        JLabel titleLB = new JLabel("Leaderboard", SwingConstants.CENTER);
        JTextArea scoreTA = new JTextArea()
        ;
        scoreTA.setEditable(false);
        scoreTA.setFont(new Font("Monospaced", Font.PLAIN, 14));

        ArrayList<ScoreCSV.Score> scores = ScoreCSV.getHighScores();
        StringBuilder text = new StringBuilder();
        text.append(String.format("%-6s %-20s %-8s %s%n", "Rank", "Player", "Score", "Time"));
        text.append("------------------------------------------------\n");
        if (scores.isEmpty()) {
            text.append("No winners yet.\n");
        } else {
            for (int i = 0; i < scores.size(); i++) {
                ScoreCSV.Score s = scores.get(i);
                text.append(String.format("%-6d %-20s %-8d %02d:%02d%n",
                    i + 1, s.username, s.points, s.seconds / 60, s.seconds % 60));
            }
        }
        text.append("\nCurrent Player: " + player.getUsername());
        scoreTA.setText(text.toString());

        backBT = new JButton("Back to Main Menu");
        backBT.addActionListener(this);
        add(titleLB, BorderLayout.NORTH);
        add(new JScrollPane(scoreTA), BorderLayout.CENTER);
        add(backBT, BorderLayout.SOUTH);
    }

    public void Final() {
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBT) {
            new Mainmenu(player);
            dispose();
        }
    }
}
