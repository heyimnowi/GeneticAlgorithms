package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import util.Props;
import model.Individual;
import model.Population;

public class SelectionAlgorithms {
	
	private static final double tempDuration = Props.instance().getTempDuration();
	private static final double minTemp = Props.instance().getMinTemp();
	private static final double initialTemp = Props.instance().getInitialTemp();
	private static final int M = Props.instance().getM();
	
	public static <T> List<Individual<T>> elite(Population<T> population, int K) {
		return getFitnessMap(population).entrySet()
	        .stream()
	        .sorted(Comparator.comparingDouble((Map.Entry<Individual<T>, Double> e) -> e.getValue()).reversed())
	        .map(Map.Entry::getKey)
	        .limit(K)
			.collect(Collectors.toList());
	}
	
	public static <T> List<Individual<T>> random(Population<T> population, int K) {
		List<Individual<T>> individuals = population.getIndividuals();
		return IntStream.range(0, K)
			.boxed()
			.map(i -> individuals.get(new Random().nextInt(individuals.size())))
			.collect(Collectors.toList());
	}
	
	public static <T> List<Individual<T>> roulette(Population<T> population, int K) {
		return roulette(getRelativeFitnessMap(population), K);
	}
	
	private static <T> List<Individual<T>> roulette(Map<Individual<T>, Double> relativeFitnessMap, int K) {
		double[] rands = new double[K];
		for (int i = 0; i < K; i++) {
			rands[i] = new Random().nextDouble();
		}
		return selectWithRandoms(relativeFitnessMap, K, rands);
	}
	
	public static <T> List<Individual<T>> universal(Population<T> population, int K) {
		double[] rands = new double[K];
		double r = new Random().nextDouble();
		for (int i = 1; i <= K; i++) {
			rands[i - 1] = (r + i - 1) / K;
		}
		return selectWithRandoms(getRelativeFitnessMap(population), K, rands);
	}
	
	public static <T> List<Individual<T>> boltzmann(Population<T> population, int K) {
		double T = getTemperature(population.getGeneration());
		Map<Individual<T>, Double> expFitnessT = getFitnessMap(population).entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> Math.exp(e.getValue()/ T)));
		double avgExpFitnessT = expFitnessT.entrySet().stream()
				.mapToDouble(Map.Entry::getValue)
				.sum();
		Map<Individual<T>, Double> expectedLife = expFitnessT.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() / avgExpFitnessT));
		return roulette(expectedLife, K);
	}
	
	public static <T> List<Individual<T>> detTournament(Population<T> population, int K) {
		List<Individual<T>> individuals = population.getIndividuals();
		List<Individual<T>> newIndividuals = new ArrayList<>();
		for (int i = 0; i < K; i++) {
			Collections.shuffle(individuals);
			List<Individual<T>> randomSubset = individuals.subList(0, M);
			randomSubset.sort(Utils.getFitnessComparator());
			newIndividuals.add(randomSubset.get(0));
		}
		return newIndividuals;
	}
	
	public static <T> List<Individual<T>> probTournament(Population<T> population, int K) {
		List<Individual<T>> individuals = population.getIndividuals();
		List<Individual<T>> newIndividuals = new ArrayList<>();
		for (int i = 0; i < K; i++) {
			Collections.shuffle(individuals);
			if (individuals.get(0).getFitness() > individuals.get(1).getFitness() &&
					new Random().nextDouble() < 0.75) {
				newIndividuals.add(individuals.get(0));
			} else {
				newIndividuals.add(individuals.get(1));
			}
		}
		return newIndividuals;
	}
	
	public static <T> List<Individual<T>> ranking(Population<T> population, int K) {
		List<Individual<T>> individuals = population.getIndividuals();
		individuals.sort(Utils.getFitnessComparator());
		double n = IntStream.range(1, individuals.size() + 1).sum();
		Map<Individual<T>, Double> rankingProbability = IntStream.range(0, individuals.size())
			.boxed()
			.collect(Collectors.toMap((Integer i) -> individuals.get(i), (Integer i) -> (individuals.size() - i) / n));
		return roulette(rankingProbability, K);
	}
	
	private static <T> List<Individual<T>> selectWithRandoms(Map<Individual<T>, Double> relativeFitnessMap, 
			int K, double[] rands) {
		Arrays.sort(rands);
		List<Individual<T>> newIndividuals = new ArrayList<>();
		for (double rand : rands) {
			List<Entry<Individual<T>, Double>> entryList = relativeFitnessMap.entrySet().stream()
					.collect(Collectors.toList());
			double acum = entryList.get(0).getValue();
			if (rand < acum) {
				newIndividuals.add(entryList.get(0).getKey());
			}
			for (int i = 1; i < entryList.size(); i++) {				
				if (acum <= rand && rand < (acum + entryList.get(i).getValue())) {
					newIndividuals.add(entryList.get(i).getKey());
				}
				acum += entryList.get(i).getValue();
			}
		}
		return newIndividuals;
	}
	
	private static <T> Map<Individual<T>, Double> getFitnessMap(Population<T> population) {
		return population.getIndividuals()
				.stream()
				.collect(Collectors.toMap(x -> x, Individual::getFitness));
	}
	
	private static <T> Map<Individual<T>, Double> getRelativeFitnessMap(Population<T> population) {
		Map<Individual<T>, Double> fitnessMap = getFitnessMap(population);
		double fitnessTotal = fitnessMap.values().stream().collect(Collectors.summingDouble(x -> x));
		return fitnessMap.entrySet()
				.stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue() / fitnessTotal));
	}
	
	private static double getTemperature(int t) {
		return Math.exp((-1 / tempDuration) * t
					+ Math.log(Math.exp((1 / tempDuration)) * (initialTemp - minTemp)))
				+ minTemp;
	}
}
