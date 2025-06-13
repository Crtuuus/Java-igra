package osnove_delovanja;

import osnove_delovanja.Entitete.*;
import osnove_delovanja.Razno.Konstante;
import osnove_delovanja.ui.StatusPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

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

        player = new Igralec(new Point2D.Double(Konstante.WIDTH / 2.0, Konstante.HEIGHT / 2.0));
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
            try {
                Thread.sleep(Math.max(1, frameTime - elapsed));
            } catch (InterruptedException ignored) {}
        }
    }

    private void updateGame() {
        long now = System.currentTimeMillis();

        // Spawn enemies
        if (now - lastEnemyWave >= Konstante.INTERVALVALOV) {
            for (int i = 0; i < waveCount; i++) {
                double x = Math.random() * (Konstante.WIDTH - Konstante.NASPROTNIK_SIZE);
                Point2D.Double spawnPos = new Point2D.Double(x, 0);
                if (Math.random() < 0.5) {
                    entities.add(new Sledilec(spawnPos, player));
                } else {
                    entities.add(new Strelec(spawnPos, player));
                }
            }
            waveCount++;
            lastEnemyWave = now;
        }

        // Spawn obstacles
        if (now - lastObstacleSpawn >= Konstante.OVIRA_INTERVAL) {
            double x = Math.random() * (Konstante.WIDTH - Konstante.OVIRA_SIZE);
            obstacles.add(new Ovira(new Point2D.Double(x, -Konstante.OVIRA_SIZE)));
            lastObstacleSpawn = now;
        }

        // Enemy shooting
        for (Entitete e : new ArrayList<>(entities)) {
            if (e instanceof Strelec s) {
                if (now - s.getLastShotTime() >= Konstante.STRELEC_FIRE_RATE) {
                    Point2D.Double origin = new Point2D.Double(
                        s.getPos().x + s.getWidth() / 2,
                        s.getPos().y + s.getHeight() / 2
                    );
                    entities.add(new Izstrelek(origin, player.getPos(), false));
                    s.setLastShotTime(now);
                }
            }
        }

        // Update entities
        for (Entitete e : new ArrayList<>(entities)) e.update();
        for (Ovira o : new ArrayList<>(obstacles)) o.update();

        // Handle collisions
        for (Entitete e : new ArrayList<>(entities)) {
            if (e instanceof Izstrelek izstrelek) {
                for (Entitete target : new ArrayList<>(entities)) {
                    if (target instanceof Nasprotnik nasprotnik && izstrelek.isFriendly()) {
                        if (izstrelek.getBounds().intersects(nasprotnik.getBounds())) {
                            izstrelek.setAlive(false);
                            nasprotnik.setAlive(false);
                            score += 10;
                        }
                    }
                }
            } else if (e instanceof Nasprotnik nasprotnik) {
                if (nasprotnik.getBounds().intersects(player.getBounds())) {
                    nasprotnik.setAlive(false);
                    lives--;
                }
            }
        }

        for (Entitete e : new ArrayList<>(entities)) {
            if (e instanceof Izstrelek iz) {
                if (!iz.isFriendly() && iz.getBounds().intersects(player.getBounds())) {
                    iz.setAlive(false);
                    lives--;
                    if (lives <= 0) running = false;
                }
            }
        }

        for (Ovira o : new ArrayList<>(obstacles)) {
            if (player.getBounds().intersects(o.getBounds())) {
                player.revertLastMove();
            }
        }

        // Cleanup
        entities.removeIf(ent -> !ent.isAlive());
        obstacles.removeIf(o -> o.getBounds().getY() > Konstante.HEIGHT);

        // Update HUD
        if (statusPanel != null) {
            statusPanel.setScore(score);
            statusPanel.setLives(lives);
            statusPanel.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Entitete e : entities) e.draw(g2);
        for (Ovira o : obstacles) o.draw(g2);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        player.setTarget(new Point2D.Double(e.getX(), e.getY()));
    }

    @Override public void mouseDragged(MouseEvent e) { mouseMoved(e); }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && running) {
            Point2D.Double mouse = new Point2D.Double(e.getX(), e.getY());
            Point2D.Double center = player.getPos();
            double dx = mouse.x - center.x;
            double dy = mouse.y - center.y;
            double angle = Math.atan2(dy, dx);
            double spawnDist = player.getWidth() / 2 + Konstante.IZSTRELEK_SIZE / 2.0;
            double spawnX = center.x + Math.cos(angle) * spawnDist;
            double spawnY = center.y + Math.sin(angle) * spawnDist;
            entities.add(new Izstrelek(new Point2D.Double(spawnX, spawnY), mouse, true));
        }
    }

    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
}
