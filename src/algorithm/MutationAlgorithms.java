package algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Gene;
import model.Individual;

public class MutationAlgorithms {

	private static final double p = 0; // TODO
	
	public static <T> void singleGene(Individual<T> individual) {
		List<Gene<? extends T>> newGenes = new ArrayList<>();
		newGenes.addAll(individual.getGenes());
		for (int i = 0; i < individual.getGenes().size(); i++) {
			if (new Random().nextDouble() < p) {
				newGenes.set(i, individual.getGenes().get(i).mutate());
				break;
			}
		}
		individual.mutate(newGenes);
	}
}
