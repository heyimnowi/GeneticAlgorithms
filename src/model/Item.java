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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(agility);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(experience);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(force);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(health);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + id;
		temp = Double.doubleToLongBits(resistance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (Double.doubleToLongBits(agility) != Double
				.doubleToLongBits(other.agility))
			return false;
		if (Double.doubleToLongBits(experience) != Double
				.doubleToLongBits(other.experience))
			return false;
		if (Double.doubleToLongBits(force) != Double
				.doubleToLongBits(other.force))
			return false;
		if (Double.doubleToLongBits(health) != Double
				.doubleToLongBits(other.health))
			return false;
		if (id != other.id)
			return false;
		if (Double.doubleToLongBits(resistance) != Double
				.doubleToLongBits(other.resistance))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ((Props.instance().debug()) ? 
					(", force=" + force + ", agility=" + agility
					+ ", experience=" + experience + ", resistance=" + resistance
					+ ", health=" + health) : "") 
				+ "]";
	}
}	
