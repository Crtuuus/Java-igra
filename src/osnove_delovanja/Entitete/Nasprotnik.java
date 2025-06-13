package osnove_delovanja.Entitete;

import java.awt.geom.Point2D;
import java.util.List;

public abstract class Nasprotnik extends Entitete {

    public Nasprotnik(double x, double y, double width, double height, OblikaTip oblikaTip) {
        super(x, y, width, height, oblikaTip);
    }

    public Nasprotnik(Point2D.Double pos, double width, double height, OblikaTip oblikaTip) {
        super(pos, width, height, oblikaTip);
    }

    /** Lahko se preglasi v posameznem nasprotniku za detekcijo trkov z izstrelki */
    public boolean handleHits(List<Entitete> entities) {
        for (Entitete e : entities) {
            if (e instanceof Izstrelek iz && iz.isFriendly()) {
                if (iz.getBounds().intersects(getBounds())) {
                    iz.setAlive(false);
                    setAlive(false);
                    return true;
                }
            }
        }
        return false;
    }
    }

