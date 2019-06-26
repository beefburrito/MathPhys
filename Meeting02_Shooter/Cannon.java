//package Meeting02_Shooter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

class Cannon {
    // appearance
    private final static Color COLOR = Color.red;
    private double radius;
    private double barrelLength;
    private double barrelWidth;

    // location and angle
    private int positionX;
    private int positionY;
    private int centerX;
    private int centerY;
    private double angle = Math.PI/4;
    private final static double ANGLE_INCREMENT = 0.2;
    private final static int DISTANCE_INCREMENT = 3;

    // initialize and draw
    public Cannon(double radius, int positionX, int positionY) {
        this.radius = radius;
        this.positionX = positionX;
        this.positionY = positionY;
        centerX = (int) (positionX + radius);
        centerY = (int) (positionY + radius);

        barrelLength = radius * 1.5;
        barrelWidth = radius/2;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Color tempColor = g2.getColor();
        g2.setColor(COLOR);

        // draw circle
        int size = (int) (radius * 2);
        g.fillOval(positionX, positionY, size, size);

        // draw barrel
        g2.setStroke(new BasicStroke((int) barrelWidth));
        g2.drawLine(centerX, centerY, (int) getBarrelMouthX(), (int) getBarrelMouthY());

        g2.setColor(tempColor);
    }

    public int getPositionX() {
        return positionX;
    }

    public double getBarrelWidth() {
        return barrelWidth;
    }

    public double getAngle() {
        return angle;
    }

    // get barrel mouth's position
    public double getBarrelMouthX() {
        return centerX + barrelLength * Math.cos(angle);
    }

    // minus, because the difference in coordinate system
    public double getBarrelMouthY() {
        return centerY - barrelLength * Math.sin(angle);
    }

    // movement methods
    public void rotateLeft() {
        angle += ANGLE_INCREMENT;
    }
    
    public void rotateRight() {
        angle -= ANGLE_INCREMENT;
    }

    public void moveUp() {
        positionY -= DISTANCE_INCREMENT;
        centerY -= DISTANCE_INCREMENT;
    }

    public void moveDown() {
        positionY += DISTANCE_INCREMENT;
        centerY += DISTANCE_INCREMENT;
    }

    public void moveLeft() {
        positionX -= DISTANCE_INCREMENT;
        centerX -= DISTANCE_INCREMENT;
    }

    public void moveRight() {
        positionX += DISTANCE_INCREMENT;
        centerX += DISTANCE_INCREMENT;
    }
}
