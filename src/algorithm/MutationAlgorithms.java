package algorithm;

import java.util.ArrayList;
import java.util.List;

import model.Gene;
import model.Individual;
import util.Props;
import util.RandomUtils;

public class MutationAlgorithms {

	private static final double p = Props.instance().getMutationP();
	
	public <T> Individual<T> singleGene(Individual<T> individual) {
		List<Gene<? extends T>> newGenes = new ArrayList<>();
		newGenes.addAll(individual.getGenes());
		if (RandomUtils.instance().nextDouble() < p) {
			int locus = RandomUtils.instance().nextInt(individual.chromosomeSize());
			newGenes.set(locus, individual.getGenes().get(locus).mutate());
		}
		return individual.mutate(newGenes);
	}
	
	public <T> Individual<T> multiGene(Individual<T> individual) {
		return mutateGenes(individual, false);
	}
	
	private <T> Individual<T> mutateGenes(Individual<T> individual, boolean singleGene) {
		List<Gene<? extends T>> newGenes = new ArrayList<>();
		newGenes.addAll(individual.getGenes());
		for (int i = 0; i < individual.getGenes().size(); i++) {
			if (RandomUtils.instance().nextDouble() < p) {
				newGenes.set(i, individual.getGenes().get(i).mutate());
			}
		}
		return individual.mutate(newGenes);
	}
}
