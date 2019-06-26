//package Meeting05_Pendulum;

/*
	Matfis pertemuan 5
	Circular motion and pendulum

	TODO:
	 1. Separate drawing area from the frame
	 2. Add more pendulums
	 3. Take care of the collision among pendulum balls. Remember, the ball moves before detecting any collisions
	 4. Minigame: hitting target with pendulum(s)
 */

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;

public class Pendulum extends JFrame {
		
		public static final double GRAVITY = 0.0098;

		private DrawingArea drawingArea;
		
		public Pendulum()
		{
			//configure the main canvas
			setExtendedState(MAXIMIZED_BOTH);
			setBackground(Color.WHITE);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true);
			
			drawingArea = new DrawingArea(getWidth(), getHeight());
			add(drawingArea);
			drawingArea.start();
		}
		public static void main(String[] args)
		{
			EventQueue.invokeLater(Pendulum::new);
		}
}
