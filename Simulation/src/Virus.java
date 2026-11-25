
public class Virus {
	private int strength; // virus damage
	private double health;
	private int lifeTime;
	private int row;
	private int column;
	private int behavior; //0=random 1=high hp 2=low hp
	private int replicationProgress;
	private int replicationThreshold;
	
	public Virus(int strength, int health, int lifeTime, int row, int column) {
		this.strength = strength;
		this.health = health;
		this.lifeTime = lifeTime;
		this.row = row;
		this.column = column;
		
		behavior = (int)(Math.random() * 3);
		replicationProgress = 0;
		replicationThreshold = 12;
	}
	public void takeDamage(double damage) {
		health = health - damage;
		if (health < 0) {
			health = 0;
		}
	}
	public void ageOneTurn() {
		lifeTime--;
	}
	public boolean isAlive() {
		return health > 0 && lifeTime > 0;
	}
	public int getStrength() {
		return strength;
	}
	public double getHealth() {
		return health;
	}
	public int getLifeTime() {
		return lifeTime;
	}
	public int getRow() {
		return row;
	}
	public int getColumn() {
		return column;
	}
	public int getBehavior() {
		return behavior;
	}
	public void setPosition(int row, int column){
		this.row = row;
		this.column = column;
	}
	public void addReplicationProgress(int damage) {
		replicationProgress += damage;
	}
	public boolean canReplicate() {
		return replicationProgress >= replicationThreshold;
	}
	public void resetReplicationProgress() {
		replicationProgress = 0;
	}
}