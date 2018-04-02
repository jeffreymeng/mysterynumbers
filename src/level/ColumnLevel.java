/*
 * Programmers: Jeffrey Meng and Dylan Yang
 * Date: Mar 27, 2018
 * Purpose: to manage the numbers in a column-format level
 */

package level;

import java.util.*;
import util.Utils;

public class ColumnLevel extends Level {

	private Digit[][] numGrid = {{}, {}, {}};
	private char operation;
	private int numVariables;
	private int[] answers;
	private int[][] hiddenCoordinates;
	//private ArrayList<int> answers;

	final static char ADDITION = '+';
	final static char SUBTRACTION = '-';
	final static char MULTIPLICATION = '*';
	final static char DIVISION = '/';

	public ColumnLevel() {
		this.operation = ADDITION;
		num1 = Utils.randInt(1, 1000);
		num2 = Utils.randInt(1, 1000);

		fillNumGrid(num1, num2);

		numVariables = getNumGridLength() / 3;

		answers = new int[numVariables];
		hiddenCoordinates = new int[numVariables][2];
	}

	public ColumnLevel(char operation) {
		this.operation = operation;
		num1 = Utils.randInt(1, 1000);
		num2 = Utils.randInt(1, 1000);

		if (operation == SUBTRACTION && num2 > num1)
			swapNums();
		else if (operation == ADDITION && String.valueOf(num2).length() > String.valueOf(num1).length())
			swapNums();

		fillNumGrid(num1, num2);

		numVariables = getNumGridLength() / 3;

		answers = new int[numVariables];
		hiddenCoordinates = new int[numVariables][2];
	}

	public ColumnLevel(char operation, int numVariables) {
		this.operation = operation;
		this.numVariables = numVariables;
		num1 = Utils.randInt(1, 1000);
		num2 = Utils.randInt(1, 1000);

		if (operation == SUBTRACTION && num2 > num1)
			swapNums();
		else if (operation == ADDITION && Utils.getDigits(num2) > Utils.getDigits(num1))
			swapNums();

		fillNumGrid(num1, num2);

		answers = new int[numVariables];
		hiddenCoordinates = new int[numVariables][2];
	}

	public ColumnLevel(char operation, int num1, int num2) {
		this.operation = operation;
		this.num1 = num1;
		this.num2 = num2;
		if (operation == SUBTRACTION && this.num2 > this.num1)
			swapNums();

		fillNumGrid(num1, num2);

		numVariables = getNumGridLength() / 3;

		answers = new int[numVariables];
		hiddenCoordinates = new int[numVariables][2];
	}

	public ColumnLevel(char operation, int num1min, int num1max, int num2min, int num2max) {
		this.operation = operation;
		num1 = Utils.randInt(num1min, num1max);
		num2 = Utils.randInt(num2min, num2max);
		if (operation == SUBTRACTION && this.num2 > this.num1)
			swapNums();

		fillNumGrid(num1, num2);

		numVariables = getNumGridLength() / 3;

		answers = new int[numVariables];
		hiddenCoordinates = new int[numVariables][2];
	}

	public void swapNums() {
		int temp = num1;
		num1 = num2;
		num2 = temp;
	}

	public void fillNumGrid(int num1, int num2) {
		String num1String = String.valueOf(num1);
		numGrid[0] = new Digit[num1String.length()];
		for (int i = 0; i < num1String.length(); i++) {
			numGrid[0][i] = new Digit(Character.getNumericValue(num1String.charAt(i)), true);
		}

		String num2String = String.valueOf(num2);
		numGrid[1] = new Digit[num2String.length()];
		for (int i = 0; i < num2String.length(); i++)
			numGrid[1][i] = new Digit(Character.getNumericValue(num2String.charAt(i)), true);
	}

	public char getOperation() {
		return operation;
	}

	public void setOperation(char operation) {
		this.operation = operation;
		if (operation == SUBTRACTION && this.num2 > this.num1)
			swapNums();
	}

	public int getNumGridLength() {
		int sum = 0;
		for (int i = 0; i < numGrid.length; i++)
			for (int j = 0; j < numGrid[i].length; j++)
				sum++;
		return sum;
	}

	public void printNumGrid() { // DEBUG ONLY
		for (Digit[] row : numGrid)
			for (Digit col : row)
				System.out.println((int)col.getValue());
		System.out.println(getNumGridLength());
	}

	public void addVariables() {
		int randRowIndex;
		int randColIndex;
		for (int i = 0; i < numVariables; i++) {
			randRowIndex = Utils.randInt(0, numGrid.length);
			randColIndex = Utils.randInt(0, randRowIndex);
			answers[i] = numGrid[randRowIndex][randColIndex].getValue();
			hiddenCoordinates[i][0] = randRowIndex;
			hiddenCoordinates[i][1] = randColIndex;
			numGrid[randRowIndex][randColIndex].setVisible(false);
		}

		Arrays.sort(answers);
		Arrays.sort(hiddenCoordinates);
		for (int i = 0; i < hiddenCoordinates.length; i++)
			Arrays.sort(hiddenCoordinates[i]);

	}

	public boolean fill(int index, int number) {
		if (answers[index] == number) {
			int numRow = hiddenCoordinates[index][0];
			int numCol = hiddenCoordinates[index][1];
			numGrid[numRow][numCol].setVisible(true);
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		ColumnLevel cl = new ColumnLevel(ColumnLevel.ADDITION);
		cl.printNumGrid();
	}

}
