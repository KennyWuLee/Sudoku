import java.util.*;

public class SudokuGenerator { 
	
	public static Random rand = new Random();
	public final static int SIZE = 4;
	
	public static void printSudoku(short[] sudoku) {
		for(int i = 0; i < (SIZE*SIZE); i++) {
			if(i > 0 && i%SIZE == 0) {
				for(int j = 0; j < (SIZE*SIZE)+SIZE-1; j++)
					System.out.print('-');
				System.out.println();
			}
			for(int j = 0; j < (SIZE*SIZE); j++) {
				if(j > 0 && j%SIZE == 0)
					System.out.print('|');
				short value = sudoku[i*(SIZE*SIZE) + j];
				char c;
				if(value == 0)
					c = ' ';
				else if(value < 10)
					c = (char) ('0' + value);
				else
					c = (char) ('a' + (value - 10));
				System.out.print(c);
			}
			System.out.println();
		}
	}
	
	public static HashSet<Short> getSubGrid(int index, short[] sudoku) {
		HashSet<Short> subGrid = new HashSet<Short>();
		int gridRow = index / (SIZE*SIZE*SIZE);
		int gridColumn = (index % (SIZE*SIZE)) / SIZE;
		for(int i = 0; i < SIZE; i++)
			for(int j = 0; j < SIZE; j++) {
				int pos = (gridRow * SIZE + i) * (SIZE*SIZE) + gridColumn * SIZE + j;
				if(sudoku[pos] > 0)
					subGrid.add(sudoku[pos]);
			}
		return subGrid;
	}
	
	public static HashSet<Short> getColumn(int index, short[] sudoku) {
		HashSet<Short> column = new HashSet<Short>();
		int columnIndex = index % (SIZE*SIZE);
		for(int i = 0; i < (SIZE*SIZE); i++) {
			int pos = i*(SIZE*SIZE) + columnIndex;
			if(sudoku[pos] > 0)
				column.add(sudoku[pos]);
		}
		return column;
	}
	
	public static HashSet<Short> getRow(int index, short[] sudoku) {
		HashSet<Short> row = new HashSet<Short>();
		int rowIndex = index / (SIZE*SIZE);
		for(int i = 0; i < (SIZE*SIZE); i++) {
			int pos = rowIndex * (SIZE*SIZE) + i;
			if(sudoku[pos] > 0)
				row.add(sudoku[pos]);
		}
		return row;
	}
	
	public static ArrayList<Short> getOptions(int index, short[] sudoku) {
		if(index >= (SIZE*SIZE*SIZE*SIZE))
			return new ArrayList<>();
		ArrayList<Short> options = new ArrayList<Short>();
		for(int i = 1; i <= (SIZE*SIZE); i++)
			options.add((short) i);
		for(short s : getRow(index, sudoku))
			options.remove(new Short(s));
		for(short s : getColumn(index, sudoku))
			options.remove(new Short(s));
		for(short s : getSubGrid(index, sudoku))
			options.remove(new Short(s));
		return options;
	}
	
	public static short[] generateSudoku() {
		short[] sudoku = new short[(SIZE*SIZE*SIZE*SIZE)];
		LinkedList<ArrayList<Short>> stack = new LinkedList<ArrayList<Short>>();
		ArrayList<Short> options = getOptions(stack.size(), sudoku);
		while(stack.size() < (SIZE*SIZE*SIZE*SIZE)) {
			if(options.size() > 0) {
				//if we have options choose one randomly
				short choice = options.remove(rand.nextInt(options.size()));
				sudoku[stack.size()] = choice;
				//add remaining options to the stack
				stack.addLast(options);
				options = getOptions(stack.size(), sudoku);
			} else {
				//if not backtrack
				options = stack.removeLast();
				//clear the previously set number
				sudoku[stack.size()] = 0;
			}
			// -------------------------------
			// only to make it pretty
			printSudoku(sudoku);
			System.out.println();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// -------------------------------
		}
		return sudoku;
	}
	
	public static void main(String[] args) {
		short[] sudoku = generateSudoku();
		printSudoku(sudoku);
	}
}
