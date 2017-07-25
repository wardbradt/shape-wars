/**
 * Created by wardbradt on 5/17/17.
 */
import javax.swing.*;

public class Game4 {
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        Screen4 scr = new Screen4(frame);
        scr.setFocusable(true);
        frame.add(scr);

        //makes closing the window exit the program
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(scr.WIDTH + frame.getInsets().right + frame.getInsets().left,
                scr.HEIGHT + frame.getInsets().top + frame.getInsets().bottom);
        frame.setResizable(false);

        //setup for Game3 loop
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS; //in nano seconds

        while (true) {
            //calculate timing variables
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;

            scr.requestFocusInWindow();  //lets our Screen3 receive mouse events
            scr.update(updateLength);

            //tells the frame to call Screen3's paint method
            frame.repaint();

            //makes our program wait until its time for the next frame
            //NOTE: sleep is not exact so frames will not be exactly OPTIMAL_TIME apart
            try{
                Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
            }
            catch (Exception e) {
            }
        }
    }
}