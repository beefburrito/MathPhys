package Meeting01_GraphingApp;

/*
    Matfis pertemuan 01
    Simple graphing app
    All UI-elements are measured in pixels, while the functions are drawn in units and angle is in radian
    Please write every decimals with dot (.) not comma (,)

    Explain how the graphing app works, which controls the coordinates shown, what functions are drawn.
    Generally, in screen with double-buffer system, there would be one layer to be displayed and another layer to be drawn on
    Thus in event loop, there are three steps:
    - Update the module (NPC's movement, player's action, etc)
    - Render the new updates in the drawing layer
    - Change current display layer with the previously drawn layer.

   TODO:
     1. Play with the app. What does each field do? What's the difference if you change one field? What if you use smaller/bigger increment factor?
     >> Length of x/y axis sets the maximum value of the x/y coordinate to be displayed
     >> Start point x sets where the lines start being graphed
     >> Incremental factor sets the frequency at which 'points' will be graphed lower number means more accurate graph display
     2. Change the function into another continuous linear/polynomial function
     3. Change the function into other functions with discontinuity, such as 1/x. What happened?
     4. Change the function into trigonometric functions or any function that needs java.lang.Math library
     5. Change the function and fields into parametric functions
     6. Change the coordinates into polar coordinates
*/

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.EventQueue;
import java.awt.GridLayout;

class GraphingApp {
    private JFrame frame;

    // members of control panel
    private JPanel controlPanel;
    private DrawingArea drawingArea;
    private JTextField fieldLengthX;
    private JTextField fieldLengthY;
    private JTextField fieldBegin;
    private JTextField fieldIncrement;
    private JButton button;

    private int cpWidth = 270;      // set control panel's width

    public GraphingApp() {
        // setup the frame
        frame = new JFrame("Graphing App");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);

        // setup control panel itself
        controlPanel = new JPanel(new GridLayout(5, 2, 5, 0));
        createControlPanel(controlPanel);
        controlPanel.setBounds(0, 0, cpWidth, 300);
        frame.add(controlPanel);

        // setup drawing area
        drawingArea = new DrawingArea();
        frame.add(drawingArea);
    }

    // add members to control panel
    private void createControlPanel(JPanel panel) {
        // the x-axis would be shown from -lengthX to lengthX
        JLabel labelLengthX = new JLabel("Length of X axis");
        controlPanel.add(labelLengthX);
        fieldLengthX = new JTextField(2);
        controlPanel.add(fieldLengthX);

        // the y-axis would be shown from -lengthY to lengthY
        JLabel labelLengthY = new JLabel("Length of Y axis");
        controlPanel.add(labelLengthY);
        fieldLengthY = new JTextField(2);
        controlPanel.add(fieldLengthY);

        // function calculation will start from startX
        JLabel labelBegin = new JLabel("Start point from X");
        controlPanel.add(labelBegin);
        fieldBegin = new JTextField(2);
        controlPanel.add(fieldBegin);

        // the lesser the number, the more detailed it will become
        JLabel labelIncrement = new JLabel("Incremental factor");
        controlPanel.add(labelIncrement);
        fieldIncrement = new JTextField(2);
        controlPanel.add(fieldIncrement);

        // clicking the button will start the animation
        button = new JButton("Begin graphing");
        button.addActionListener(e -> sendValuesToDrawer());
        controlPanel.add(button);
    }
}
