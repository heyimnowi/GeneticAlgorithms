package algorithm;

import java.util.HashMap;
import java.util.Map;

import util.Props;
import model.Individual;
import model.Population;

public class EndingAlgorithms {
	
	private Map<IndividualWrapper<?>, Integer> prevIndividualsMap;
	private int contentCount = 0;
	private Double prevMaxFitness = null;
	
	public <T> boolean maxGenerations(Population<T> population) {
		return population.getGeneration() >= Props.instance().getMaxGenerations();
	}
	
	public <T> boolean fitnessMin(Population<T> population) {
		return getMaxFitness(population) >= Props.instance().getFitnessMin();
	}
	
	public <T> boolean structure(Population<T> population) {
		Map<IndividualWrapper<?>, Integer> individualsMap = new HashMap<>();
		for (Individual<T> ind: population.getIndividuals()) {
			IndividualWrapper<?> wrapped = new IndividualWrapper<>(ind);
			individualsMap.putIfAbsent(wrapped, 0);
			individualsMap.put(wrapped, individualsMap.get(wrapped) + 1);
		}
		if (prevIndividualsMap == null) {
			prevIndividualsMap = individualsMap;
			return false;
		}
		int acum = 0;
		for (Individual<T> ind: population.getIndividuals()) {
			IndividualWrapper<?> wrapped = new IndividualWrapper<>(ind);
			acum += Math.min(individualsMap.getOrDefault(wrapped, 0), prevIndividualsMap.getOrDefault(wrapped, 0));
		}
		prevIndividualsMap = individualsMap;
		return acum / (double) population.size() >= Props.instance().getStructure();
	}
	
	public <T> boolean content(Population<T> population) {
		double maxFitness = getMaxFitness(population);
		if (prevMaxFitness != null && maxFitness == prevMaxFitness) {
			contentCount++;
		} else {
			contentCount = 0;
			prevMaxFitness = maxFitness;
		}
		return contentCount >= Props.instance().getContent();
	}
	
	<T> double getMaxFitness(Population<T> population) {
		return population.getIndividuals().stream()
				.mapToDouble(ind -> ind.getFitness())
				.max()
				.orElseThrow(() -> new IllegalStateException("Population is empty."));
	}
	
	private class IndividualWrapper<T> {
		private final Individual<T> individual;

		public IndividualWrapper(Individual<T> individual) {
			super();
			this.individual = individual;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((individual.getGenes() == null) ? 0 : individual.getGenes().hashCode());
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
			Individual<?> other = ((IndividualWrapper<?>) obj).individual;
			if (individual.getGenes() == null) {
				if (other.getGenes() != null)
					return false;
			} else if (!individual.getGenes().equals(other.getGenes()))
				return false;
			return true;
		}	
	}
}
