
public class VirusWorld {
	private int rows;
	private int columns;
	private HostCell[][] hosts;
	private boolean[][] virusCells;
	private Virus virus;
	private int turns;
	
	public VirusWorld(int rows, int columns, int virusStrength, int hostMaxHealth, int hostRegeneration) {
		this.rows = rows;
		this.columns = columns;
		hosts = new HostCell[rows][columns];
		virusCells = new boolean[rows][columns];
		virus = new Virus(virusStrength);
		turns = 0;
		
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				hosts[row][column] = new HostCell(hostMaxHealth, hostRegeneration); //fill grid with host cells
				virusCells[row][column] = false; //start uninfected
			}
		}
		virusCells[rows / 2][columns / 2] = true; //puts virus in the middle
	}
	public void step() {
		turns++;
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				if(!virusCells[row][column]) {
					hosts[row][column].regenerate();
				}
			}		
		}
		boolean[][] nextVirusCells = new boolean[rows][columns];
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				nextVirusCells[row][column] = virusCells[row][column];
			}
		}
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				if(virusCells[row][column]) { //attack adjacent
					attackNeighbor(row - 1, column, nextVirusCells); //attack above
					attackNeighbor(row + 1, column, nextVirusCells); //attack below
					attackNeighbor(row , column - 1, nextVirusCells); //attack left
					attackNeighbor(row , column + 1, nextVirusCells); //attack right
				}
			}
		}
		virusCells = nextVirusCells; //infection grid update
	}
	private void attackNeighbor(int row, int column, boolean[][] nextVirusCells) {
		if(row >= 0 && row < rows && column >= 0 && column < columns && !virusCells[row][column]) { //checks if inside grid
			hosts[row][column].takeDamage(virus.getStrength());
			if(!hosts[row][column].isAlive()) { //if host health = 0
				nextVirusCells[row][column] = true; //infect
			}
		}
	}
	public int getInfectedCount() {
		int count = 0;
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				if(virusCells[row][column]) {
					count++;
				}
			}
		}
		return count;
	}
	public boolean hasVirusAt(int row, int column) {
		return virusCells[row][column];
	}
	public int getRows() {
		return rows;
	}
	public int getColumns() {
		return columns;
	}
	public int getTurns() {
		return turns;
	}
}