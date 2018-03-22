package graphics;

import java.awt.*;

import javax.swing.*;

public class GraphicsPanel extends JPanel{
	final int width = 800;
	final int height = 600;
	JFrame frame = new JFrame("History Tiles");
	public void paintComponent(Graphics graphics) {
		int width = frame.getWidth();
		int height = frame.getHeight();
		super.paintComponent(graphics);
		
		
		
	}
	
	public void setupWindow(GraphicsPanel panel) {
		
		
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setVisible(true);
		
	}

}