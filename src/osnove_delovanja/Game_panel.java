package osnove_delovanja;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import osnove_delovanja.Entitete.Igralec;
import osnove_delovanja.Entitete.Nasprotniki;
import osnove_delovanja.Entitete.izstrelek;
import osnove_delovanja.Entitete.Entitete;
import osnove_delovanja.Razno.Konstante;

public class Game_panel extends JPanel implements Runnable, MouseMotionListener, MouseListener {
    private Thread gameThread;
    private volatile boolean running = true;
    private final List<Entitete> entities = new ArrayList<>();
    private Igralec player;

    public Game_panel() {
        setPreferredSize(new Dimension(Konstante.WIDTH, Konstante.HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addMouseMotionListener(this);
        addMouseListener(this);

        // initialize player in center
        player = new Igralec(
            new Point2D.Double(Konstante.WIDTH / 2.0, Konstante.HEIGHT / 2.0)
        );
        entities.add(player);

        // test enemy
        entities.add(new Nasprotniki(
            new Point2D.Double(100, -20)
        ));

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        final int FPS = 60;
        final long targetTime = 1000L / FPS;

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
        // update positions
        for (Entitete e : new ArrayList<>(entities)) {
            e.update();
        }
        // collision: bullets vs enemies
        for (Entitete e : new ArrayList<>(entities)) {
            if (e instanceof Nasprotniki) {
                for (Entitete b : new ArrayList<>(entities)) {
                    if (b instanceof izstrelek
                        && e.getBounds().intersects(b.getBounds())) {
                        e.setAlive(false);
                        b.setAlive(false);
                    }
                }
            }
        }
        // remove dead
        entities.removeIf(ent -> !ent.isAlive());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Entitete e : entities) {
            e.draw(g2);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        player.setTarget(
            new Point2D.Double(e.getX(), e.getY())
        );
    }

    @Override public void mouseDragged(MouseEvent e) { mouseMoved(e); }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            entities.add(new izstrelek(
                player.getPos(),
                new Point2D.Double(e.getX(), e.getY())
            ));
        }
    }

    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
}