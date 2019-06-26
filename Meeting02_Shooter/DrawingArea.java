//package Meeting02_Shooter;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.*;

class DrawingArea extends JPanel {
    public final static int GRAPH_SCALE = 30;
    private double time = 0;
    private final static double TIME_INCREMENT = 0.05;
    private int width;
    private int height;
    private int originX;        // the origin points (0, 0)
    private int originY;
    private Image drawingArea;
    private Thread animator;    // thread to draw the animation
    private Cannon cannon;
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private Target target;
    // setup the drawing area
    public DrawingArea(int width, int height, int cpSize) {
        super(null);
        this.width = width - cpSize;
        this.height = height;
        setBounds(cpSize, 0, this.width, this.height);
        drawingArea = createImage(this.width, this.height);

        originX = this.width / 4;
        originY = this.height / 2;
        // trigger drawing process
        drawingArea = createImage(this.width, this.height);
        animator = new Thread(this::eventLoop);
    }

    public double getTime() {
        return time;
    }

    public void start() {
        animator.start();
        target = new Target(40, getWidth()/2, 500, 50);
    }

    public void setCannon(Cannon cannon) {
        this.cannon = cannon;
    }

    public void setBullet(Bullet bullet) {
        bullet.shoot();
        bullet.setTime(time);
        bullets.add(bullet);
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    private void eventLoop() {
        drawingArea = createImage(width, height);
        while (true) {
            update();
            render();
            printScreen();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }

    private void update() {
        time += TIME_INCREMENT;
        for(int i=0; i<bullets.size(); i++) {
            if (bullets.get(i) != null && bullets.get(i).isShot()) {
                bullets.get(i).move(time);
                if (bullets.get(i).getPositionY() > getHeight()) {
                    bullets.get(i).stopShoot();
                }
            }
        }
        target.move(time);
        int targetCenterX = target.positionX + (int)target.radius;
        int targetCenterY = target.positionY + (int)target.radius;
        for(int i=0; i<bullets.size(); i++) {
            if(bullets.get(i).getPositionX() + bullets.get(i).getRadius() > targetCenterX - target.radius &&
                bullets.get(i).getPositionX() + bullets.get(i).getRadius() < targetCenterX + target.radius &&
                bullets.get(i).getPositionY() + bullets.get(i).getRadius() > targetCenterY - target.radius &&
                bullets.get(i).getPositionY() + bullets.get(i).getRadius() < targetCenterY + target.radius) {
                    target.changeColor(Color.YELLOW);

                    bullets.remove(i);
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    target.changeColor(Color.BLUE);
                }
        }
    }

    private void render() {
        if (drawingArea != null) {
            //get graphics of the image where coordinate and function will be drawn
            Graphics g = drawingArea.getGraphics();

            // clear screen
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.black);
            //draw the x-axis and y-axis
            g.drawLine(originX+150, 0, originX+150, getHeight());

            // draw cannon and bullet
            cannon.draw(g);
            for(int i=0; i<bullets.size(); i++) {
                if (bullets.get(i) != null && bullets.get(i).isShot()) {
                    bullets.get(i).draw(g);
                }
            }
            target.draw(g);
        }
    }

    private void printScreen()
    {
        try
        {
            Graphics g = getGraphics();
            if(drawingArea != null && g != null)
            {
                g.drawImage(drawingArea, 0, 0, null);
            }

            // Sync the display on some systems.
            // (on Linux, this fixes event queue problems)
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        }
        catch(Exception ex)
        {
            System.out.print("Graphics error: ");
            ex.printStackTrace();
        }
    }
}
