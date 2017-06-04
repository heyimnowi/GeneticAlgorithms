package model;

import java.util.Arrays;
import java.util.List;

public class Couple<T> {

	private final Individual<T> individual1;
	private final Individual<T> individual2;
	
	public Couple(List<Individual<T>> individuals) {
		if (individuals.size() != 2) {
			throw new IllegalArgumentException("A couple should be made of only 2 individuals.");
		}
		this.individual1 = individuals.get(0);
		this.individual2 = individuals.get(1);
	}
	
	public Couple(Individual<T> individual1, Individual<T> individual2) {
		super();
		this.individual1 = individual1;
		this.individual2 = individual2;
	}
	
	public int chromosomeSize() {
		return individual1.chromosomeSize();
	}

	public Individual<T> getIndividual1() {
		return individual1;
	}

	public Individual<T> getIndividual2() {
		return individual2;
	}
	
	public List<Individual<T>> toList() {
		return Arrays.asList(individual1, individual2);
	}
	
	public Couple<T> clone() {
		return new Couple<>(individual1.clone(), individual2.clone());
	}
	
	@Override
	public String toString() {
		return "Couple: (" + individual1 + ", " + individual2 + ")";
	}
}
