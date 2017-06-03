import java.awt.*;

/**
 * Created by wardbradt on 5/29/17.
 */
public class HealthCircle extends CircleEntity{
    private static final int height = 30;

//    private double lifetime, timeLived;
    public HealthCircle(double x, double y) {
        super(height, x, y, Color.RED);
    }

    public void reactHit(PlayablePolygon poly){
        if (poly.getDamage() > 5)
            poly.incrementDamage(-5);
    }
}
