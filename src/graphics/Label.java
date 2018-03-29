/*
 * Programmer: Jeffrey Meng and Dylan Yang
 * Date: Mar 28 2018
 * Purpose:
 */

package graphics;

import java.awt.*;

public class Label {
	private String text;
	private Font font;
	public final static String TOP = "top";
	public final static String BOTTOM = "bottom";
	public final static String LEFT = "left";
	public final static String RIGHT = "right";
	public final static String VCENTER = "vcenter";
	public final static String HCENTER = "hcenter";
	public final static String CENTER = VCENTER + HCENTER;

	// concatenate option strings to create an option modifier.

	public Label(String text, Font font) {
		this.font = font;
		this.text = text;
	}

	public void draw(Graphics graphics, int width, int height, String options) {
		int x = -1, y = -1;

		graphics.setFont(font.get());

		int titleWidth = graphics.getFontMetrics().stringWidth(text);
		int titleHeight = graphics.getFontMetrics().getHeight();
		int padding = 25;// px
		if (options.indexOf("top") > -1) {
			y = padding;
		} else if (options.indexOf("bottom") > -1) {
			y = (height - padding) + (titleHeight);
		}
		if (options.indexOf("left") > -1) {
			x = padding;
		} else if (options.indexOf("right") > -1) {
			x = (width - padding) + (titleWidth);
		}
		
		if ((options.indexOf("vcenter") > -1) || (y == -1)) {//defualt is center
			y = ((height / 2) - (titleHeight / 2));
			
		} else if ((options.indexOf("hcenter") > -1) || (x == -1)) {
			x = ((width / 2) - (titleWidth / 2));
		}

		// we don't need to pass title because height is static across the font
		draw(graphics, x, y);// get the top left corner

	}

	public void draw(Graphics graphics, int x, int y) {
		graphics.drawString(text, x, y);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}
}