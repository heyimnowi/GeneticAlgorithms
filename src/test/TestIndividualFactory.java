package test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import model.Gene;
import model.Individual;
import model.IndividualFactory;

public class TestIndividualFactory implements IndividualFactory<Integer> {

	private int i = 0;
	
	private final static List<List<Integer>> INDIVIDUALS = Arrays.asList(
			Arrays.asList(1, 2, 3), 
			Arrays.asList(2, 3, 4),
			Arrays.asList(3, 4, 5),
			Arrays.asList(4, 5, 6),
			Arrays.asList(5, 6, 7)
	);
	
	@Override
	public Individual<Integer> getIndividual() {
		return getIndividual(convertToGenes(INDIVIDUALS.get(i++)));
	}

	@Override
	public Individual<Integer> getIndividual(List<Gene<? extends Integer>> genes) {
		return new Individual<Integer>(this, genes) {	
			@Override
			public double getFitness() {
				return getGenes().stream().mapToInt(g -> (int) g.getAlele()).sum();
			}
		};
	}
	
	private List<Gene<? extends Integer>> convertToGenes(List<Integer> geneValues) {
		return geneValues.stream()
			.map(i -> new Gene<>(new IntegerAleles(), i))
			.collect(Collectors.toList());
	}
}