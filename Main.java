import GUI.Board;
import javax.swing.SwingUtilities;

/**
 * จุดเริ่มต้นโปรแกรม มีหน้าที่เดียว: สั่งสร้างและแสดงหน้าต่างเกม
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Board board = new Board();
            board.setVisible(true);
        });
    }
}
