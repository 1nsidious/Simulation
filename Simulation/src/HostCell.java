
public class HostCell {
	private int health; //cell hp
	private int maxHealth; // max cell hp
	private double regeneration; // cell regen
	
	public HostCell(int maxHealth, double regeneration) { //hostcell object
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.regeneration = regeneration;
	}
	public void regenerate(double immunity) { //regen cell hp
		if(health > 0 && health < maxHealth) {
			double currentRegen = regeneration +(regeneration * immunity); 
			health += currentRegen;
			if(health > maxHealth) { //unneccesary?
				health = maxHealth;
			}
		}
	}
	public boolean takeDamage(int damage) { //damage cell
		boolean wasAlive = health > 0;
		health = health - damage;
		if(health < 0) { //unneccesary?
			health = 0;
		}
		return wasAlive && health == 0;
	}
	public boolean isAlive() { // cell life check
		return health > 0;
	}
	public int getHealth() { //return cell hp
		return health;
	}
	public int getMaxHealth() {
		return maxHealth;
	}
}