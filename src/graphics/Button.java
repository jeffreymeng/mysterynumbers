/*
 * Programmer: Jeffrey Meng and Dylan Yang
 * Date: Mar 28 2018
 * Purpose: A Button class, like JButton.
 */

package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class Button implements MouseListener {
	ButtonListener buttonInterface;
	boolean listenerAdded = false;
	private Color color;
	private Label label;
	private int x, y, width, height;
	private JPanel panel;
	private boolean pressed = false;
	private boolean pressedBeforeRepaint = false;
	private boolean releasedBeforeRepaint = false;
	public final static String TOP = "top";
	public final static String BOTTOM = "bottom";
	public final static String LEFT = "left";
	public final static String RIGHT = "right";
	public final static String VCENTER = "vcenter";
	public final static String HCENTER = "hcenter";
	public final static String CENTER = VCENTER + HCENTER;

	public final static Color BLUE = new Color(0, 0, 255);

	// concatenate option strings to create an option modifier.
	// in the future: maybe extend JButton?
	public Button(Color color, Label label, JPanel panel) {
		this.color = color;
		this.label = label;
		this.panel = panel;
		panel.addMouseListener(this);

	}

	public Button(Color color, Label label, Color labelColor, JPanel panel) {
		this.color = color;
		label.setColor(labelColor);
		this.label = label;
		this.panel = panel;
		panel.addMouseListener(this);

	}

	public void addButtonListener(ButtonListener listener) {
		buttonInterface = listener;
		listenerAdded = true;

	}

	public void draw(Graphics graphics, int width, int height, String options) {
		draw(graphics, width, height, options, 0, 0);

	}

	public void draw(Graphics graphics, int width, int height, String options, int xoffset, int yoffset) {
		int panelWidth = panel.getWidth();
		int panelHeight = panel.getHeight();
		int x = -1;
		int y = -1;

		int padding = 25; // px

		// to center something horizontally, we take half of the panel width, then we
		// subtract half of the button width. That way, we can get the x coordinate of
		// the (top) left corner,
		// for drawing later

		if (options.indexOf("top") > -1) {
			y = padding;
		} else if (options.indexOf("bottom") > -1) {
			y = (panelHeight - padding) - (height);
		} else if (options.indexOf("right") > -1) {
			x = (panelWidth - padding) - (width);
		}
		if ((options.indexOf("vcenter") > -1) || (y == -1)) {// Default is
																// center
			y = ((panelHeight / 2) - (height / 2));

		}
		if ((options.indexOf("hcenter") > -1) || (x == -1)) {
			x = ((panelWidth / 2) - (width / 2));

		}

		y += yoffset;
		x += xoffset;

		draw(graphics, x, y, width, height);// get the top left corner

	}

	public void draw(Graphics graphics, int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		graphics.setColor(color);

		if (pressed) {
			graphics.setColor(color.darker());
		}

		graphics.fillRect(x, y, width, height);
		graphics.setColor(color.darker().darker());
		graphics.drawRect(x, y, width, height);

		// no color is set
		if (label.getColor() == null) {
			graphics.setColor(Color.white);
			// temporarily set the color to white
			// this does not override the Label
		}

		graphics.setFont(label.getFont().get());// set the font for font metrics
		int labelWidth = graphics.getFontMetrics().stringWidth(label.getText());
		int labelHeight = graphics.getFontMetrics().getHeight();

		// (center of rectangle x) - (half of the rectangle width)
		int labelX = (x + (width / 2)) - (labelWidth / 2);

		// TODO: remove the +15px and get it to work
		int labelY = (y + (height / 2)) - (labelHeight / 2) + 15;

		label.draw(graphics, labelX, labelY);
	}

	public void mouseClicked(MouseEvent e) {
		//TODO: use clickzone instead
		if (releasedBeforeRepaint) {
			releasedBeforeRepaint = false;
			return;
		}
		// System.out.println(e.getY());
		// check if the click was inside the button
		if ((e.getX() > x && e.getX() < (x + width)) && (e.getY() > y && e.getY() < (y + height))) {
			// callback
			if (listenerAdded) {
				buttonInterface.buttonClicked(e, this);
			}
		}

	}

	public void mousePressed(MouseEvent e) {
		if (pressedBeforeRepaint) {
			// because when we repaint the class, if the button is still pressed, this event
			// will get called twice.
			pressedBeforeRepaint = false;
			return;
		}
		// check if the press was inside the button
		if ((e.getX() > x && e.getX() < (x + width)) && (e.getY() > y && e.getY() < (y + height))) {

			this.pressed = true;
			this.pressedBeforeRepaint = true;
			if (listenerAdded) {
				buttonInterface.buttonPressed(e, this);
			}
			panel.repaint();
		}

	}

	public void mouseReleased(MouseEvent e) {

		if (pressed) {
			pressed = false;
			this.releasedBeforeRepaint = true;
			if (listenerAdded) {
				buttonInterface.buttonReleased(e, this);
			}
			panel.repaint();
		}

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
