package Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * มีหน้าที่เดียว: เก็บสถานะการ์ด 1 ใบ (สัญลักษณ์, คว่ำ/หงาย, จับคู่แล้วหรือยัง)
 * และวาดตัวเองตามสถานะ + ค่า animation ปัจจุบัน (ที่ Animation.java เป็นคนสั่งเปลี่ยนค่า)
 */
public class Card extends JPanel {

    private String symbol;
    private boolean faceUp = false;
    private boolean matched = false;

    // ค่าที่ Animation.java จะปรับเพื่อสร้างเอฟเฟกต์ต่าง ๆ
    private double scaleX = 1.0;
    private double pulseScale = 1.0;
    private double shakeX = 0.0;

    private static final Color BACK_COLOR = new Color(70, 130, 180);
    private static final Color FRONT_COLOR = Color.WHITE;
    private static final Color MATCH_COLOR = new Color(144, 238, 144);

    public Card(String symbol) {
        this.symbol = symbol;
        setOpaque(false);
    }

    public void setOnClick(Runnable action) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
    }

    public String getSymbol() { return symbol; }
    public boolean isFaceUp() { return faceUp; }
    public boolean isMatched() { return matched; }
    public void setFaceUp(boolean faceUp) { this.faceUp = faceUp; repaint(); }
    public void setMatched(boolean matched) { this.matched = matched; repaint(); }

    // ---------- ให้ Animation.java เรียกใช้เพื่อขยับค่าทีละเฟรม ----------
    public void setScaleX(double v) { this.scaleX = v; repaint(); }
    public void setPulseScale(double v) { this.pulseScale = v; repaint(); }
    public void setShakeX(double v) { this.shakeX = v; repaint(); }

    public void reset(String newSymbol) {
        this.symbol = newSymbol;
        this.faceUp = false;
        this.matched = false;
        this.scaleX = 1.0;
        this.pulseScale = 1.0;
        this.shakeX = 0.0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.translate(w / 2.0 + shakeX, h / 2.0);
        g2.scale(Math.max(0.02, scaleX * pulseScale), pulseScale);
        g2.translate(-w / 2.0, -h / 2.0);

        RoundRectangle2D rect = new RoundRectangle2D.Double(4, 4, w - 8, h - 8, 16, 16);
        Color fillColor = matched ? MATCH_COLOR : (faceUp ? FRONT_COLOR : BACK_COLOR);
        g2.setColor(fillColor);
        g2.fill(rect);
        g2.setColor(new Color(0, 0, 0, 60));
        g2.draw(rect);

        if (faceUp || matched) {
            drawCentered(g2, symbol, w, h, new Font("Serif", Font.PLAIN, 34), Color.BLACK);
        } else {
            drawCentered(g2, "?", w, h, new Font("Serif", Font.BOLD, 30), Color.WHITE);
        }

        g2.dispose();
    }

    private void drawCentered(Graphics2D g2, String text, int w, int h, Font font, Color color) {
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(text);
        int th = fm.getAscent();
        g2.setColor(color);
        g2.drawString(text, (w - tw) / 2.0f, (h + th) / 2.0f - 4);
    }
}
