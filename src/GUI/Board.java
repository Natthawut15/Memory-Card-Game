package src.GUI;
//import Card.*;
import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import src.Card.Card;
import src.Card.CardSet;
import src.Login.Player;


public class Board extends JFrame {
    private Player player;
    private CardSet cardSet;

    private ArrayList<Card> cards;

    private JButton[] all_BT;

    public Board(Player player,CardSet cardSet) {

        super( "เกมจับคู่ภาพ Memory Card Game");

        this.player = player;
        this.cardSet = cardSet;

        // สร้างการ์ด
        cards = cardSet.createCards();

        Init();
        setComponent();
        Final();
    }

    public void Init() {

        Container cp =getContentPane();
        cp.setLayout(new BorderLayout());
    }

    public void setComponent() {

        getContentPane().add(GamePanel(),BorderLayout.NORTH);
        getContentPane().add(CardPanel(),BorderLayout.CENTER);
    }

    public JPanel GamePanel() {

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());

        JLabel playerLB = new JLabel("Player : " +player.getUsername());
        JLabel setLB = new JLabel("Set : " +cardSet.getName());
        JLabel timeLB = new JLabel( "Time : 00:00");
        JLabel movesLB = new JLabel( "Moves : 0");

        p.add(playerLB);
        p.add(setLB);
        p.add(timeLB);
        p.add(movesLB);

        return p;
    }

    public JPanel CardPanel() {

        JPanel p = new JPanel();

        p.setLayout( new GridLayout(4, 4));

        all_BT = new JButton[16];

        for (int i = 0;i < all_BT.length;i++) {

            all_BT[i] = new JButton();
            // แสดงรูปเพื่อทดสอบ
            all_BT[i].setIcon(cards.get(i).getImage());
            all_BT[i].setPreferredSize(new Dimension(100, 100));
            p.add(all_BT[i]);
        }

        return p;
    }

    public void Final() {
        pack();
        //setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}