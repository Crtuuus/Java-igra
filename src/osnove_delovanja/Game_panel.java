
package osnove_delovanja;
import osnove_delovanja.Entitete.Izstrelek;
import osnove_delovanja.Entitete.Sledilec;
import osnove_delovanja.Entitete.Strelec;
import osnove_delovanja.Entitete.Igralec;
import osnove_delovanja.Entitete.Nasprotnik;
import osnove_delovanja.Entitete.Entitete;
import osnove_delovanja.Entitete.HiterNasprotnik;
import osnove_delovanja.Entitete.Ovira;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

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
    private int steviloIzstrelkov = Konstante.ST_ZACETNIH_IZSTRELKOV;
    private Runnable onGameOver;
    private boolean bombAvailable = false;
    private long lastBombUse = 0;
    private final long bombCooldown = 20000; // 20 seconds
    private boolean showBombEffect = false;
    private long bombEffectStart = 0;
    private final long bombEffectDuration = 1000; // 1 second
    private static List<Entitete> globalEntities;
    public static void setGlobalEntityList(List<Entitete> list) {
        globalEntities = list;
    }

    public static void addEntityFromStaticContext(Entitete e) {
        if (globalEntities != null) {
            synchronized (globalEntities) {
                globalEntities.add(e);
            }
        }
    }
    



    public Game_panel(Runnable onGameOver, StatusPanel statusPanel) {
        this.onGameOver = onGameOver;
        this.statusPanel = statusPanel;
        setPreferredSize(new Dimension(Konstante.WIDTH, Konstante.HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addMouseMotionListener(this);
        addMouseListener(this);

        player = new Igralec(new Point2D.Double(
            Konstante.WIDTH / 2.0,
            Konstante.HEIGHT / 2.0
        ));
        entities.add(player);
        Game_panel.setGlobalEntityList(entities);


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

        // Spawn enemies
        if (now - lastEnemyWave >= Konstante.INTERVALVALOV) {
            for (int i = 0; i < waveCount; i++) {
                double x = Math.random() * (Konstante.WIDTH - Konstante.NASPROTNIK_SIZE);
                Point2D.Double spawnPos = new Point2D.Double(x, -Konstante.NASPROTNIK_SIZE);
                if (Math.random() < 0.4) {
                    entities.add(new HiterNasprotnik(spawnPos, player));
                    entities.add(new Sledilec(spawnPos, player));
                } else {
                    entities.add(new Strelec(spawnPos, player));
                    entities.add(new Sledilec(spawnPos, player));
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

        // Sovražniki streljajo
        for (Entitete e : List.copyOf(entities)) {
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

        // Posodobi entitete in ovire
        for (Entitete e : List.copyOf(entities)) e.update();
        for (Ovira o : List.copyOf(obstacles)) o.update();

        // Trki izstrelkov z nasprotniki
        for (Entitete e : List.copyOf(entities)) {
            if (e instanceof Izstrelek izstrelek) {
                for (Entitete target : List.copyOf(entities)) {
                    if (target instanceof Nasprotnik nasprotnik && izstrelek.isFriendly()) {
                        if (izstrelek.getBounds().intersects(nasprotnik.getBounds())) {
                            izstrelek.setAlive(false);
                            nasprotnik.setAlive(false);
                            score += 1;
                            if (score % 20 == 0) {
                                steviloIzstrelkov++;
                            }
                            if (score >= 100) {
                                bombAvailable = true;
                            }
                        }
                    }
                }
            }
        }

        // Trki sovražnikovih izstrelkov z igralcem
        for (Entitete e : List.copyOf(entities)) {
            if (e instanceof Izstrelek iz && !iz.isFriendly()) {
                if (iz.getBounds().intersects(player.getBounds())) {
                    iz.setAlive(false);
                    lives--;
                    if (lives <= 0) {
                        running = false;
                        if (onGameOver != null) onGameOver.run();
                        return;
                    }
                }
            }
        }

        // Trki nasprotnikov z igralcem
        for (Entitete e : List.copyOf(entities)) {
            if (e instanceof Nasprotnik nasprotnik) {
                if (nasprotnik.getBounds().intersects(player.getBounds())) {
                    nasprotnik.setAlive(false);
                    lives--;
                    if (lives <= 0) {
                        running = false;
                        if (onGameOver != null) SwingUtilities.invokeLater(onGameOver);
                        return;
                    }
                }
            }
        }

        // Trki z ovirami
        for (Ovira o : List.copyOf(obstacles)) {
            if (player.getBounds().intersects(o.getBounds())) {
                player.revertLastMove();
            }
        }

        // Odstrani mrtve entitete
        entities.removeIf(e -> !e.isAlive());
        obstacles.removeIf(o -> o.getBounds().getY() > Konstante.HEIGHT + 300);

        // Posodobi prikaz
        if (statusPanel != null) {
            statusPanel.setScore(score);
            statusPanel.setLives(lives);
            statusPanel.repaint();
        }
    }


    public void resetGame() {
        entities.clear();
        obstacles.clear();
        waveCount = 1;
        score = 0;
        lives = Konstante.ZIVLJENJA;
        steviloIzstrelkov = Konstante.ST_ZACETNIH_IZSTRELKOV;
        running = true;

        player = new Igralec(new Point2D.Double(Konstante.WIDTH / 2.0, Konstante.HEIGHT / 2.0));
        entities.add(player);

        // ustavi staro nit če teče
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
        }

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        List<Entitete> entitiesCopy;
        List<Ovira> obstaclesCopy;

        synchronized (entities) {
            entitiesCopy = new ArrayList<>(entities);
        }
        synchronized (obstacles) {
            obstaclesCopy = new ArrayList<>(obstacles);
        }

        for (Entitete e : entitiesCopy) {
            e.draw(g2);
        }
        for (Ovira o : obstaclesCopy) {
            o.draw(g2);
        }

        g2.setColor(Color.WHITE);
        g2.drawString("Življenja: " + lives, 10, Konstante.HEIGHT - 30);
        g2.drawString("Točke: " + score, 10, Konstante.HEIGHT - 15);
        if (bombAvailable) {
            long cooldownRemaining = Math.max(0, bombCooldown - (System.currentTimeMillis() - lastBombUse));
            g2.drawString("Bomb cooldown: " + (cooldownRemaining / 1000.0) + "s", 10, Konstante.HEIGHT - 45);
        }

        if (showBombEffect) {
            g2.setColor(new Color(255, 100, 0, 120));
            g2.fillOval(0, 0, getWidth(), getHeight());
        }

    }


    @Override public void mouseMoved(MouseEvent e) {
        player.setTarget(new Point2D.Double(e.getX(), e.getY()));
    }
    @Override public void mouseDragged(MouseEvent e) { mouseMoved(e); }
    @Override
    public void mousePressed(MouseEvent e) {
        if (!running) return;

        if (SwingUtilities.isLeftMouseButton(e)) {
            Point2D.Double mouse = new Point2D.Double(e.getX(), e.getY());
            Point2D.Double center = player.getPos();
            double dx = mouse.x - center.x;
            double dy = mouse.y - center.y;
            double baseAngle = Math.atan2(dy, dx);
            double spread = Math.toRadians(15);

            for (int i = 0; i < steviloIzstrelkov; i++) {
                double angleOffset = spread * (i - (steviloIzstrelkov - 1) / 2.0);
                double angle = baseAngle + angleOffset;
                double spawnDist = player.getWidth()/2 + Konstante.IZSTRELEK_SIZE/2.0;
                double spawnX = center.x + Math.cos(angle) * spawnDist;
                double spawnY = center.y + Math.sin(angle) * spawnDist;

                entities.add(new Izstrelek(new Point2D.Double(spawnX, spawnY),
                        new Point2D.Double(center.x + Math.cos(angle) * 100,
                                           center.y + Math.sin(angle) * 100),
                        true));
            }
        }

        // Bomb: Right-click
        if (SwingUtilities.isRightMouseButton(e) && bombAvailable) {
            long now = System.currentTimeMillis();
            if (now - lastBombUse >= bombCooldown) {
                entities.removeIf(ent -> ent instanceof Nasprotnik);
                lastBombUse = now;
                showBombEffect = true;
                bombEffectStart = now;
        
            }
        
        }
    }


    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
}
