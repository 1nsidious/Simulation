import java.util.ArrayList;

public class VirusWorld {
	//virus
	private int rows;
	private int columns;
	private HostCell[][] hosts;
	private ArrayList<Virus> viruses;
	private int virusStrength;
	private int virusHealth;
	private int virusLifeTime;
//	private int replicationChance;
	//host
	private int hostMaxHealth;
	private int hostRegeneration;
	//simulation
	private int turns;
	private double immunity;
	
	public VirusWorld(int rows, int columns, int virusStrength, int virusHealth, int virusLifeTime, int hostMaxHealth, int hostRegeneration) {
		this.rows = rows;
		this.columns = columns;
		this.virusStrength = virusStrength;
		this.virusHealth = virusHealth;
		this.virusLifeTime = virusLifeTime;
//		this.replicationChance = replicationChance;
		
		this.hostMaxHealth = hostMaxHealth;
		this.hostRegeneration = hostRegeneration;
		
		hosts = new HostCell[rows][columns];
		viruses = new ArrayList<Virus>();
		turns = 0;
		immunity = 0;
		
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				hosts[row][column] = new HostCell(hostMaxHealth, hostRegeneration); //fill grid with host cells
			}
		}
		Virus firstVirus = new Virus(virusStrength, virusHealth, virusLifeTime, rows / 2, columns / 2);
		viruses.add(firstVirus);
	}
	public void step() {
		turns++;
		immunity = turns * .01;
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				hosts[row][column].regenerate(immunity);
			}
		}
		ArrayList<Virus> newViruses = new ArrayList<Virus>();
		
		for(int i = 0; i < viruses.size(); i++) {
			Virus virus = viruses.get(i);
			int oldRow = virus.getRow();
			int oldColumn = virus.getColumn();
			int[] position = findNextPosition(virus, newViruses);
			if(position != null) {
				virus.setPosition(position[0], position[1]);
			}
			int row = virus.getRow();
			int column = virus.getColumn();
			
			if(hosts[row][column].isAlive()) {
			boolean destroyed = hosts[row][column].takeDamage(virus.getStrength());
			virus.addReplicationProgress(virus.getStrength());
				if(virus.canReplicate()) {
					if(!isOccupied(oldRow, oldColumn, virus, newViruses)) {
						Virus newVirus = new Virus(virusStrength, virusHealth, virusLifeTime, oldRow, oldColumn);
						newViruses.add(newVirus);
						virus.resetReplicationProgress();
					}
				}
				if(!destroyed && hosts[row][column].isAlive()) {
					virus.takeDamage(1 + (1 * immunity));
				}
			}
			virus.ageOneTurn();
		}
		for(int i = viruses.size() - 1; i >= 0; i--) {
			if(!viruses.get(i).isAlive()) {
				viruses.remove(i);
			}
		}
		viruses.addAll(newViruses);
	}
	private int[] findNextPosition(Virus virus, ArrayList<Virus> newViruses) {
		if(virus.getBehavior() != 0) {
			int[] nextPosition = findPathPosition(virus, newViruses);
			if(nextPosition != null) {
				return nextPosition;
			}
		}
		int row = virus.getRow();
		int column = virus.getColumn();
		int[] rowChange = {-1,1,0,0};
		int[] columnChange = {0,0,-1,1};
		int startDirection = (int)(Math.random() * 4);
		for(int i = 0; i < 4; i++) {
			int direction = (startDirection + i) % 4;
			int newRow = row + rowChange[direction];
			int newColumn = column + columnChange[direction];
			if(newRow >= 0 && newRow < rows && newColumn >= 0 && newColumn < columns) {
				if(!isOccupied(newRow, newColumn, virus, newViruses)) {
					return new int[] {newRow, newColumn};
				}
			}
		}
		return null;
	}
	private boolean isOccupied(int row, int column, Virus movingVirus, ArrayList<Virus> newViruses) {
		for(int i = 0; i < viruses.size(); i++) {
			Virus other = viruses.get(i);
			if(other != movingVirus && other.getRow() == row && other.getColumn() == column) {
				return true;
			}
		}
		for(int i = 0; i < newViruses.size(); i++) {
			Virus other = newViruses.get(i);
			if(other.getRow() == row && other.getColumn() == column) {
				return true;
			}
		}
		return false;
	}
	public int getVirusCount() {
		return viruses.size();
	}
	public boolean hasVirusAt(int row, int column) {
		for(int i = 0; i < viruses.size(); i++) {
			Virus virus = viruses.get(i);
			if(virus.getRow() == row && virus.getColumn() == column) {
				return true;
			}
		}
		return false;
	}
	private int[] findPathPosition(Virus virus, ArrayList<Virus> newViruses) {
		int startRow = virus.getRow();
		int startColumn = virus.getColumn();
		boolean[][] visited = new boolean[rows][columns];
		int[][] firstRow = new int[rows][columns];
		int[][]firstColumn = new int [rows][columns];
		int[] queueRow = new int[rows * columns];
		int[] queueColumn = new int[rows * columns];
		int front = 0;
		int back = 0;
		queueRow[back] = startRow;
		queueColumn[back] = startColumn;
		back++;
		visited[startRow][startColumn] = true;
		int[] rowChange = {-1,1,0,0};
		int[] columnChange = {0,0,-1,1};
		double bestPercent = virus.getBehavior() == 2 ? Double.MAX_VALUE : -1;
		int bestRow = -1;
		int bestColumn = -1;
		while(front < back) {
			int currentRow = queueRow[front];
			int currentColumn = queueColumn[front];
			front++;
			for(int i = 0; i < 4; i++) {
				int nextRow = currentRow + rowChange[i];
				int nextColumn = currentColumn + columnChange[i];
				if(nextRow < 0 || nextRow >= rows || nextColumn < 0 || nextColumn >= columns) {
					continue;
				}
				if(visited[nextRow][nextColumn]) {
					continue;
				}
				if(isOccupied(nextRow, nextColumn, virus, newViruses)) {
					continue;
				}
				visited[nextRow][nextColumn] = true;
				if(currentRow == startRow && currentColumn == startColumn) {
					firstRow[nextRow][nextColumn] = nextRow;
					firstColumn[nextRow][nextColumn] = nextColumn;
				}
				else {
					firstRow[nextRow][nextColumn] = firstRow[currentRow][currentColumn];
					firstColumn[nextRow][nextColumn] = firstColumn[currentRow][currentColumn];
				}
				double health = hosts[nextRow][nextColumn].getHealth();
				double maxHealth = hosts[nextRow][nextColumn].getMaxHealth();
				if(health > 0) {
					double healthPercent = health / maxHealth;
					if(virus.getBehavior() == 2 && healthPercent < bestPercent) {
						bestPercent = healthPercent;
						bestRow = nextRow;
						bestColumn = nextColumn;
					}
					if(virus.getBehavior() == 1 && healthPercent > bestPercent) {
						bestPercent = healthPercent;
						bestRow = nextRow;
						bestColumn = nextColumn;
					}
				}
				queueRow[back] = nextRow;
				queueColumn[back] = nextColumn;
				back++;
			}
		}
		if(bestRow != -1) {
			return new int[] {firstRow[bestRow][bestColumn], firstColumn[bestRow][bestColumn]};
		}
		return null;
	}
	public double getHostHealthAt(int row, int column){
		return hosts[row][column].getHealth();
	}
	public int getHostMaxHealth() {
		return hostMaxHealth;
	}
	public int getDestroyedCount() {
		int count = 0;
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				if(!hosts[row][column].isAlive()) {
					count++;
				}
			}
		}
		return count;
	}
	public boolean allCellsDestroyed() {
		return getDestroyedCount() == rows * columns;
	}
	public double getImmunity() {
		return immunity;
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