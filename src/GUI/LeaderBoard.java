package src.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import src.Login.Player;

public class LeaderBoard extends JFrame implements ActionListener {

    private Player player;
    private JButton backBT;
    public  LeaderBoard(Player player) {

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

        JLabel titleLB = new JLabel("Leaderboard",SwingConstants.CENTER);
        JTextArea scoreTA = new JTextArea();

        scoreTA.setEditable(false);
        scoreTA.setText(
            "Rank     Player          Score\n\n" +
            "1          " + "Player1 : " + player.getUsername() + "\n" +"Current Player : " + player.getUsername());

        backBT = new JButton("Back to Main Menu");

        add(titleLB,BorderLayout.NORTH);
        add(new JScrollPane(scoreTA),BorderLayout.CENTER);
        add(backBT,BorderLayout.SOUTH);

        backBT.addActionListener(this);
}
    public void Final() {

        setSize(450, 400);
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