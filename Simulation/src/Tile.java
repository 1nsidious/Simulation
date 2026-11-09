
public class Tile {
	private int x;
	private int y;
	private int dnaAmount;
	
	public Tile(int startX, int startY, int initialDna) {
		this.x = startX;
		this.y = startY;
		this.dnaAmount = initialDna;
	}
	public int getDnaAmount() {
		return dnaAmount;
	}
	public void extractDna(int amount) {
		this.dnaAmount -= amount;
		
		if(this.dnaAmount < 0) {
			this.dnaAmount = 0;
		}
	}
	public void regenerateDna(int amount) {
		this.dnaAmount += amount;
	}
}