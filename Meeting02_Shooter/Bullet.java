//package Meeting02_Shooter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

class Bullet {
    private int originX;
    private int originY;
    private int positionX;
    private int positionY;
    private double radius;
    private double velocityX;
    private double velocityY;
    private final static double GRAVITY = 9.8;
    private double timeInitial;
    private boolean shot = false;
    private final static Color COLOR = Color.darkGray;
    private int mass;
    private int wind;
    private int angle;

    public Bullet(double radius, int originX, int originY, double angle, double BASE_VELOCITY, double time, int mass, int wind, int windAngle) {
        this.radius = radius;
        this.originX = originX;
        this.originY = originY;
        this.velocityX = BASE_VELOCITY * Math.cos(angle);
        this.velocityY = BASE_VELOCITY * Math.sin(angle);
        this.timeInitial = time;
        this.mass = mass;
        this.wind = wind;
        this.angle = windAngle;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setTime(double time) {
        timeInitial = time;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void shoot() {
        shot = true;
    }

    public void stopShoot() {
        shot = false;
    }

    public boolean isShot() {
        return shot;
    }
    /**
     counting distance based on time
     in x-axis, the velocity is constant
     in y-axis, the velocity is influenced by gravitational acceleration
     and we decrease the y-displacement because of different coordinate system
     */
    public void move(double time) {
        double currentTime = time - timeInitial;

        velocityX += wind * Math.cos(Math.toRadians(angle)) / mass;
        velocityY += wind * Math.sin(Math.toRadians(angle)) / mass;

        positionX = (int) (originX + (velocityX * currentTime));
        positionY = (int) (originY - (velocityY * currentTime - GRAVITY * currentTime * currentTime / 2));
    }

    // drawing function
    public void draw(Graphics g) {
        int size = (int) (radius * 2);
        Graphics2D g2 = (Graphics2D) g;
        Color tempColor = g.getColor();
        g2.setColor(COLOR);

        // draw the bullet
        g2.fillOval((int) (positionX + radius), (int) (positionY - radius), size, size);
        g2.setColor(tempColor);
    }
}
