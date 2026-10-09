package src.Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import src.GUI.Mainmenu;

public class Login extends JFrame implements ActionListener {
    
    private JTextField usernameTF;
    private JButton loginBT;

    public Login() {

        super("Memory Card Game - Login");

        Init();
        setComponent();
        Final();
    }


    public void Init() {

        setLayout(new GridLayout(2, 2, 10, 10));
    }

    public void setComponent() {

        JLabel usernameLB = new JLabel("Player Name :");
        usernameTF = new JTextField();
        loginBT = new JButton("Login");

        add(usernameLB);
        add(usernameTF);

        add(new JLabel());
        add(loginBT);

        loginBT.addActionListener(this);
        
    }

    public void Final() {

        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void login() {
        String username = usernameTF.getText().trim();
        // ตรวจสอบว่ากรอกชื่อหรือยัง
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Must enter a player name.","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        // สร้าง Player
        Player player = new Player(username);
        // บันทึกชื่อผู้เล่นลง CSV
        PlayerCSV.savePlayer(username);
        // ไปหน้า Main Menu
        new Mainmenu(player);
        // ปิดหน้า Login
        dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBT) {
            login();
        }
    }

}

    
