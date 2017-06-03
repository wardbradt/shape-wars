import java.awt.*;

/**
 * Created by wardbradt on 5/28/17.
 */
public class SideIncreaser extends CircleEntity {
    private static final int height = 30;

    public SideIncreaser(double x, double y) {
        super(height, x, y, Color.GREEN);
    }

    public void reactHit(PlayablePolygon poly){
        if (poly.getSides() < 10) {
            poly.setRadius(poly.getRadius() * (poly.getSides() + 1) / poly.getSides());
            poly.incrementSides(1);
        }
    }
}
