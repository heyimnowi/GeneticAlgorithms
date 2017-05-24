package model;

import util.Props;

public class Item {

	private final int id;
	private final double force;
	private final double agility;
	private final double experience;
	private final double resistance;
	private final double health;
	
	public Item(int id, double force, double agility, double experience,
			double resistance, double health) {
		super();
		this.id = id;
		this.force = force;
		this.agility = agility;
		this.experience = experience;
		this.resistance = resistance;
		this.health = health;
	}

	public double getId() {
		return id;
	}

	public double getForce() {
		return force;
	}

	public double getAgility() {
		return agility;
	}

	public double getExperience() {
		return experience;
	}

	public double getResistance() {
		return resistance;
	}

	public double getHealth() {
		return health;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Item [id=" + id + ((Props.instance().debug()) ? 
					(", force=" + force + ", agility=" + agility
					+ ", experience=" + experience + ", resistance=" + resistance
					+ ", health=" + health) : "") 
				+ "]";
	}
}	
