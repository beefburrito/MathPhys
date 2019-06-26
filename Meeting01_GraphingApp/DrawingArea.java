package Meeting01_GraphingApp;
//
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

    class DrawingArea extends JPanel {
        boolean draw = false;
        int originX;        // the origin points (0, 0)
        int originY;
        double scaleX;      //scaling the canvas according to lengthX & lengthY
        double scaleY;
        double lengthX;     // how many numbers shown along absis and ordinate
        double lengthY;
        double currentX;    // current X-point, the Y is retrieved from
        double increment;   // controlling detail
        double t;
        final int MAX_POINTS = 1000;    // in case the function is a loop, or the thread runs for far too long
        ArrayList<Point2D.Double> parametric = new ArrayList<Point2D.Double>();
        ArrayList<Point2D.Double> trigonometric = new ArrayList<Point2D.Double>();
        ArrayList<Point2D.Double> discontinuity = new ArrayList<Point2D.Double>();
        ArrayList<Point2D.Double> polar = new ArrayList<Point2D.Double>();
        ArrayList<Point2D.Double> linear = new ArrayList<Point2D.Double>();
        Image drawingArea;
        Thread animator;    // thread to draw the graph
        
        // setup the drawing area
        public DrawingArea() {
            super(null);
            setBounds(cpWidth, 0, frame.getWidth() - cpWidth, frame.getHeight());
            originX = frame.getWidth()/2;
            originY = frame.getHeight()/2;
            setBackground(Color.white);
            drawingArea = createImage(frame.getWidth() - controlPanel.getWidth(),
                    frame.getHeight());
        }
        
        // functions to draw on the screen
        public double functionX(double t) {
            return Math.cos(t);
        }
        public double functionY(double t) {
        	return Math.sin(t);
        }

        public double functionR(double t) {
            return 5*Math.sin(t);
        }
        
        public double function3(double x) {
            return 1/x;
        }
        
        public double function2(double x) {
            return 3*x+1;
        }
        
        private double polarFunction(double theta) {
            return  1 + Math.sin(theta);
        }

        public double polarX(double r, double theta) {
            return r * Math.cos(theta);
        }
        public double polarY(double r, double theta) {
            return r * Math.sin(theta);
        }
        
        public void beginDrawing(double lengthX, double lengthY, double startX, double increment) {
            // retrieve data
            this.lengthX = lengthX + 1;
            this.lengthY = lengthY + 1;
            this.scaleX = (double) (frame.getWidth() - originX) / lengthX;
            this.scaleY = (double) (frame.getHeight() - originY) / lengthY;
            this.currentX = startX;
            this.increment = increment;
            
            // trigger drawing process
            draw = true;
            drawingArea = createImage(frame.getWidth() - controlPanel.getWidth(),
                    frame.getHeight());
            animator = new Thread(this::eventLoop);
            animator.start();
        }

        void eventLoop() {
            while(draw) {
                update();
                render();
                printScreen();
                try {
                    animator.sleep(10);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            // clean up the thread
            System.out.println("stopping");
            draw = false;
            try {
                animator.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        // x=at^2 y=2at
        // add points to be drawn
        void update()
        {
            // while there is still a need to draw
            if(currentX < lengthX && currentX > -lengthX && linear.size()  < MAX_POINTS)
            {
                System.out.println("checking");
                currentX = currentX + increment;
                parametric.add(new Point2D.Double(functionY(currentX), functionX(currentX)));
                trigonometric.add(new Point2D.Double(currentX, functionR(currentX)));
                discontinuity.add(new Point2D.Double(currentX, function3(currentX)));
                linear.add(new Point2D.Double(currentX, function2(currentX)));
                double r1 = polarFunction(currentX);
                double cartesianX1 = polarX(r1, currentX) * 10;
                double cartesianY1 = polarY(r1, currentX) * 10;
                polar.add(new Point2D.Double(cartesianX1, cartesianY1));
            }
            else {      // cleanup for the next thread
                draw = false;
                parametric.clear();
                trigonometric.clear();
                discontinuity.clear();
                polar.clear();
            }
        }

        void render() {
            if (drawingArea != null) {
                //get graphics of the image where coordinate and function will be drawn
                Graphics g = drawingArea.getGraphics();
                g.setColor(Color.BLACK);

                //draw the x-axis and y-axis
                g.drawLine(0, originY, getWidth(), originY);
                g.drawLine(originX, 0, originX, getHeight());

                //print numbers on the x-axis and y-axis, based on the scale
                for (int i = 0; i < lengthX; i++) {
                    g.drawString(Integer.toString(i), (int) (originX + (i * scaleX)), originY);
                    g.drawString(Integer.toString(-1 * i), (int) (originX + (-i * scaleX)), originY);
                }
                for (int i = 0; i < lengthY; i++) {
                    g.drawString(Integer.toString(-1 * i), originX, (int) (originY + (i * scaleY)));
                    g.drawString(Integer.toString(i), originX, (int) (originY + (-i * scaleY)));
                }
                
                // draw the lines
                for (int i = 0; i < linear.size() - 1; i++) {
                    g.drawLine((int) (originX + parametric.get(i).x * scaleX), (int) (originY - parametric.get(i).y * scaleY),
                            (int) (originX + parametric.get(i + 1).x * scaleX), (int) (originY - parametric.get(i + 1).y * scaleY));
                    g.drawLine((int) (originX + trigonometric.get(i).x * scaleX), (int) (originY - trigonometric.get(i).y * scaleY),
                            (int) (originX + trigonometric.get(i + 1).x * scaleX), (int) (originY - trigonometric.get(i + 1).y * scaleY));
                    g.drawLine((int) (originX + discontinuity.get(i).x * scaleX), (int) (originY - discontinuity.get(i).y * scaleY),
                            (int) (originX + discontinuity.get(i + 1).x * scaleX), (int) (originY - discontinuity.get(i + 1).y * scaleY));
                    g.drawLine((int) (originX + linear.get(i).x * scaleX), (int) (originY - linear.get(i).y * scaleY),
                            (int) (originX + linear.get(i + 1).x * scaleX), (int) (originY - linear.get(i + 1).y * scaleY));
                    g.drawLine((int) (originX + polar.get(i).x * scaleX), (int) (originY - polar.get(i).y * scaleY),
                            (int) (originX + polar.get(i + 1).x * scaleX), (int) (originY - polar.get(i + 1).y * scaleY));
                }
            }
        }

        void printScreen()
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
                System.out.println("Graphics error: " + ex);
            }
        }
    }
