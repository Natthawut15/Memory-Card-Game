package src.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import src.GUI.Board;
import src.Login.Player;


public class SelectCardSet extends JFrame implements ActionListener {
    
    private Player player;
    private JButton animalBT;
    private JButton fruitBT;
    private JButton numberBT;

    public SelectCardSet(Player player) {

        super("Choose Card Set");

        this.player = player;

        Init();
        setComponent();
        Final();
    }

    public void Init() {

        setLayout(new GridLayout(4, 1, 10, 10));
    }

    public void setComponent() {

        JLabel titleLB = new JLabel("Choose Card Set", SwingConstants.CENTER);
        animalBT = new JButton("Animals");
        fruitBT = new JButton("Fruits");
        numberBT = new JButton("Numbers");

        animalBT.addActionListener(this);
        fruitBT.addActionListener(this);
        numberBT.addActionListener(this);

        add(titleLB);
        add(animalBT);
        add(fruitBT);
        add(numberBT);
    }
     
    public void Final() {

        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       
       if (e.getSource() == animalBT) {
            CardSet animalset = new CardSet("Animals", new String[] {
                "./Picture/animals/bear.png",
                "./Picture/animals/bird.png",
                "./Picture/animals/cat.png",
                "./Picture/animals/dog.png",
                "./Picture/animals/lion.png",
                "./Picture/animals/panda.png",
                "./Picture/animals/rabbit.png",
                "./Picture/animals/tiger.png",
            }
        );
            new Board(player, animalset);
            dispose();
        }
        else if (e.getSource() == fruitBT) {
            CardSet fruitset = new CardSet("Fruits", new String[]{
                "./Picture/fruits/apple.png",
                "./Picture/fruits/banana.png",
                "./Picture/fruits/grape.png",
                "./Picture/fruits/mango.png",
                "./Picture/fruits/Mushroom.png",
                "./Picture/fruits/orange.webp",
                "./Picture/fruits/pineapple.png",
                "./Picture/fruits/strawberry.png",
                
            }
        );
            new Board(player, fruitset);
            dispose();
        } else if (e.getSource() == numberBT) {
            CardSet numberset = new CardSet("Numbers", new String[]{
                "./Picture/number/1.png",
                "./Picture/number/2.png",
                "./Picture/number/3.png",
                "./Picture/number/4.png",
                "./Picture/number/5.png",
                "./Picture/number/6.png",
                "./Picture/number/7.png",
                "./Picture/number/8.png",
            }
        );
            new Board(player, numberset);
            dispose();
        }
    }
}