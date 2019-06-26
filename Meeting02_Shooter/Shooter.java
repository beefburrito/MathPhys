//package Meeting02_Shooter;


import javax.swing.*;
import javax.swing.JTextArea;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.*;

/*
    MatFis pertemuan 2
    Note that every dimension-related are measured in pixel
    Except for angle, which is measured in radian
    Explain how parabolic motion of projectile works.
    >> Motion in x and y coordinates exist, Y- coordinate has free fall motion while
    X-coordinate usually has constant velocity 
    uses the equation s(t) = u t + 1/2 a t ^ 2 and v = u + at
    What is the difference between mapping code in Cartesian coordinates and pixel coordinates?

    TODO:
     1. Add a text field to adjust bullet's velocity
     2. Make cannon able to shoot more than one bullet
     3. Limit the amount of bullet in the cannon
     4. Add wind force, with its direction (which impacts acceleration on x-axis and y-axis; use Newton's second law)
     5. Make a shooter game with simple moving target (yes, over-achievers, I need SIMPLE)

    Extra:
    Q: Does this mean I can make a bullet hell game for my final project?
    A: Yes, but since the concept is already explained in class, you won't get Liv's extra brownie point.
 */


class Shooter {
    private static JFrame frame;

    // game area
    private Bullet bullet = null;
    private Cannon cannon;

    // bullet magazine system
    private int maxBullets = 10;
    public static int currentBullets = 10;
    private String bulletString = "Bullets: " + Integer.toString(currentBullets) + "/" + Integer.toString(maxBullets);

    // physics attributes
    public static int mass = 1;
    public static int wind = 10;
    public static int angle = 0;


    private static final String INSTRUCTION = "Welcome to Cannon Simulation!\n" +
            "\nMove cannon's position = W A S D\n" +
            "Move shooting direction = Left | Right \n" +
            "Launch bullet = Space\n";
    private static int cpSize = 230;      // set control panel's width

    public Shooter() {
        // setup the frame
        frame = new JFrame("Target Shooter");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setFocusable(true);
        frame.setVisible(true);

        // setup control panel itself
        JTextArea instruction = new JTextArea(INSTRUCTION);
        instruction.setBounds(5, 5, cpSize - 5, 100);
        JLabel bVLabel = new JLabel("Bullet Velocity");
        JTextField bulletVelocity = new JTextField("50");
        bulletVelocity.setBounds(5, 125, cpSize-5, 20);
        bVLabel.setBounds(5, 105, cpSize-5, 20);
        JLabel bulletsLabel = new JLabel(bulletString);
        bulletsLabel.setBounds(5, 145, cpSize-5, 20);
        JLabel massLabel = new JLabel("Cannon Mass");
        JTextField cannonSpeed = new JTextField("1");
        massLabel.setBounds(5, 165, cpSize-5, 20);
        cannonSpeed.setBounds(5, 185, cpSize-5, 20);
        JLabel windLabel = new JLabel("Wind Speed");
        JTextField windSpeed = new JTextField("10");
        windLabel.setBounds(5, 205, cpSize-5, 20);
        windSpeed.setBounds(5, 225, cpSize-5, 20);
        JLabel angleLabel = new JLabel("Angle Trajectory");
        JTextField angle = new JTextField("0");
        angleLabel.setBounds(5, 245, cpSize-5, 20);
        angle.setBounds(5, 265, cpSize-5, 20);
        JButton apply = new JButton("Apply");
        apply.setBounds(5, 285, cpSize-5, 20);
        
        
        frame.add(instruction);
        frame.add(bVLabel);
        frame.add(bulletVelocity);
        frame.add(bulletsLabel);
        frame.add(massLabel);
        frame.add(cannonSpeed);
        frame.add(windLabel);
        frame.add(windSpeed);
        frame.add(angleLabel);
        frame.add(angle);
        frame.add(apply);


        // setup drawing area
        DrawingArea drawingArea = new DrawingArea(frame.getWidth(), frame.getHeight(), cpSize);
        cannon = new Cannon(drawingArea.GRAPH_SCALE / 2, drawingArea.getOriginX(), drawingArea.getOriginY());
        drawingArea.setCannon(cannon);
        frame.add(drawingArea);

        // Keyboard shortcuts
        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.requestFocusInWindow();
            }
        });

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                    	//condition for shooting plus multiple bullets
                        if(currentBullets > 0) {
                            bullet = new Bullet(cannon.getBarrelWidth() / 2, (int) cannon.getBarrelMouthX(), (int) cannon.getBarrelMouthY(), cannon.getAngle(),
                                                Double.parseDouble(bulletVelocity.getText()), drawingArea.getTime(),
                                                Integer.parseInt(cannonSpeed.getText()), Integer.parseInt(windSpeed.getText()), Integer.parseInt(angle.getText()));
                            drawingArea.setBullet(bullet);
                            currentBullets--;
                            bulletString = "Bullets: " + Integer.toString(currentBullets) + "/" + Integer.toString(maxBullets);
                            bulletsLabel.setText(bulletString);
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        cannon.rotateLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        cannon.rotateRight();
                        break;
                    case KeyEvent.VK_W:
                        cannon.moveUp();
                        break;
                    case KeyEvent.VK_A:
                        if(cannon.getPositionX() > 0) {
                        cannon.moveLeft();
                        }
                        break;
                    case KeyEvent.VK_S:
                        cannon.moveDown();
                        break;
                    case KeyEvent.VK_D:
                        if(cannon.getPositionX() < drawingArea.getWidth()/4 + 100) {
                            cannon.moveRight();
                        }
                        break;
                }
            }
        });

        drawingArea.start();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Shooter::new);
    }
}

class Target {
    int positionX;
    int positionY;
    double radius;
    private double timeInitial;
    private double screenHeight;
    private static Color COLOR = Color.RED;

    public Target(double radius, int screenX, double timeInitial, double screenHeight) {
        this.radius = radius;
        this.positionX = screenX;
        this.timeInitial = timeInitial;
        this.screenHeight = screenHeight;
    }
    public double getRadius() {
        return radius;
    }
    public double getPositionX() {
        return positionX;
    }
    public double getPositionY() {
        return positionY;
    }
    public void setTime(double time) {
        timeInitial = time;
    }
    public Color getColor() {
        return COLOR;
    }
    public void changeColor(Color color) {
        COLOR = color;
    }
    /**
     counting distance based on time
     target x stays in place
     target y moves up and down
     */
    public void move(double time) {
        double currentTime = time - timeInitial;
        positionY = (int)(Math.abs(Math.sin(currentTime/3)) * screenHeight*3/4 + screenHeight*1/8);
    }

    // drawing function
    public void draw(Graphics g) {
        int size = (int) (radius * 2);
        Graphics2D g2 = (Graphics2D) g;
        Color tempColor = g.getColor();
        g2.setColor(COLOR);

        // draw the target
        g2.fillOval((int) (positionX - radius), (int) (positionY - radius), size, size);
        g2.setColor(tempColor);
    }
}
