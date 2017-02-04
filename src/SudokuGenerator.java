import java.util.*;

public class SudokuGenerator { 
	
	public static Random rand = new Random();
	
	public static void printSudoku(short[] sudoku) {
		for(int i = 0; i < 9; i++) {
			if(i > 0 && i%3 == 0) {
				for(int j = 0; j < 11; j++)
					System.out.print('-');
				System.out.println();
			}
			for(int j = 0; j < 9; j++) {
				if(j > 0 && j%3 ==0)
					System.out.print('|');
				short value = sudoku[i*9 + j];
				if(value > 0)
					System.out.print(value);
				else
					System.out.print(' ');
			}
			System.out.println();
		}
	}
	
	public static HashSet<Short> getSubGrid(int index, short[] sudoku) {
		HashSet<Short> subGrid = new HashSet<Short>();
		int gridRow = index / 27;
		int gridColumn = (index % 9) / 3;
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++) {
				int pos = (gridRow * 3 + i) * 9 + gridColumn * 3 + j;
				if(sudoku[pos] > 0)
					subGrid.add(sudoku[pos]);
			}
		return subGrid;
	}
	
	public static HashSet<Short> getColumn(int index, short[] sudoku) {
		HashSet<Short> column = new HashSet<Short>();
		int columnIndex = index % 9;
		for(int i = 0; i < 9; i++) {
			int pos = i*9 + columnIndex;
			if(sudoku[pos] > 0)
				column.add(sudoku[pos]);
		}
		return column;
	}
	
	public static HashSet<Short> getRow(int index, short[] sudoku) {
		HashSet<Short> row = new HashSet<Short>();
		int rowIndex = index / 9;
		for(int i = 0; i < 9; i++) {
			int pos = rowIndex * 9 + i;
			if(sudoku[pos] > 0)
				row.add(sudoku[pos]);
		}
		return row;
	}
	
	public static ArrayList<Short> getOptions(int index, short[] sudoku) {
		if(index >= 81)
			return new ArrayList<>();
		ArrayList<Short> options = new ArrayList<Short>();
		for(int i = 1; i <= 9; i++)
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
		short[] sudoku = new short[81];
		LinkedList<ArrayList<Short>> stack = new LinkedList<ArrayList<Short>>();
		ArrayList<Short> options = getOptions(stack.size(), sudoku);
		while(stack.size() < 81) {
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
