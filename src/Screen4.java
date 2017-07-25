import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Ward Bradt
 * June 3, 2017
 *
 * Used information from:
 * // https://stackoverflow.com/questions/223918/iterating-through-a-collection-avoiding-concurrentmodificationexception-when-re
 * // https://stackoverflow.com/questions/258486/calculate-the-display-width-of-a-string-in-java
 *
 * A simple game made in Java primarily using JPanel and awt.polygon
 */
public class Screen4 extends JPanel implements MouseListener{
    // 0 is blue, 1 is red.
    private PlayablePolygon[] polygons;
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    private int gravity;
    private ArrayList<CircleEntity> specials;
    private JFrame frame;
    private double increaseTimer;
    private double invincibilityTimer;
    private String gameState;

    private static final String LEFT_RELEASE = "Left Released";
    private Action leftRelease = new AbstractAction(LEFT_RELEASE) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameState.equals("play")) {
                polygons[0].setxVel(polygons[0].getxVel() - 100);
                polygons[0].setyVel(polygons[0].getyVel() - 100);
            }
        }
    };

    private static final String UP_RELEASE = "Up Released";
    private Action upRelease = new AbstractAction(UP_RELEASE) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameState.equals("play"))
                polygons[0].rotatePolygon(Math.PI / 12);
        }
    };

    private static final String DOWN_RELEASE = "Down Released";
    private Action downRelease = new AbstractAction(DOWN_RELEASE) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameState.equals("play"))
                polygons[0].rotatePolygon(-Math.PI / 12);
        }
    };

    private static final String RIGHT_RELEASE = "Right Released";
    private Action rightRelease = new AbstractAction(RIGHT_RELEASE) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameState.equals("play")) {
                polygons[0].setxVel(polygons[0].getxVel() + 100);
                polygons[0].setyVel(polygons[0].getyVel() - 100);
            }
        }
    };

    private static final String W_RELEASE = "W Released";
    private Action wRelease = new AbstractAction(W_RELEASE) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameState.equals("play"))
                polygons[1].rotatePolygon(Math.PI / 12);
        }
    };

    private static final String A_RELEASE = "A Released";
    private Action aRelease = new AbstractAction(A_RELEASE) {
        @Override
        public void actionPerformed(ActionEvent e) {
            // red square goes up left
            if (gameState.equals("play")) {
                polygons[1].setxVel(polygons[1].getxVel() - 100);
                polygons[1].setyVel(polygons[1].getyVel() - 100);
            }
        }
    };

    private static final String D_RELEASE = "D Released";
    private Action dRelease = new AbstractAction(D_RELEASE) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameState.equals("play")) {
                polygons[1].setxVel(polygons[1].getxVel() + 100);
                polygons[1].setyVel(polygons[1].getyVel() - 100);
            }
        }
    };

    private static final String S_RELEASE = "S Released";
    private Action sRelease = new AbstractAction(S_RELEASE) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameState.equals("play"))
                polygons[1].rotatePolygon(-Math.PI / 12);
        }
    };

    private static final String G_RELEASE = "Game Over Override";
    private Action gRelease = new AbstractAction(LEFT_RELEASE) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameState.equals("over")) {
                menuPolygons = initializeMenuPolygons(10, 180);
                frame.setBackground(Color.BLACK);
                gameState = "menu";
            }
        }
    };

    public Screen4(JFrame frame) {
        frame.setBackground(Color.black);
        specials = new ArrayList<CircleEntity>();
        gameState = "menu";
        this.frame = frame;
        polygons = new PlayablePolygon[2];
        gravity = 150;
        increaseTimer = 0;
        invincibilityTimer = 0;

        addMouseListener(this);  //registers this object to recieve mouse events

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), LEFT_RELEASE);
        getActionMap().put(LEFT_RELEASE, leftRelease);

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), RIGHT_RELEASE);
        getActionMap().put(RIGHT_RELEASE, rightRelease);

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), DOWN_RELEASE);
        getActionMap().put(DOWN_RELEASE, downRelease);

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), UP_RELEASE);
        getActionMap().put(UP_RELEASE, upRelease);

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), W_RELEASE);
        getActionMap().put(W_RELEASE, wRelease);

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), A_RELEASE);
        getActionMap().put(A_RELEASE, aRelease);

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), D_RELEASE);
        getActionMap().put(D_RELEASE, dRelease);

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), S_RELEASE);
        getActionMap().put(S_RELEASE, sRelease);

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0, false), G_RELEASE);
        getActionMap().put(G_RELEASE, gRelease);
    }

    // dt is number of nanoseconds since the last time update was called
    // update the location of the polygon based on changes in xvel, yvel, and gravity w/ time passed.
    public void update(long dt) {
        if (gameState.equals("play")) {
            //converts dt into seconds
            double dtS = dt / 1000000000.0;
            increaseTimer += dtS;
            if (polygons[0].isInvincible() || polygons[0].isInvincible()) {
                invincibilityTimer += dtS;
                polygons[0].setColor(new Color(160 - (int)(70 * invincibilityTimer), (int)(160 - 70 * invincibilityTimer), 255));
                polygons[1].setColor(new Color(255, 160 - (int)(70 * invincibilityTimer), (int)(160 - 70 * invincibilityTimer)));
            }

            if (invincibilityTimer > 2) {
                invincibilityTimer = 0;
                polygons[0].setColor(Color.BLUE);
                polygons[1].setColor(Color.RED);
                setInvincibilities(false);
            }

            // every 2 to 5 seconds, one or more random CircleEntity-s spawn at random locations for 4 seconds.
            double randomIncrease = (int) (Math.random() * 3) + 7;
            if (increaseTimer > randomIncrease) {
                for (int i = 0; i < increaseTimer / randomIncrease; i++) {
                    if (polygons[0].getClass().equals(DamagedPolygon.class)) {
                        specials.add(new HealthCircle(Math.random() * (WIDTH * 0.8) + WIDTH * 0.1,
                                Math.random() * (HEIGHT * 0.8) + HEIGHT * 0.1));
                    }
                    else {
                        specials.add(new SideIncreaser(Math.random() * (WIDTH * 0.8) + WIDTH * 0.1,
                                Math.random() * (HEIGHT * 0.8) + HEIGHT * 0.1));
                    }
                }
                increaseTimer %= randomIncrease;
            }

            for (int s = 0; s < polygons.length; s++) {
                // react to polygons hitting circle entities and turn on/ off circle entities.
                for (Iterator<CircleEntity> iterator = specials.iterator(); iterator.hasNext(); ) {
                    CircleEntity next = iterator.next();

                    next.incrementTimeLived(dtS);
                    if (next.getTimeLived() > next.getLifetime()) {
                        // Remove the current element from the iterator and the list.
                        iterator.remove();
                    }

                    // turn on after 1.5 seconds
                    else if (next.getTimeLived() > 2.5) {
                        next.turnOn();
                    }

                    // isHitting will make this polygon bigger if it is true
                    if (polygons[s].isHitting(next)) {
                        next.reactHit(polygons[s]);
                        iterator.remove();
                    }
                }

                if (polygons[s].isHitting(polygons[(s + 1) % 2])) {
                    polygons[0].bounceOff(polygons[1]);
                    if (!polygons[(s+1) % 2].isInvincible()) polygons[s].hit(polygons[(s + 1) % 2]);
                    setInvincibilities(true);
                }

                // if a polygon has reached criteria for death after being hit, game is over
                if (polygons[(s + 1) % 2].isDead()) {
                    gameState = "over";
                    break;
                }

                if (Math.abs(polygons[s].getxVel()) > 400) {
                    polygons[s].setxVel(Math.signum(polygons[s].getxVel()) * 400);
                }

                if (Math.abs(polygons[s].getyVel()) > 400) {
                    polygons[s].setyVel(Math.signum(polygons[s].getyVel()) * 400);
                }

                // gravity: affect the current y velocity by gravity * passed seconds.
                double yVel = polygons[s].getyVel() + dtS * gravity;
                polygons[s].setyVel(yVel);

                double xVel = polygons[s].getxVel();

                xVel *= 0.999;

                polygons[s].setxVel(xVel);

                // bounce off sides
                polygons[s].reactSides();

                // need this for collisions.
                for (int i = 0; i < polygons[s].getSides(); i++) {
                    polygons[s].setxPoint(i, polygons[s].getxPoints()[i] + (int) (dtS * polygons[s].getxVel()));
                    polygons[s].setyPoint(i, polygons[s].getyPoints()[i] + (int) (dtS * polygons[s].getyVel()));
                }

                double[] newCenter = new double[2];
                newCenter[0] = polygons[s].getCenter()[0] + (int) (dtS * polygons[s].getxVel());
                newCenter[1] = polygons[s].getCenter()[1] + (int) (dtS * polygons[s].getyVel());

                polygons[s].setCenter(newCenter);

                polygons[s].updatePolygon();
            }
        }
    }

    private void setInvincibilities(boolean bool) {
        for (int i = 0; i < polygons.length; i++) {
            polygons[i].setInvincible(bool);
        }
    }

    public void paint(Graphics g) {
        switch(gameState) {
            case "menu" :
                paintMenu(g);
                break;
            case "help" :
                paintHelp(g);
                break;
            case "play" :
                paintGame(g);
                break;
            case "over" :
                paintGameOver(g);
                break;
        }
    }

    private void paintGame(Graphics g) {
        if (invincibilityTimer <= 3 && invincibilityTimer > 0) {
            int fontWidth = g.getFontMetrics().stringWidth("HIT!");
            g.drawString("HIT!", WIDTH / 2 - (fontWidth / 2), HEIGHT / 2);
        }


        for (int s = 0; s < polygons.length; s++) {
            g.setColor(polygons[s].getColor());

            g.fillPolygon(polygons[s].getPolygon());

            if (polygons[s].getClass().equals(DamagedPolygon.class)) paintScores(g, s);
            if (invincibilityTimer <= 3 && invincibilityTimer > 0)
                paintInvincibilityTimer(g);
        }

        for (int s = 0; s < specials.size(); s++) {
            CircleEntity iter = specials.get(0);
            g.setColor(iter.getColor());
            if (iter.isActive())
                g.setColor(iter.getColor());
            else
                g.setColor(Color.GRAY);
            g.fillOval((int)iter.getCenter()[0], (int)iter.getCenter()[1], (int)iter.getDiameter(), (int)iter.getDiameter());
        }
    }

    private void paintInvincibilityTimer(Graphics g) {
        g.setColor(Color.BLACK);
        String time = Integer.toString( 2 - (int)invincibilityTimer);
        int fontWidth = g.getFontMetrics().stringWidth(time);
        int fontHeight = g.getFontMetrics().getHeight();
        g.drawString(time, WIDTH / 2 - fontWidth, HEIGHT / 2 - fontHeight);
    }

    /**
     * Paints the scores for the two PlayablePolygon-s. Helper method for paintGame(Graphics g).
     *
     * @param g Graphics object
     * @param index the index of this polygon in polygons[]
     */
    private void paintScores(Graphics g, int index) {
        int barHeight = 30;
        g.setColor( new Color(0,0,255,70));
        g.drawRect(0,0, WIDTH / 2, barHeight);
        g.fillRect((WIDTH / 2) - (int)((polygons[0].getDamage() / 100) * (WIDTH / 2)), 0,
                (int)((polygons[0].getDamage() / 100) * (WIDTH / 2)), barHeight);

        g.setColor( new Color(255,0,0,70));
        g.drawRect(WIDTH/2,0, WIDTH / 2, barHeight);
        g.fillRect(WIDTH/2, 0, (int)((polygons[1].getDamage() / 100) * (WIDTH / 2)), barHeight);

        // the damage of this polygon to two decimal places
        String score = Double.toString(polygons[index].getDamage());
        if (score.indexOf(".") > -1) {
            if (score.indexOf(".") + 2 < score.length())
                score = score.substring(0, score.indexOf(".") + 2);
        }
        score = score.length() > 7 ? score.substring(0,6) : score;

        int fontWidth = g.getFontMetrics().stringWidth(score);
        g.setColor(polygons[index].getColor());

        if (index == 0) {
            g.drawString(score, 5,frame.getInsets().top);
        }
        else {
            g.drawString(score, WIDTH - fontWidth,frame.getInsets().top);
        }
    }

    private PlayablePolygon[] menuPolygons = initializeMenuPolygons(10, 180);

    /**
     * Helper method for paintMenu(g)
     * @param initSides how many sides the largest shape on the menu will have. either 3 or 10.
     * @return
     */
    private PlayablePolygon[] initializeMenuPolygons(int initSides, double initRadius) {
        return initializeMenuPolygons(initSides, initRadius, WIDTH /2 , HEIGHT / 2);
    }

    private PlayablePolygon[] initializeMenuPolygons(int initSides, double initRadius, double x, double y) {
        PlayablePolygon[] result = new PlayablePolygon[8];
        switch (initSides) {
            // makes the largest then smallest shapes.
            case 10 :
                int sides = 10;
                double angle = 0;
                for (int i = 10; i >=3; i--) {
                    // Alternate between blue and red
                    Color col = i % 2 == 0 ? Color.RED : Color.BLUE;

                    result[10 - i] = new PlayablePolygon((int)x, (int)y, col, sides, initRadius, angle);

                    // set the radius equal to the previous apothem
                    initRadius = .98 * initRadius * Math.cos(Math.PI / sides);
                    // set angle to make bottom flat
                    double shapeTheta = Math.PI * 2 / (sides - 1);
                    angle = (Math.PI - shapeTheta) / 2;

                    sides--;
                }
                return result;
            case 3:
                sides = 3;
                angle = 0;
                for (int i = 3; i <=10; i++) {
                    // Alternate between blue and red
                    Color col = i % 2 == 0 ? Color.RED : Color.BLUE;

                    result[i-3] = new PlayablePolygon((int)x, (int)y, col, sides, initRadius, angle);

                    // set the radius equal to the previous apothem
                    initRadius = initRadius * Math.cos(Math.PI / sides);
                    // set angle to make bottom flat
                    double shapeTheta = Math.PI * 2 / (sides - 1);
                    angle = (Math.PI - shapeTheta) / 2;

                    sides++;
                }
                return result;
            default:
                return initializeMenuPolygons(10, 180);
        }
    }

    private void paintMenuPolygons(Graphics g) {
        Graphics2D g2D = (Graphics2D)g;

        g.setFont(new Font("Monaco", Font.PLAIN, 10));
        int fontHeight = g.getFontMetrics().getHeight();
        Rectangle leftHalf = new Rectangle(WIDTH/2, HEIGHT);

        for (int i = 0; i < menuPolygons.length; i++) {
            if (i == menuPolygons.length - 1) menuPolygons[i].rotatePolygon(Math.PI / 64);
            g.setColor(menuPolygons[i].getColor());
            g.fillPolygon(menuPolygons[i].getPolygon());

            Area areaA = new Area(menuPolygons[i].getPolygon());
            areaA.intersect(new Area(leftHalf));
            if (g.getColor().equals(Color.RED)) {
                g.setColor(Color.BLUE);
            }
            else {
                g.setColor(Color.RED);
            }
            g2D.fill(areaA);

            int fontWidth = g.getFontMetrics().stringWidth(Integer.toString(10-i));
            g.setColor(menuPolygons[i].getColor());

            // draw letters
            g.setColor(Color.WHITE);
            if (i < menuPolygons.length - 1) {
                int y = (int)menuPolygons[i].getCenter()[1] + (int) (menuPolygons[i+1].getRadius() * Math.cos(Math.PI / (10 - i))) + (fontHeight / 2);
                g.drawString(Integer.toString(10 - i), (int)menuPolygons[i].getCenter()[0] - (fontWidth / 2), y);
            }
            else {
                g.drawString(Integer.toString(10 - i), WIDTH / 2 - (fontWidth / 2), HEIGHT / 2 + (fontHeight / 2));
            }
        }
    }

    private void paintMenu(Graphics g) {
        paintMenuPolygons(g);

        // play game text
        g.setFont(new Font("Stencil", Font.PLAIN, 30));
        g.setColor(Color.WHITE);
        g.drawString("?", WIDTH/8 - 40, HEIGHT / 8 - 20);
    }

    public void paintGameOver(Graphics g) {
        g.setFont(new Font("Monaco", Font.PLAIN, 60));
        int fontWidth = g.getFontMetrics().stringWidth("Game Over!");
        int fontHeight = g.getFontMetrics().getHeight();

        g.drawString("Game Over!", WIDTH / 2 - (fontWidth / 2), HEIGHT / 4 - (fontHeight / 2));

        String winnerString = "";
        if (polygons[0].isDead()) {
            g.setColor(polygons[1].getColor());
            winnerString = "Red Wins!";
        }
        else {
            g.setColor(polygons[0].getColor());
            winnerString = "Blue Wins!";
        }

        fontWidth = g.getFontMetrics().stringWidth(winnerString);
        g.drawString(winnerString, WIDTH / 2 - (fontWidth / 2), HEIGHT / 2);

        g.setFont(new Font("Monaco", Font.PLAIN, 20));
        g.setColor(Color.BLACK);
        fontWidth = g.getFontMetrics().stringWidth("Press 'G' to play again.");

        g.drawString("Press 'G' to play again.", WIDTH / 2 - (fontWidth / 2), HEIGHT / 4 * 3 + (fontHeight / 2));
    }

    public void paintHelp(Graphics g) {
        g.setColor(Color.WHITE);
        int fontWidth = g.getFontMetrics().stringWidth("Back");
        int fontHeight = g.getFontMetrics().getHeight();
        g.drawString("Back", WIDTH / 8 - fontWidth - 20, HEIGHT / 8 - 40);

        fontWidth = g.getFontMetrics().stringWidth("1. Use W and S or UP and DOWN to rotate your polygon.");
        g.drawString("1. Use W and S or UP and DOWN to rotate your polygon.", WIDTH / 2 - (fontWidth / 2), HEIGHT / 8 - 15);
        fontWidth = g.getFontMetrics().stringWidth("2. Use A and D or LEFT and RIGHT to jump to the left and right 45 degrees.");
        g.drawString("2. Use A and D or LEFT and RIGHT to jump to the left and right 45 degrees.", WIDTH / 2 - (fontWidth / 2), HEIGHT / 8 + fontHeight - 15);
        fontWidth = g.getFontMetrics().stringWidth("3. To hurt another player, your vertex must collide with their side.");
        g.drawString("3. To hurt another player, your vertex must collide with their side.", WIDTH / 2 - (fontWidth / 2), HEIGHT / 8 + fontHeight * 2 - 15);
        fontWidth = g.getFontMetrics().stringWidth("Red and Green Orbs help you win! Collect them before they disappear!");
        g.drawString("Red and Green Orbs help you win! Collect them before they disappear!", WIDTH / 2 - (fontWidth / 2), HEIGHT / 8 + fontHeight * 3 - 15);

        fontWidth = g.getFontMetrics().stringWidth("How To Start:");
        g.drawString("How To Start:", WIDTH / 2 - (fontWidth / 2), HEIGHT / 2 + 140 + 10);
        fontWidth = g.getFontMetrics().stringWidth("1. To play Damage Battalion, click on the left side.");
        g.drawString("1. To play Damage Battalion, click on the left side.", WIDTH / 2 - (fontWidth / 2), HEIGHT / 2 + 140 + 10 + fontHeight);

        fontWidth = g.getFontMetrics().stringWidth("1b. To play Side Mania, click on the right.");
        g.drawString("1b. To play Side Mania, click on the right.", WIDTH / 2 - (fontWidth / 2), HEIGHT / 2 + 140 + 10 + fontHeight * 2);

        fontWidth = g.getFontMetrics().stringWidth("2. Click on the shape with the amount of sides you would like to start with.");
        g.drawString("2. Click on the shape with the amount of sides you would like to start with.", WIDTH / 2 - (fontWidth / 2), HEIGHT / 2 + 140 + 10 + fontHeight * 3);
        fontWidth = g.getFontMetrics().stringWidth("2b. It is recommended to begin with 3 or 4 sides.");
        g.drawString("2b. It is recommended to begin with 3 or 4 sides.", WIDTH / 2 - (fontWidth / 2), HEIGHT / 2 + 140 + 10 + fontHeight * 4);

        paintMenuPolygons(g);
    }

    public void clickMenuPolygon(int xClick, int yClick) {
        for (int i = menuPolygons.length - 1; i >= 0; i--) {
            if (menuPolygons[i].getPolygon().contains(xClick, yClick)) {
                int radius = 50;
                if (xClick <= WIDTH / 2) {
                    polygons[0] = new DamagedPolygon((WIDTH + frame.getInsets().left) / 4 * 3,
                            HEIGHT + frame.getInsets().top - radius, Color.BLUE, menuPolygons[i].getSides(), radius);
                    polygons[1] = new DamagedPolygon((WIDTH + frame.getInsets().left) / 4,
                            HEIGHT + frame.getInsets().top - radius, Color.RED, menuPolygons[i].getSides(), radius);
                }
                else {
                    polygons[0] = new PlayablePolygon((WIDTH + frame.getInsets().left) / 4 * 3,
                            HEIGHT + frame.getInsets().top - radius, Color.BLUE, menuPolygons[i].getSides(), radius);
                    polygons[1] = new PlayablePolygon((WIDTH + frame.getInsets().left) / 4,
                            HEIGHT + frame.getInsets().top - radius, Color.RED, menuPolygons[i].getSides(), radius);
                }
                gameState = "play";
                frame.setBackground(Color.WHITE);
                // start a game with this many sides.
                break;
            }
        }
    }

    public void mouseClicked(MouseEvent e){
        // playButtonWidth = 100 & height = 50
        // if within the boundaries of the play button and on the menu screen
        int xClick = e.getX();
        int yClick = e.getY();

        if (gameState.equals("menu")) {
            if (xClick < WIDTH / 8 - 10 && yClick < HEIGHT / 8 + 10) {
                gameState = "help";
                menuPolygons = initializeMenuPolygons(10, 140, WIDTH / 2, HEIGHT / 2 - 10);
                return;
            }
            clickMenuPolygon(xClick, yClick);
        }

        if (gameState.equals("help")) {
            if (xClick < WIDTH / 8 && yClick < HEIGHT / 8) {
                gameState = "menu";
                frame.setBackground(Color.BLACK);
                menuPolygons = initializeMenuPolygons(10, 180);
                return;
            }
            clickMenuPolygon(xClick, yClick);
        }
    }

    public void mouseEntered(MouseEvent e){}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e){}
}
