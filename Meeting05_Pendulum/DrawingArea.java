//package Meeting05_Pendulum;


import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DrawingArea extends JPanel {
        private Thread animator;
		//the ball to be drawn
		ArrayList<Ball> balls = new ArrayList<>();
		// the line to be drawn
		ArrayList<Rope> ropes = new ArrayList<>();
		//Image where functions are drawn, which then drawn to the canvas
		BufferedImage dbImage;	
        //Variables
        private boolean press;
		private int canvasHeight;
        private int canvasStartY;
        private double globalTime = 0;
        private double timeSpeed = 1.0;
        private int score = 0;
		private Ball selectedBall;
		private Rope selectedRope;
		private boolean isPressed;
		private double mousePressedX;
		private double mousePressedY;
        private double time;
		private int maxBalls = 5;
		private double ropeLength = 400;
		private double ballSize = 40;
		private double firstRopeX = 600;
		private double incX = 0.1;
		public static final double GRAVITY = 0.098;

    public DrawingArea(int width, int height) {
        setBounds(0, 0, width, height);
        canvasHeight = getHeight() - getInsets().top;
        canvasStartY = getInsets().top;	
        //create the pendulum
        for(int i=0; i<maxBalls; i++)
            addPendulum();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                //get the coordinate where the mouse is clicked
                mousePressedX = (double)(e.getX());
                mousePressedY = (double)(e.getY());

                //get the ball at that coordinate
                for(Ball b: balls)
                    if(b.isInside(mousePressedX, mousePressedY))
                    {
                        selectedBall = b;
                        selectedRope = b.getRope();
                    }

                //get which ball is pressed
                if(selectedBall != null)
                {
                    isPressed = true;
                    selectedBall.setVx(0);
                    selectedBall.setVy(0);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                //indicating that the mouse is not pressed anymore
                isPressed = false;
                selectedBall = null;
                selectedRope = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                //get the coordinate where the mouse is located
                int mouseMovedX = e.getX();
                int mouseMovedY = e.getY();

                if(selectedBall != null)
                {
                    Vector v = new Vector(mouseMovedX - selectedRope.getX1(), mouseMovedY - selectedRope.getY1());
                    v = v.unitVector();
                    v.multiply(selectedRope.getLength());

                    if((selectedRope.getY1() + v.getY() - selectedBall.getRadius()) > 0)
                    {
                        selectedBall.move(selectedRope.getX1() + v.getX(), selectedRope.getY1() + v.getY());
                    }
                }
            }
        });
        
        //start the thread to draw functions to canvas
        animator = new Thread(this::EventLoop);
    }
	public void addPendulum()
    {
        if(balls.size() < maxBalls)
        {				
            //create the ropes
            ropes.add(new Rope(firstRopeX + 2*balls.size()*(ballSize + incX), 0,
                    firstRopeX + 2*balls.size()*(ballSize + incX), ropeLength, Color.blue));
            //create the balls
            balls.add(new Ball(0, 0, ballSize, 0, 0, Color.RED, 4));
            //attach the ball to the rope
            ropes.get(ropes.size()-1).attach(balls.get(balls.size()-1));				
        }
    }
    public void start() {
        animator.start();
    }

    public boolean isPressed() {
        return press;
    }

    public void setPress(boolean press) {
        this.press = press;
        if (!press) {
            time = 0;
        }
    }

    public double getTime() {
        return time;
    }

    public void EventLoop() {
        dbImage = (BufferedImage)createImage(getWidth(), getHeight());
        while(true)
        {
            globalTime += timeSpeed;
            update();
            render();
            printScreen();
        }		
    }
    
    public void update()
    {
        //update the rope if no mouse is pressed
        if(!isPressed)
        {
            for(Ball b: balls)
            {					
                b.move();                
                for(Ball b2: balls) {
                    if(!b.equals(b2)) { // Check if it is not itself
                        double rsum = b.getRadius() + b2.getRadius();
                        double distance = Math.sqrt(Math.pow(b.getPositionX() - b2.getPositionX(), 2)  + Math.pow(b.getPositionY() - b2.getPositionY(), 2));
                        if(distance <= rsum) { // Check if the ball collides
                            // X Velocity Transfer
                            double v1 = b.getVx();
                            double v2 = b2.getVx();
                            b.setVx(v2);
                            b2.setVx(v1);
                        }
                    }
                }
            }
        }
    }
    
    public void render()
    {
        if(dbImage != null)
        {				
        //get graphics of the image where coordinate and function will be drawn
            Graphics g = dbImage.getGraphics();
        
            //clear screen
            g.setColor(new Color(200,200,150));
            g.fillRect(0, 0, getWidth(), canvasHeight);

            //draw the balls
            for(Ball b: balls)
            {
                if(b != null)
                      b.draw(g);
            }
            
            //draw the ropes				
            for(Rope r: ropes)
            {				
                r.draw(g);
            }
        }
    }
    
    public void printScreen()
    {
        try
        {
            Graphics g = getGraphics();
            if(dbImage != null && g != null)
            {
                g.drawImage(dbImage, 0, canvasStartY, null);
            }
        
            // Sync the display on some systems.
            // (on Linux, this fixes event queue problems)
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        } catch(Exception ex)
        {
            System.out.println("Graphics error: " + ex);  
        }		
    }
}
//
