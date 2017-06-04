package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Population<T> {

	private final Collection<Individual<T>> individuals;
	private final int generation;
	
	public Population(Collection<Individual<T>> individuals, int generation) {
		this.individuals = individuals;
		this.generation = generation;
	}
	
	public Population(IndividualFactory<T> factory, int N, int generation) {
		this(new ArrayList<>(), generation);
		for (int i = 0; i < N; i++) {
			individuals.add(factory.getIndividual());
		}
	}
	
	public List<Individual<T>> getIndividuals() {
		return new ArrayList<>(individuals);
	}
	
	public int size() {
		return individuals.size();
	}

	public int getGeneration() {
		return generation;
	}
	
	public String toString() {
		return "POp at : " + generation + " [" + String.join(", ", individuals.stream()
				.map(i -> i.toString())
				.collect(Collectors.toList())) + "]";
	}
}
