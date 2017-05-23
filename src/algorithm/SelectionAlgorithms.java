package algorithm;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.Individual;
import model.Population;

public class SelectionAlgorithms {

	private static <T> Map<Individual<T>, Double> getFitnessMap(Population<T> population) {
		return population.getIndividuals()
				.stream()
				.collect(Collectors.toMap(x -> x, Individual::getFitness));
	}
	
	@SuppressWarnings("unused")
	private static <T> Map<Individual<T>, Double> getRelativeFitnessMap(Population<T> population) {
		Map<Individual<T>, Double> fitnessMap = getFitnessMap(population);
		double fitnessTotal = fitnessMap.values().stream().collect(Collectors.summingDouble(x -> x));
		return fitnessMap.entrySet()
				.stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue() / fitnessTotal));
	}
	
	public static <T> List<Individual<T>> elite(Population<T> population, int K) {
		return getFitnessMap(population).entrySet()
	        .stream()
	        .sorted(Map.Entry.comparingByValue())
	        .map(Map.Entry::getKey)
	        .limit(K)
			.collect(Collectors.toList());
	}
}
