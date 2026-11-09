
public class Agent {
	private int x;
	private int y;
	private int collectedDna;
	
	public Agent(int startX, int startY) {
		this.x = startX;
		this.y = startY;
		this.collectedDna = 0;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void move(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}
	public void collectDna(int amount) {
		this.collectedDna += amount;
	}
	public int getCollectedDna() {
		return collectedDna;
	}
}