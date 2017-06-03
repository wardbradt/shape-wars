/**
 * Created by wardbradt on 5/28/17.
 */
import java.awt.*;
import java.awt.geom.Ellipse2D;

public abstract class CircleEntity {
    private double lifetime, timeLived;
    private Ellipse2D.Double ellipse;
    private double[] center;
    private Color color;
    // represents the diameter and width, which are always equal.
    private double diameter;
    private boolean isActive;

    public CircleEntity(double diameter, double x, double y, Color color) {
        this.diameter = diameter;
        this.center = new double[]{x,y};
        this.color = color;
        this.lifetime = 5;
        timeLived = 0;
        isActive = false;

        ellipse = new Ellipse2D.Double(x, y, diameter, diameter);
    }

    public void updateEllipse() {
        ellipse = new Ellipse2D.Double(center[0], center[1], diameter, diameter);
    }

    public void incrementTimeLived(double x) {
        timeLived += x;
    }

    public abstract void reactHit(PlayablePolygon poly);

    public Ellipse2D.Double getEllipse() {
        return ellipse;
    }

    public boolean isActive() {
        return isActive;
    }

    public double[] getCenter() {
        return center;
    }

    public double getLifetime() {
        return lifetime;
    }

    public double getTimeLived() {
        return timeLived;
    }

    public void turnOn() {
        isActive = true;
        setColor(color);
    }

    public double getDiameter() {
        return diameter;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
