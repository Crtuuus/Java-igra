package osnove_delovanja;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import osnove_delovanja.Entitete.*;
import osnove_delovanja.Razno.Konstante;
import osnove_delovanja.ui.StatusPanel;

public class Game_panel extends JPanel implements Runnable, MouseMotionListener, MouseListener {
    private Thread gameThread;
    private volatile boolean running = true;
    private final List<Entitete> entities = new ArrayList<>();
    private final List<Ovira> obstacles = new ArrayList<>();
    private Igralec player;
    private StatusPanel statusPanel;
    private long lastEnemyWave = System.currentTimeMillis();
    private long lastObstacleSpawn = System.currentTimeMillis();
    private int waveCount = 1;
    private int score = 0;
    private int lives = Konstante.ZIVLJENJA;

    public Game_panel() {
        setPreferredSize(new Dimension(Konstante.WIDTH, Konstante.HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addMouseMotionListener(this);
        addMouseListener(this);

        // initialize player in center
        player = new Igralec(new Point2D.Double(
            Konstante.WIDTH / 2.0,
            Konstante.HEIGHT / 2.0
        ));
        entities.add(player);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setStatusPanel(StatusPanel sp) {
        this.statusPanel = sp;
    }

    @Override
    public void run() {
        final int FPS = 60;
        final long frameTime = 1000L / FPS;
        while (running) {
            long start = System.currentTimeMillis();
            updateGame();
            repaint();
            long elapsed = System.currentTimeMillis() - start;
            try { Thread.sleep(Math.max(1, frameTime - elapsed)); } catch (InterruptedException ignored) {}
        }
    }

    private void updateGame() {
        long now = System.currentTimeMillis();
        // Spawn enemy wave
        if (now - lastEnemyWave >= Konstante.INTERVALVALOV) {
            for (int i = 0; i < waveCount; i++) {
                double x = Math.random() * (Konstante.WIDTH - 20);
                entities.add(new Nasprotniki(
                    new Point2D.Double(x, -20), player
                ));
            }
            waveCount++;
            lastEnemyWave = now;
        }
        // Spawn obstacles at top
        if (now - lastObstacleSpawn >= 5000) {
            double x = Math.random() * (Konstante.WIDTH - Konstante.OVIRASTEVILO);
            obstacles.add(new Ovira(
                new Point2D.Double(x, -Konstante.OVIRASTEVILO)
            ));
            lastObstacleSpawn = now;
        }

        // Update all entities and obstacles
        for (Entitete e : new ArrayList<>(entities)) e.update();
        for (Ovira o : new ArrayList<>(obstacles)) o.update();

        // Collisions: bullets vs. enemies
        for (Entitete e : new ArrayList<>(entities)) {
            if (e instanceof Nasprotniki) {
                for (Entitete b : new ArrayList<>(entities)) {
                    if (b instanceof izstrelek &&
                        e.getBounds().intersects(b.getBounds())) {
                        e.setAlive(false);
                        b.setAlive(false);
                        score += 10;
                    }
                }
            }
        }
        // Collisions: player vs. enemies
        for (Entitete e : new ArrayList<>(entities)) {
            if (e instanceof Nasprotniki &&
                e.getBounds().intersects(player.getBounds())) {
                e.setAlive(false);
                lives--;
                if (lives <= 0) running = false;
            }
        }
        // Collisions: player vs. obstacles
        for (Ovira o : obstacles) {
            if (player.getBounds().intersects(o.getBounds())) {
                player.revertLastMove();
            }
        }

        // Remove off-screen obstacles
        obstacles.removeIf(o -> o.getBounds().getY() > Konstante.HEIGHT);
        // Clean up dead entities
        entities.removeIf(ent -> !ent.isAlive());

        // Update HUD
        if (statusPanel != null) statusPanel.update(score, lives);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Entitete e : entities) e.draw(g2);
        for (Ovira o : obstacles) o.draw(g2);
    }

    @Override public void mouseMoved(MouseEvent e) {
        player.setTarget(new Point2D.Double(e.getX(), e.getY()));
    }
    @Override public void mouseDragged(MouseEvent e) { mouseMoved(e); }
    @Override public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && running) {
            entities.add(new izstrelek(
                player.getPos(), new Point2D.Double(e.getX(), e.getY())
            ));
        }
    }
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
}