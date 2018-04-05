/*
 * Programmer: Jeffrey Meng and Dylan Yang
 * Date: Mar 28 2018
 * Purpose:
 */

package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class Button implements MouseListener{
	private Color color;
	private Label label;
	private int x, y, width, height;
	private JPanel panel;
	private boolean pressed = false;
	public final static String TOP = "top";
	public final static String BOTTOM = "bottom";
	public final static String LEFT = "left";
	public final static String RIGHT = "right";
	public final static String VCENTER = "vcenter";
	public final static String HCENTER = "hcenter";
	public final static String CENTER = VCENTER + HCENTER;
	public final static Color BLUE = new Color(0, 0, 255);

	// concatenate option strings to create an option modifier.

	public Button(Color color, Label label, JPanel panel) {
		this.color = color;
		this.label = label;
		panel.addMouseListener(this);
		this.panel = panel;
		
	}

	public Button(Color color, Label label, Color labelColor, JPanel panel) {
		this.color = color;
		label.setColor(labelColor);
		this.label = label;
		panel.addMouseListener(this);
		this.panel = panel;

	}

	public void draw(Graphics graphics, int width, int height, String options) {
		draw(graphics, width, height, options, 0, 0);

	}

	public void draw(Graphics graphics, int width, int height, String options,
			int xoffset, int yoffset) {
		int x = -1, y = -1;

		int padding = 25; // px

		if (options.indexOf("top") > -1) {
			y = padding;
		} else if (options.indexOf("bottom") > -1) {
			y = (height - padding) - (height);
		}
		if (options.indexOf("left") > -1) {
			x = padding;
		} else if (options.indexOf("right") > -1) {
			x = (width - padding) - (width);
		}
		if ((options.indexOf("vcenter") > -1) || (y == -1)) {// Default is
																// center
			y = ((height / 2) - (height / 2));

		}
		if ((options.indexOf("hcenter") > -1) || (x == -1)) {
			x = ((width / 2) - (width / 2));

		}
		y += yoffset;
		x += xoffset;

		draw(graphics, x, y, width, height);// get the top left corner

	}

	public void draw(Graphics graphics, int x, int y, int width, int height) {
		this.height = height;
		this.width = width;

		graphics.setColor(color);
		if (pressed) {
			graphics.setColor(color.darker());
		}
		// width is center, height is center + 100px offset

		x = (width / 2 - (width / 2));
		y = (height / 2 + ((height / 2) + 75/* offset */));

		graphics.fillRect(x, y, width, height);
		graphics.setColor(color.darker().darker());
		graphics.drawRect(x, y, width, height);
		if (label.getColor() == null) {
			graphics.setColor(Color.white);
			// temporarily set the color to white
		}
		label.draw(graphics, width, height, Label.CENTER, 0, 140);
	}

	public void mouseClicked(MouseEvent e) {
		if ((e.getX() > x && e.getX() < (x + width))
				&& (e.getY() > y && e.getY() < (y + height))) {
			// callback
			buttonClicked(e);
		}

	}

	public void mousePressed(MouseEvent e) {
		if ((e.getX() > x && e.getX() < (x + width))
				&& (e.getY() > y && e.getY() < (y + height))) {
			pressed = true;
		}
		panel.repaint();
	}

	public void mouseReleased(MouseEvent e) {
		if (color != BLUE) {
			color = BLUE;
		}
		panel.repaint();
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public Label getText() {
		return label;
	}

	public void setText(Label label) {
		this.label = label;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

}