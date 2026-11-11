
public class HostCell {
	private int health; //cell hp
	private int maxHealth; // max cell hp
	private int regeneration; // cell regen
	
	public HostCell(int maxHealth, int regeneration) { //hostcell object
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.regeneration = regeneration;
	}
	public void regenerate() { //regen cell hp
		if(health < maxHealth) {
			health = health + regeneration;
			if(health > maxHealth) { //unneccesary?
				health = maxHealth;
			}
		}
	}
	public void takeDamage(int damage) { //damage cell
		health = health - damage;
		if(health < 0) { //unneccesary?
			health = 0;
		}
	}
	public boolean isAlive() { // cell life check
		return health > 0;
	}
	public int getHealth() { //return cell hp
		return health;
	}

}