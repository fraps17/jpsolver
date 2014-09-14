package model;

public class Nonogram {

	private String name;
	private int[][] rows;
	private int[][] cols;
	
	public Nonogram(){}

	
	
	public Nonogram(String name, int[][] rows, int[][] cols) {
		super();
		this.name = name;
		this.rows = rows;
		this.cols = cols;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[][] getRows() {
		return rows;
	}

	public void setRows(int[][] rows) {
		this.rows = rows;
	}

	public int[][] getCols() {
		return cols;
	}

	public void setCols(int[][] cols) {
		this.cols = cols;
	}
	
	
}
