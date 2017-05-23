package model;

import java.util.ArrayList;
import java.util.Collection;

public class Population<T> {

	private final Collection<Individual<T>> individuals;
	
	public Population(Collection<Individual<T>> individuals) {
		this.individuals = individuals;
	}
	
	public Population(IndividualFactory<T> factory, int N) {
		this(new ArrayList<>());
		for (int i = 0; i < N; i++) {
			individuals.add(factory.getIndividual());
		}
	}
	
	public Collection<Individual<T>> getIndividuals() {
		return new ArrayList<>(individuals);
	}
	
	public int size() {
		return individuals.size();
	}
}
