package src.GUI;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import src.Login.Player;
import src.Card.SelectCardSet;
import src.Login.Login;


public class Mainmenu extends JFrame implements ActionListener {
    
    private Player player;
    private JButton startGameBT;
    private JButton leaderboardBT;
    private JButton logoutBT;

    public Mainmenu(Player player) {

        super("Memory Card Game - Main Menu");

        this.player = player;

        Init();
        setComponent();
        Final();
    }

    public void Init() {

        setLayout(
            new GridLayout(5,1,10,10)
        );
    }

    public void setComponent() {

        JLabel titleLB = new JLabel("Memory Card Game",SwingConstants.CENTER);
        JLabel playerLB = new JLabel("Player : " +player.getUsername(),SwingConstants.CENTER);

        startGameBT = new JButton("Start Game");
        leaderboardBT = new JButton("Leaderboard");
        logoutBT = new JButton("Logout");

        startGameBT.addActionListener(this);
        leaderboardBT.addActionListener(this);
        logoutBT.addActionListener(this);

        add(titleLB);
        add(playerLB);
        add(startGameBT);
        add(leaderboardBT);
        add(logoutBT);    
}
public void Final() {

        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

@Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == startGameBT) {
        new SelectCardSet(player);
        dispose();

    } else if (e.getSource() == leaderboardBT) {
        new LeaderBoard(player);
        dispose();

    } else if (e.getSource() == logoutBT) {
        new Login();
        dispose();
    }

}
}
