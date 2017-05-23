package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Individual<T> {

	private List<Gene<? extends T>> genes;
	
	public Individual(List<Gene<? extends T>> genes) {
		this.genes = genes;
	}
	
	public List<Gene<? extends T>> getGenes() {
		return new ArrayList<>(genes);
	}

	public void mutate(List<Gene<? extends T>> genes) {
		this.genes = genes;
	}
	
	public abstract double getFitness();
}
