/**
 * An extension of Java's java.awt.Polygon class that is designed for 2D games where tracking the location
 * and movement of a Polygon are necessary.
 *
 * Created by Ward Bradt on 5/26/17.
 *
 * Sources Used:
 * // https://stackoverflow.com/questions/15690846/java-collision-detection-between-two-shape-objects
 */
import java.awt.*;
import java.awt.geom.Area;
public class PlayablePolygon {
    private Polygon polygon;
    private double[] xPoints, yPoints;
    private double[] center;
    private double xVel, yVel;
    private Color color;
    private boolean invincible;
    private double damage;

    private double angle;
    private double radius;
    private int sides;

    /**
     * Constructs a regular polygon for the use of the Game3 class.
     * @param x The center x-coord of the polygon
     * @param y The center x-coord of the polygon
     * @param color This polygon's color
     * @param sides How many sides it has
     * @param radius the distance from (x,y) to a vertex.
     */
    public PlayablePolygon(int x, int y, Color color, int sides, double radius) {
        this(x, y, color, sides, radius, 0);
    }

    public PlayablePolygon(int x, int y, Color color, int sides, double radius, double angle) {
        if (sides < 3) throw new IllegalArgumentException("There must be at least 3 sides in a polygon!");

        xPoints = new double[sides];
        yPoints = new double[sides];
        center = new double[]{x, y};
        this.angle = angle;
        this.sides = sides;
        this.radius = radius;
        this.color = color;

        updatePolygon();

        xVel = 0;
        yVel = 0;
        damage = 0;
        invincible = false;
    }

    /**
     * Tests if two polygons are colliding mainly by using Polygon's contains method, which tests if a given
     * point at (x,y) is within the bounds of a Polygon.
     *
     * @param other another PlayablePolygon
     * @return if this polygon is hitting another one
     */
    public boolean isHitting(PlayablePolygon other) {
        for (int i = 0; i < xPoints.length; i++) {
            if (other.polygon.contains((int)xPoints[i], (int)yPoints[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Tests if this polygon is hitting a CircleEntity
     * @param other a CircleEntity in this Screen
     * @return if this and other are colliding
     */
    public boolean isHitting(CircleEntity other) {
        // a collision does not count if a CircleEntity is not active
        if ( !other.isActive() ) return false;

        Area areaA = new Area(polygon);
        areaA.intersect(new Area(other.getEllipse()));

        // if the area of this and other intersect at all
        return !areaA.isEmpty();
    }

    public void reactSides() {
        reactSides(0.8);
    }

    public void reactSides(double bounceFactor) {
        // bounce off bottom
        // if the square is going down or stagnant
        if (yVel > 0) {
            for (int i = 0; i < yPoints.length; i++) {
                // if any point on the polygon is below the bottom of the frame
                if (yPoints[i] >= Screen4.HEIGHT) {
                    yVel *= -bounceFactor;

                    break;
                }
            }
        }

        // bounce off top.
        for (int i = 0; i < yPoints.length; i++) {
            // if any point on polygons[0] is above the top
            if (yPoints[i] <= 0) {
                // if polygons[0] is going up or stagnant
                if (yVel <= 0) {
                    yVel *= -1;
                }
                break;
            }
        }

        // bounce off left.
        for (int i = 0; i < xPoints.length; i++) {
            // if any point on polygons[0] is too far left
            if (xPoints[i] <= 0) {
                // if polygons[0] is going left or stagnant
                if (xVel <= 0) {
                    xVel *= -bounceFactor;
                }
                break;
            }
        }

        // bounce off right.
        for (int i = 0; i < xPoints.length; i++) {
            // if any point on polygons[0] is below the bottom
            if (xPoints[i] >= Screen4.WIDTH) {

                // if polygons[0] is going right or stagnant
                if (xVel >= 0) {
                    xVel *= -bounceFactor;
                }
                break;
            }
        }
    }

    /**
     * Rotate the polygon a given amount of degrees. Only rotates in multiples of π / 24.
     * @param x How many degrees this Polygon will rotate.
     */
    public void rotatePolygon(double x) {
        angle += x;

        updatePolygon();
    }

    /**
     * Updates/resets the Polygon field (typically for a rotation) using angle to affect the xPoints and yPoints
     * and accounting for a change in sides.
     * Also makes sure that with the rotation, the polygon isn't outside of the JFrame
     */
    public void updatePolygon() {
        // update xPoints and yPoints (because of the changes in xVel and yVel that have affected them)
        double theta = Math.PI * 2 / sides;

        for (int i = 0; i < sides; i++) {
            // x-value
            xPoints[i] = center[0] + radius * Math.cos(theta * i + angle);
            // y-value
            yPoints[i] = center[1] + radius * Math.sin(theta * i + angle);

            // make sure the shape isn't inside of a wall, floor, or ceiling.
            if (yPoints[i] > Screen4.HEIGHT) {
                double bumpUp = yPoints[i] - Screen4.HEIGHT;
                for (int a = 0; a < yPoints.length; a++) {
                    yPoints[a] -= bumpUp;
                }
                center[1] -= bumpUp;
            }
            if (xPoints[i] < 0) {
                for (int a = 0; a < xPoints.length; a++) {
                    xPoints[a] -= xPoints[i];
                }
                center[0] -= xPoints[i];
            }
            if (xPoints[i] > Screen4.WIDTH) {
                double bumpLeft = xPoints[i] - Screen4.WIDTH;
                for (int a = 0; a < xPoints.length; a++) {
                    xPoints[a] -= bumpLeft;
                }
                center[0] -= bumpLeft;
            }
        }

        polygon = new Polygon(doublesToInts(xPoints), doublesToInts(yPoints), sides);
    }

    public void hit(PlayablePolygon other) {
        other.radius *= (double)(other.sides - 1) / other.sides;
        other.incrementSides(-1);
    }

    /**
     * Bounce off another polygon
     * @param other
     */
    public void bounceOff(PlayablePolygon other) {
        if (!invincible) {
            yVel *= -1;
            xVel *= -1;

            other.yVel *= -1;
            other.xVel *= -1;
        }
    }

    /**
     * Helper method to convert a double[] to int[]
     * @param arr a double array
     * @return an int array that has casted all elements of a respective int array
     */
    public static int[] doublesToInts(double[] arr) {
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = (int)arr[i];
        }
        return result;
    }

    public boolean isDead() {
        return sides < 3;
    }

    public void setyVel(double yVel) {
        this.yVel = yVel;
    }

    public void setxVel(double xVel) {
        this.xVel = xVel;
    }

    public double getxVel() {
        return xVel;
    }

    public double getyVel() {
        return yVel;
    }

    public double[] getxPoints() {
        return xPoints;
    }

    public double[] getyPoints() {
        return yPoints;
    }

    public void setyPoint(int index, double yPoint) {
        this.yPoints[index] = yPoint;
    }

    public void setxPoint(int index, double xPoint) {
        this.xPoints[index] = xPoint;
    }

    public Color getColor() {
        return color;
    }

    public void incrementDamage(double x) {
        damage += x;
    }

    public double[] getCenter() {
        return center;
    }

    public void setCenter(double[] center) {
        this.center = center;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public int getSides() {
        return sides;
    }

    public double getRadius() {
        return radius;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public double getDamage() {
        return damage;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void incrementSides(int x) {
        sides += x;
        xPoints = new double[sides];
        yPoints = new double[sides];
        damage += x;

        updatePolygon();
    }
}
