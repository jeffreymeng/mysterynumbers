/*
 * Programmer: Jeffrey Meng and Dylan Yang
 * Date: Mar 30, 2018
 * Purpose:
 */

package graphics;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MysteryNumbersGame {
	final int numCards = 3;
	JFrame frame;
	
	public MysteryNumbersGame() {
		frame = new JFrame("Mystery Numbers");

	}

	public void setupWindow() {
		int width = 500;
		int height = 600;
		
		GraphicsPanel panel = new GraphicsPanel(frame);
		
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setVisible(true);

	}

}