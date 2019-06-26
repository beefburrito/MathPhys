//package Meeting03_Dribble;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Ball {
    private double positionX;                   // center of ball's position
    private double positionY;
    private double radius;
    private double velocityX;                   // ball's velocity
    private double velocityY;
    private Color ballColor;
    private double e = 0.5;        // ball's coefficient of resistution
    private double GRAVITY = 0.5;  // use custom gravity

    public Ball(double positionX, double positionY, double radius, double velocityX, double velocityY, Color ballColor, double elasticity) {
        this.radius = radius;
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.ballColor = ballColor;
        this.e = elasticity;
    }
    // Getters
    public double getX() {
        return positionX;
    }
    public double getY() {
        return positionY;
    }
    public double getRadius() {
        return radius;
    }
    public double getE() {
        return e;
    }
    public void toggleGravity() {
        if(GRAVITY != 0) GRAVITY = 0;
        else GRAVITY = 0.5;
    }
    // drawing function
    public void draw(Graphics g) {
        g.setColor(ballColor);
        g.fillOval((int) (positionX - radius), (int) (positionY - radius), (int) (2 * radius), (int) (2 * radius));
    }

    // move the ball by modifying current position, with assumption that time = 1
    public void move() {
        positionX += velocityX;
        positionY -= (velocityY + GRAVITY/2);
        velocityY -= GRAVITY;
    }

    // check collision between walls and the ball
    public void detectCollision(ArrayList<Wall> walls) {
        for (Wall w : walls) {
            if(w.distanceFromPoint(positionX, positionY) <= radius) {
                double error = radius - w.distanceFromPoint(positionX, positionY);

                positionX += error * w.normalLine().getX();
                positionY -= error * w.normalLine().getY();

                //if the ball collided with a vertical wall
                if(w.getWidth() == 0) {
                    velocityX = -e * velocityX;
                }

                //if the ball collided with a horizontal wall
                else if(w.getHeight() == 0) {
                    velocityY = -e * velocityY;
                }
                // if the wall is slanted/diagonal
                else {
                    Vector normal = w.normalLine(); // Get the wall's normal vector
                    double c = velocityX*normal.getX() + velocityY*normal.getY();
                    Vector proj = new Vector(c*normal.getX(), c*normal.getY()); 
                    velocityX = 2*proj.getX() - velocityX; // V' = 2*proj - v
                    velocityY = 2*proj.getY() - velocityY;
                }
            }
        }
    }
}
