package osnove_delovanja.entity;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.Color;

import osnove_delovanja.Razno.Konstante;
import osnove_delovanja.util.Constants;

public class Nasprotniki extends Entitete {
    public Nasprotniki(Point2D.Double startPos) {
        super(startPos);
    }

    /**
     * Move downward at a constant speed.
     */
    @Override
    public void update() {
        pos.y += Konstante.ENEMY_SPEED / 2;  // or Constants.ENEMY_SPEED if defined
    }

    /**
     * Draw the enemy as a red square.
     */
    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillRect((int)pos.x - 10, (int)pos.y - 10, 20, 20);
    }

    @Override
    protected double getWidth()  { return 20; }
    @Override
    protected double getHeight() { return 20; }
}