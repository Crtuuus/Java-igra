package osnove_delovanja.ui;

import javax.swing.JPanel;

import osnove_delovanja.Razno.Konstante;

import java.awt.Graphics;
import java.awt.Color;

public class StatusPanel extends JPanel {
    private int score = 0;
    private int lives = Konstante.ZIVLJENJA;

    public void setScore(int score) {
        this.score = score;
    }
    public void setLives(int lives) {
        this.lives = lives;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawString("Življenja: " + lives, 10, 20);
        g.drawString("Točke: " + score, 10, 40);
    }
}
