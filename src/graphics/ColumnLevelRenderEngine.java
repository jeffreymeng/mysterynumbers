/*
 * Programmers: Jeffrey Meng and Dylan Yang
 * Date: Apr 11, 2018
 * Purpose: This class contains the graphics to render the content generated in each level.
 */
package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import level.*;

public class ColumnLevelRenderEngine implements ButtonListener, ClickListener {
	JPanel panel;
	ColumnLevel level;
	Font rubik;
	Button[] buttons;
	Button submitButton;
	int numHiddenDigits;
	int numSolvedDigits;// represents the number of digits that the user has filled out. If the user has
						// filled out a digit incorrectly it is still counted.
	int focusSlot;// the digit slot that the user is focused on. This corresponds to an element in
					// the clickzones array.

	// these five variabes make the levels harder as you play for longer
	int numVariables = 1;
	int numSolved = 0;
	int maxVariables = 6;
	// the hardest level you can ever get, assuming more variables = harder level

	int numToNextVariableIncrease = 1;
	int numSolvedInCurrentIncreaseCycle = 0;
	// resets each time a variable is added

	// clickzones. The order refers to the order of
	Clickzone[] clickzones;
	int[] clickzoneValues;// parallel to above array, contains user's filled in values
	boolean textPrinted;// for debug printing

	String lastSubmitAttemptMessage;// error message for user - should be short

	public ColumnLevelRenderEngine(JPanel panel) {
		makeNewLevel(panel);
	}

	public void makeNewLevel(JPanel panel) {
		this.panel = panel;
		rubik = new Font("src/fonts/Rubik/Rubik-Regular.ttf", Font.PLAIN, 25);
		level = new ColumnLevel(ColumnLevel.ADDITION, numVariables);
		numHiddenDigits = level.getNumVariables();
		// create the buttons
		buttons = new Button[10];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new Button(Button.BLUE, new Label(String.valueOf(i), rubik), panel);

			buttons[i].addButtonListener(this);
		}

		// initialize this first because it must persist.
		clickzoneValues = new int[level.getNumVariables()];

		for (int i = 0; i < clickzoneValues.length; i++) {
			clickzoneValues[i] = -1;
		}
		rubik.setSize(15);
		submitButton = new Button(Button.BLUE, new Label("Submit", rubik), panel);
		submitButton.addButtonListener(this);

		lastSubmitAttemptMessage = "";
		textPrinted = false;
		focusSlot = -1;
	}

	public void render(Graphics graphics) {
		drawGrid(graphics);
		drawInputTray(graphics);
		drawSubmitTray(graphics);
	}

	public void drawGrid(Graphics graphics) {
		Digit[][] grid = level.getAlignedDigitGrid();
		Digit digit;
		String text = "";
		Label[][] label = new Label[grid.length][grid[0].length];
		// the width and height of the level area, and the x and y area.
		int levelWidth, levelHeight, levelX, levelY;

		int levelPadding;

		int equalsBarHeight = 5;
		int equalsBarPadding = 5;

		// calculate level's width, height, x, and y
		levelWidth = panel.getWidth() / 2;

		// add equals line height and padding for top and bottom to the
		// levelHeight
		levelHeight = (panel.getHeight() / 2) + (equalsBarHeight + (equalsBarPadding * 2));
		levelX = (panel.getWidth() / 2) - (levelWidth / 2);
		levelY = (panel.getHeight() / 2) - (levelHeight / 2);

		levelPadding = 10;// on each side
		// rectangle around level.
		// we double the padding for width and height to account for the width
		// of the padding on both sides.
		graphics.drawRect(levelX - levelPadding, levelY - levelPadding, levelWidth + (levelPadding * 2),
				levelHeight + (levelPadding * 2));
		int labelX, labelY, labelContainerWidth, labelContainerHeight, labelLeftmost, labelTopmost;

		int labelBoxPadding;// padding for the box around hidden numbers. Unlike
							// the padding around the
		// whole level, this padding is added into the box

		int labelBoxOffset = 0;// the amount to offset the label box

		labelBoxPadding = 5;

		// labelContainerWidth refers to the box that contains the label, within
		// which we center the number, as opposed to the width of the text
		// itself. the same applies to labelContainerHeight

		// y is first when you visualize like a coordinate panes

		// init the clickzones array
		clickzones = new Clickzone[level.getNumVariables()];

		int filledClickzoneSlots = 0;

		for (int y = 0; y < grid.length; y++) {

			for (int x = 0; x < grid[y].length; x++) {

				digit = grid[y][x];

				if (digit.isSpace()) {
					text += " ";
					// don't render anything
					continue;
				}
				// we do these calculations here, above the if statement,
				// because
				// some of them are useful when drawing the box for a hidden
				// number
				labelContainerWidth = levelWidth / (grid[y].length + 1);

				// we shift everything to the right one place to make space for the addition
				// sign
				labelLeftmost = (levelX + (labelContainerWidth * (x + 1)));
				labelX = labelLeftmost + (labelContainerWidth / 2);

				// we add the padding for top and bottom, as well as the height
				// of the equals bar
				// here
				labelContainerHeight = (levelHeight / grid.length);
				labelTopmost = levelY + (labelContainerHeight * y);
				labelY = labelTopmost + (labelContainerHeight / 2);

				// if this is the last row, we add the width and padding of the
				// equals bar

				if (y == (grid.length - 1)) {

					labelY += equalsBarHeight + (equalsBarPadding * 2);

					// for the hidden number boxes.
					labelBoxOffset = equalsBarHeight + (equalsBarPadding * 2);
				}

				// for debugging
				text += digit.getValue();

				if (!digit.isVisible()) {
					// filledClickzoneSlots is the current slot index.
					int currentSlot = filledClickzoneSlots;// for better readability
					filledClickzoneSlots++;
					if (currentSlot == focusSlot) {
						graphics.setColor(Color.BLUE);
					}
					// draw a empty box
					graphics.drawRect(labelLeftmost + labelBoxPadding, labelTopmost + labelBoxPadding + labelBoxOffset,
							labelContainerWidth - (labelBoxPadding * 2),
							labelContainerHeight - (labelBoxPadding * 2) - labelBoxOffset);
					if (currentSlot == focusSlot) {
						graphics.setColor(Color.BLACK);
					}
					clickzones[currentSlot] = new Clickzone(labelLeftmost + labelBoxPadding,
							labelTopmost + labelBoxPadding + labelBoxOffset,
							labelContainerWidth - (labelBoxPadding * 2),
							labelContainerHeight - (labelBoxPadding * 2) - labelBoxOffset, panel, this);

					if (clickzoneValues[currentSlot] > -1) {
						label[y][x] = new Label(String.valueOf(clickzoneValues[currentSlot]), rubik);
						label[y][x].draw(graphics, labelX, labelY);
					}

					continue;
				}

				label[y][x] = new Label(String.valueOf(digit.getValue()), rubik);

				// System.out.println(levelWidth);
				// System.out.println(grid.length);
				// System.out.println(grid[y].length);

				label[y][x].draw(graphics, labelX, labelY);
			}

			// if this is between the second to last and last rows, we draw the
			// plus sign and bar
			if (y == (grid.length - 1)) {

				// the equals bar y is at the top of the last line, because
				// during that line we offset it's y by 15 px.
				int equalsBarY = (levelY + ((levelHeight / grid.length) * (y))) + equalsBarPadding;
				// draw the bar with padding of 5px on each side
				graphics.fillRect(levelX + equalsBarPadding, equalsBarY, levelWidth - (equalsBarPadding * 2),
						equalsBarHeight);
			}
			text += "\n";

		}
		// draw the sign
		int signContainerWidth = levelWidth / (grid[0].length);
		int signLeftmost = levelX;// sign is in leftmost column
		int signX = signLeftmost + (signContainerWidth / 2);

		int signContainerHeight = (levelHeight / grid.length);
		int signTopmost = levelY + (signContainerHeight * (grid.length - 2));
		int signY = signTopmost + (signContainerHeight / 2);
		Label signLabel = new Label(String.valueOf(level.getOperation()), rubik);
		signLabel.draw(graphics, signX, signY);

		// debugging
		// if (!textPrinted) {
		// 	System.out.println(text);
		// 	textPrinted = true;
		// }
	}

	public void drawInputTray(Graphics graphics) {
		int trayPadding = 10;
		int trayHeight = panel.getHeight() / 10;
		// we want the tray to be 9/10 to the bottom
		int trayY = ((panel.getHeight() / 10) * 9) - (trayHeight / 2);
		int trayWidth = panel.getWidth() / 2;
		int trayX = (panel.getWidth() / 2) - (trayWidth / 2);

		// draw the tray border
		graphics.drawRect(trayX - trayPadding, trayY - trayPadding, trayWidth + (trayPadding * 2),
				trayHeight + (trayPadding * 2));

		// init array and vars

		int buttonX, buttonY, buttonWidth, buttonHeight;
		int buttonPadding = 5;// 5 px on left and right, so total of 10px between buttons
		for (int i = 0; i < buttons.length; i++) {

			buttonWidth = (trayWidth / buttons.length);

			buttonX = trayX + ((trayWidth / buttons.length) * i);

			buttonHeight = trayHeight;
			buttonY = trayY;
			buttons[i].draw(graphics, buttonX + buttonPadding, buttonY, buttonWidth - (buttonPadding * 2),
					buttonHeight);
		}

	}

	public void drawSubmitTray(Graphics graphics) {
		int trayPadding = 10;
		int trayHeight = panel.getHeight() / 15;
		// we want the tray to be 1/10 to the bottom
		int trayY = ((panel.getHeight() / 10) * 1) - (trayHeight / 2);
		int trayWidth = panel.getWidth() / 2;
		int trayX = (panel.getWidth() / 2) - (trayWidth / 2);
		graphics.setColor(Color.BLACK);
		// draw the tray border
		graphics.drawRect(trayX - trayPadding, trayY - trayPadding, trayWidth + (trayPadding * 2),
				trayHeight + (trayPadding * 2));

		// draw the submit button. It takes up the right half

		submitButton.draw(graphics, trayX + (trayWidth / 2), trayY, trayWidth / 2, trayHeight);

		// last message label takes up the left half
		Label lastMessageLabel = new Label(lastSubmitAttemptMessage, rubik, Color.BLACK);
		lastMessageLabel.draw(graphics, trayX + 20/* padding */, trayY + 20/* padding */);

	}

	public void checkUserAnswer() {
		Digit[][] grid = level.getAlignedDigitGrid();
		Digit digit;
		int num1, num2;
		//System.out.println(buildNumber(grid[0]));
		//System.out.println(buildNumber(grid[1]));
		//System.out.println(buildNumber(grid[2]));

		if (buildNumber(grid[0]) + buildNumber(grid[1]) == buildNumber(grid[2])) {
			// user is correct
			// calculate the number of variables in the next level.
			// Generally, we start off at after each new level, gain a variable,
			// however, each time you gain a variable, we add one to the number of levels
			// you have
			// to complete to get the next variable.
			if (numVariables <= maxVariables) {// once they limit the threshold the difficulty stays the same
				numSolvedInCurrentIncreaseCycle++;
				numSolved++;
				if (numSolvedInCurrentIncreaseCycle >= numToNextVariableIncrease) {
					numSolvedInCurrentIncreaseCycle = 0;
					numToNextVariableIncrease++;
					numVariables++;
				}
			}

			makeNewLevel(panel);

		} else {
			// user is incorrect
			lastSubmitAttemptMessage = "Incorrect";
		}
		panel.repaint();

	}

	public int buildNumber(Digit[] row) {
		int number = 0;
		int userAnswer;
		int currentClickzone = 0;
		Digit digit;
		for (int x = 0; x < row.length; x++) {

			digit = row[x];

			if (digit.isSpace()) {

				continue;
			}
			if (!digit.isVisible()) {

				userAnswer = clickzoneValues[currentClickzone];
				if (userAnswer == -1) {
					return -1;// user did not finish puzzle, immediately exit
				}
				currentClickzone++;

				number += (userAnswer * Math.pow(10, row.length - x));
				// we need to invert x to account for the allignment issues.

				continue;
			}

			// Multiply by it's place value
			number += (digit.getValue() * Math.pow(10, row.length - x));
		}
		return number;
	}

	public void buttonPressed(MouseEvent e, Button source) {
		parseButtonEvent(e, source, 0);
	}

	public void buttonClicked(MouseEvent e, Button source) {
		parseButtonEvent(e, source, 1);
	}

	public void buttonReleased(MouseEvent e, Button source) {
		parseButtonEvent(e, source, 2);
	}

	// 3 types, 0 is pressed, 1 is clicked, 2 is released.
	public void parseButtonEvent(MouseEvent e, Button source, int type) {
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i] == source) {
				switch (type) {
				case 0:
					// button pressed

					break;
				case 1:
					// button clicked

					break;
				case 2:
					// button released
					// System.out.println(i + " pressed.");

					if (focusSlot > -1) {
						clickzoneValues[focusSlot] = i;
						panel.repaint();
					}
					break;
				default:
					// uh oh
					System.out.println(
							"ERROR in ColumnLevelRenderEngine. Expected type to be between 0 and 2, inclusive, but type wasan't.");
				}
			} else if (source == submitButton) {
				checkUserAnswer();
			}
		}
	}

	public void clickzonePressed(MouseEvent e, Clickzone source) {
		parseClickEvent(e, source, 0);
	}

	public void clickzoneClicked(MouseEvent e, Clickzone source) {
		parseClickEvent(e, source, 1);
	}

	public void clickzoneReleased(MouseEvent e, Clickzone source) {
		parseClickEvent(e, source, 2);

	}

	public void parseClickEvent(MouseEvent e, Clickzone source, int type) {
		// System.out.println(clickzones.length);
		for (int i = 0; i < clickzones.length; i++) {
			// System.out.println(clickzones[i]);
			// System.out.println(source);
			if (clickzones[i] == source) {
				switch (type) {
				case 0:
					// clickzone pressed

					break;
				case 1:
					// clickzone clicked

					break;
				case 2:
					// clickzone released
					// System.out.println("clickzone " + i + " released.");
					focusSlot = i;

					panel.repaint();
					break;
				default:
					// uh oh
					System.out.println(
							"ERROR in ColumnLevelRenderEngine. Expected type to be between 0 and 2, inclusive, but type wasan't.");
				}
			}
		}
	}

}
