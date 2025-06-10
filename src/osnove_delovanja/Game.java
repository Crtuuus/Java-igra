package osnove_delovanja;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class Game extends JPanel implements Runnable, MouseMotionListener, MouseListener {
    // window size
    private static final int WIDTH = 1200, HEIGHT = 1200;
    // game thread
    private Thread thread;
    private volatile boolean running = true;

    // player
    private Point2D.Double playerPos = new Point2D.Double(WIDTH/2.0, HEIGHT/2.0);
    private Point2D.Double playerTarget = new Point2D.Double(playerPos.x, playerPos.y);
    private final double playerSpeed = 3.0;
    private final double shipSize = 20;

    // bullets
    private List<Bullet> bullets = new ArrayList<>();
    private final double bulletSpeed = 8;

    // enemies
    private List<Enemy> enemies = new ArrayList<>();
    private final double enemySpeed = 2;
    private final int maxEnemies = 10;
    private Random rnd = new Random();

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        addMouseMotionListener(this);
        addMouseListener(this);

        thread = new Thread(this);
        thread.start();
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
            if (wait > 0) try { Thread.sleep(wait); } catch (InterruptedException ignored) {}
        }
    }

    private void updateGame() {
        // move player toward mouse
        double dx = playerTarget.x - playerPos.x;
        double dy = playerTarget.y - playerPos.y;
        double dist = Math.hypot(dx, dy);
        if (dist > 1) {
            playerPos.x += (dx / dist) * playerSpeed;
            playerPos.y += (dy / dist) * playerSpeed;
        }

        // update bullets
        Iterator<Bullet> bit = bullets.iterator();
        while (bit.hasNext()) {
            Bullet b = bit.next();
            b.update();
            if (!b.isAlive()) bit.remove();
        }

        // spawn enemies up to max
        if (enemies.size() < maxEnemies && rnd.nextInt(100) < 2) {
            double ex = rnd.nextInt(WIDTH);
            enemies.add(new Enemy(new Point2D.Double(ex, -20)));
        }

        // update enemies
        Iterator<Enemy> eit = enemies.iterator();
        while (eit.hasNext()) {
            Enemy e = eit.next();
            e.update();
            // remove if off screen
            if (e.pos.y > HEIGHT + 20) {
                eit.remove();
                continue;
            }
            // check collisions with bullets
            for (Bullet b : bullets) {
                if (e.collides(b.pos)) {
                    e.alive = false;
                    b.alive = false;
                }
            }
            if (!e.alive) eit.remove();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        // draw player as a triangle pointing toward velocity
        double angle = Math.atan2(playerTarget.y - playerPos.y, playerTarget.x - playerPos.x);
        AffineTransform old = g2.getTransform();
        g2.translate(playerPos.x, playerPos.y);
        g2.rotate(angle);
        g2.setColor(Color.CYAN);
        int[] xs = { (int)shipSize, -(int)shipSize/2, -(int)shipSize/2 };
        int[] ys = { 0, (int)shipSize/2, -(int)shipSize/2 };
        g2.fillPolygon(xs, ys, 3);
        g2.setTransform(old);

        // draw bullets
        g2.setColor(Color.YELLOW);
        for (Bullet b : bullets) {
            g2.fillOval((int)b.pos.x - 4, (int)b.pos.y - 4, 8, 8);
        }

        // draw enemies
        g2.setColor(Color.RED);
        for (Enemy e : enemies) {
            g2.fillRect((int)e.pos.x - 10, (int)e.pos.y - 10, 20, 20);
        }
    }

    // mouse motion
    @Override
    public void mouseMoved(MouseEvent e) {
        playerTarget.x = e.getX();
        playerTarget.y = e.getY();
    }
    @Override public void mouseDragged(MouseEvent e) { mouseMoved(e); }

    // mouse click to shoot
    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            bullets.add(new Bullet(new Point2D.Double(playerPos.x, playerPos.y),
                                   new Point2D.Double(e.getX(), e.getY())));
        }
    }
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // unused
    @Override public void mouseClicked(MouseEvent e) {}

    // --- Inner classes for bullets and enemies ---

    private class Bullet {
        Point2D.Double pos, vel;
        boolean alive = true;
        Bullet(Point2D.Double start, Point2D.Double target) {
            this.pos = new Point2D.Double(start.x, start.y);
            double dx = target.x - start.x, dy = target.y - start.y;
            double d = Math.hypot(dx, dy);
            vel = new Point2D.Double((dx/d)*bulletSpeed, (dy/d)*bulletSpeed);
        }
        void update() {
            pos.x += vel.x;
            pos.y += vel.y;
            if (pos.x < -10 || pos.x > WIDTH+10 || pos.y < -10 || pos.y > HEIGHT+10) 
                alive = false;
        }
        boolean isAlive() { return alive; }
    }

    private class Enemy {
        Point2D.Double pos;
        boolean alive = true;
        Enemy(Point2D.Double start) {
            this.pos = start;
        }
        void update() {
            pos.y += enemySpeed;
        }
        boolean collides(Point2D.Double p) {
            return Math.abs(p.x - pos.x) < 12 && Math.abs(p.y - pos.y) < 12;
        }
    }

    // --- Main to launch the game ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Divje vesolje");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            Game panel = new Game();
            window.add(panel);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}
