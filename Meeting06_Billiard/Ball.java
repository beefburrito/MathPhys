//package Meeting06_Billiard;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Ball {
	private double positionX;              // center of ball's position
	private double positionY;
	public final  double RADIUS = 30;
	private double velocityX = 0;          // ball's velocity
	private double velocityY = 0;
	private final static double e = 1;     // ball's coefficient of resistution
	private final static double a = 0.005; // ball's deceleration/fraction
	private Color ballColor;
	private double mass;

	public Ball(double positionX, double positionY, Color ballColor, double mass) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.ballColor = ballColor;
		this.mass = mass;
	}

	public double getRadius(){
		return RADIUS;
	}

	public double getMass(){
		return mass;
	}

	public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }


	public double getPositionX() {
		return positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public double getVelocityX() {
		return velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	// drawing function
	public void draw(Graphics g) {
		Color tempColor = g.getColor();
		g.setColor(ballColor);
		g.fillOval((int) (positionX - RADIUS), (int) (positionY - RADIUS), (int) (2 * RADIUS), (int) (2 * RADIUS));
		g.setColor(tempColor);
	}

	// move the ball by modifying current position, with assumption that time = 1
	public void move() {
		positionX += velocityX;
		positionY += velocityY;

		double velocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY);

		if (velocity > 0) {
			velocityX -= a;
			velocityY -= a;
		}
	}

	public void ballCollide(ArrayList<Ball> balls) {
        for (Ball b: balls) {
            if (b != this && this.distance(b) <= this.RADIUS + b.getRadius()) {
                //calculate the unit centre vector, that is the vector between the centre of the two balls colliding
                double CVectorX = b.getPositionX() - this.positionX;
                double CVectorY = b.getPositionY() - this.positionY;
                double CVectorLength = Math.sqrt(CVectorX*CVectorX + CVectorY*CVectorY);
                CVectorX = CVectorX/CVectorLength;
                CVectorY = CVectorY/CVectorLength;

                //calculate the normal vector of the centre vector (by rotating 90 degrees)
                double NVectorX = -CVectorY;
                double NVectorY =  CVectorX;
                //projecting the ball's velocity to the centre and normal vector of the two centre balls
                double V1x  = CVectorX * this.velocityX + CVectorY * this.velocityY;
                double V1y  = NVectorX * this.velocityX + NVectorY * this.velocityY;

                double V2x  = CVectorX * b.getVelocityX() + CVectorY * b.getVelocityY();

                double V2y = NVectorX * b.getVelocityX() + NVectorY * b.getVelocityY();

                //calculate the new velocity after collision
                double NewV1x = ((this.mass * V1x) + (b.getMass() * V2x) + b.getMass() * e *(V2x - V1x))/(this.mass + b.getMass());
                double NewV1y = V1y ;

                double NewV2x = ((this.mass * V1x) + (b.getMass() * V2x) - this.mass * e * (V2x - V1x))/(this.mass + b.getMass());
                double NewV2y = V2y ;

                //set the new velocity for the two balls colliding

                this.velocityX = NewV1x * CVectorX + NewV1y * NVectorX;
                this.velocityY = NewV1x * CVectorY + NewV1y * NVectorY;

                b.setVelocityX(NewV2x * CVectorX + NewV2y * NVectorX);
				b.setVelocityY(NewV2x * CVectorY + NewV2y * NVectorY);
				while(this.distance(b) <= (this.RADIUS + b.getRadius()))
                {
                    this.positionX += velocityX;
                    this.positionY -= velocityY;
                    b.setPositionX(b.getPositionX() + b.getVelocityX());
                    b.setPositionX(b.getPositionX() - b.getVelocityY());
                }
            }
        }
	}
	
	 // check collision between walls and the ball
	 public void wallCollide(ArrayList<Wall> walls) {
        for (Wall w : walls) {
            if(w.distanceFromPoint(positionX, positionY) <= RADIUS) {
                double error = RADIUS - w.distanceFromPoint(positionX, positionY);

                positionX += error * w.normalLine().getX();
                positionY -= error * w.normalLine().getY();

                //if the ball collided with a vertical wall
                if(w.getWidth() == 0) {
                    velocityX *= -1;
                }

                //if the ball collided with a horizontal wall
                else if(w.getHeight() == 0) {
                    velocityY *= -1;
                }
            }
        }
    }

	public double distance(Ball other) {
		double distanceX = this.positionX - other.getPositionX();
		double distanceY = this.positionY - other.getPositionY();
		return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
	}
}
