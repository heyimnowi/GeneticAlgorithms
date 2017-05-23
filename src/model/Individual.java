package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Individual<T> {

	private final IndividualFactory<T> factory;
	private final List<Gene<? extends T>> genes;
	
	public Individual(IndividualFactory<T> factory, List<Gene<? extends T>> genes) {
		this.factory = factory;
		this.genes = genes;
	}
	
	public int chromosomeSize() {
		return genes.size();
	}
	
	public Gene<? extends T> getGene(int locus) {
		return genes.get(locus);
	}
	
	public List<Gene<? extends T>> getGenes() {
		return new ArrayList<>(genes);
	}

	public Individual<T> mutate(List<Gene<? extends T>> genes) {
		return factory.getIndividual(genes);
	}
	
	public abstract double getFitness();
	
	public Individual<T> clone() {
		return factory.getIndividual(genes);
	}
	
	public String toString() {
		return "Ind: [" + String.join(", ", genes.stream()
				.map(g -> g.toString())
				.collect(Collectors.toList())) + "]";
	}
}
