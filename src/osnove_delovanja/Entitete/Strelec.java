package osnove_delovanja.Entitete;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import osnove_delovanja.Razno.Konstante;

public class Strelec extends Nasprotnik {
    private Igralec igralec;
    private long lastShotTime = 0;

    public Strelec(Point2D.Double pos, Igralec igralec) {
        super(pos, Konstante.NASPROTNIK_SIZE, Konstante.NASPROTNIK_SIZE, OblikaTip.TRIKOTNIK);
        this.igralec = igralec;
        this.speed = Konstante.STRELEC_SPEED;
    }

    public long getLastShotTime() {
        return lastShotTime;
    }

    public void setLastShotTime(long t) {
        this.lastShotTime = t;
    }

    @Override
    public void update() {
        // Strelec se lahko premika vodoravno ali miruje
        pos.y += speed;  // Pade navzdol kot osnovno premikanje
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        var shape = getOblika();
        g.fill(shape);
    }
}