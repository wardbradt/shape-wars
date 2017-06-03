import java.awt.*;

/**
 * Created by wardbradt on 5/29/17.
 */
public class DamagedPolygon extends PlayablePolygon {

    public DamagedPolygon(int x, int y, Color color, int sides, double radius) {
        super(x, y, color, sides, radius, 0);
    }

    public void hit(PlayablePolygon other) {
        // damage other if this is hitting other
        double velocity = Math.sqrt(Math.pow(getyVel(), 2) + Math.pow(getxVel(), 2));
        other.incrementDamage(velocity / 10);
    }

    public boolean isDead() {
        return getDamage() >= 100;
    }
}
