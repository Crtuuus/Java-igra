package osnove_delovanja;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import osnove_delovanja.Entitete.*;
import osnove_delovanja.Razno.Konstante;

public class Game_panel extends JPanel implements Runnable, MouseMotionListener, MouseListener {
    private Thread gameThread;
    private volatile boolean running = true;

    private List<Entitete> entities = new ArrayList<>();

    public Game_panel() {
        setPreferredSize(new Dimension(Konstante.WIDTH, Konstante.HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addMouseMotionListener(this);
        addMouseListener(this);

        // initialize player, enemies, etc.
        // entities.add(new Player(...));

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        final int FPS = 60;
        final long targetTime = 1000 / FPS;

        while (running) {
            long start = System.currentTimeMillis();

            updateGame();
            repaint();

            long elapsed = System.currentTimeMillis() - start;
            long wait = targetTime - elapsed;
            if (wait > 0) {
                try { Thread.sleep(wait); } catch (InterruptedException ignored) {}
            }
        }
    }

    private void updateGame() {
        for (Entitete e : entities) {
            e.update();
        }
        entities.removeIf(e -> !e.isAlive());
        // spawn logic, collisions, etc.
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Entitete e : entities) {
            e.draw(g2);
        }
    }

    // stub mouse callbacks
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
}