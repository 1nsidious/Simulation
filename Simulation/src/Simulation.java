import java.util.ArrayList;

public class Simulation {
	private Tile[][] grid;
	private ArrayList<Agent> agents;
	private int width;
	private int height;
	private int dnaMulti;
	
	public Simulation(int width, int height, int dnaMulti) {
		this.width = width;
		this.height = height;
		this.dnaMulti = dnaMulti;
		this.grid = new Tile[width][height];
		this.agents = new ArrayList<>();
		setupHostCells();
	}
	private void setupHostCells() {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int xDistance = x - 10;
				if(xDistance < 0) {
					xDistance = -xDistance;
				}
				int yDistance = y - 10;
				if(yDistance < 0) {
					yDistance = -yDistance;
				}
				int distance = xDistance + yDistance;
				int initialDna = 20 - distance;
				if(initialDna < 2) {
					initialDna = 2;
				}
				grid[x][y] = new Tile(x,y,initialDna);
			
			}
		}
	}
	public void addAgent(Agent agent) {
		agents.add(agent);
	}
	public void step() {
		for(int i = 0; i < agents.size(); i++) {
			Agent agent = agents.get(i);
			int agentX = agent.getX();
			int agentY = agent.getY();
			
			Tile currentCell = grid[agent.getX()][agent.getY()];
			if(currentCell.getDnaAmount() > 0) {
				int baseExtract = 1;
				int finalExtract = baseExtract * dnaMulti;
				currentCell.extractDna(finalExtract);
				agent.collectDna(finalExtract);
			}
			int bestX = agentX;
			int bestY = agentY;
			int maxDna = currentCell.getDnaAmount();
			for(int ax = -1; ax <= 1; ax++) {
				for(int ay = -1; ay <= 1; ay++) {
					int targetX = agentX + ax;
					int targetY = agentY + ay;
					if(targetX >= 0 && targetX < width && targetY >= 0 && targetY < height) {
						int neighborDna = grid[targetX][targetY].getDnaAmount();
						if(neighborDna > maxDna) {
							maxDna = neighborDna;
							bestX = targetX;
							bestY = targetY;
						}
					}
				}
			}
			agent.move(bestX, bestY);
		}
	}
	public Tile[][] getGrid(){
		return grid;
	}
	public ArrayList<Agent> getAgents(){
		return agents;
	}
}