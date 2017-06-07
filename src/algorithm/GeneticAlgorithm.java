package algorithm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import model.Couple;
import model.EndingMethod;
import model.Individual;
import model.IndividualFactory;
import model.MutationMethod;
import model.Population;
import model.ReplacementMethod;
import model.ReproductionMethod;
import model.SelectionMethod;
import util.Props;
import util.RandomUtils;

public class GeneticAlgorithm<T> {

	private int generation = 0;
	private double maxFitness = 0;
	
	private Population<T> population;
	private SelectionAlgorithms selectionAlgorithms = new SelectionAlgorithms();
	private ReproductionAlgorithms reproductionAlgorithms = new ReproductionAlgorithms();
	private MutationAlgorithms mutationAlgorithms = new MutationAlgorithms();
	private EndingAlgorithms endingAlgorithms = new EndingAlgorithms();
	
	public void run(IndividualFactory<T> individualFactory) {
		AtomicBoolean stop = new AtomicBoolean(false);
		Thread th = new Thread(() -> {
			Scanner sc = new Scanner(System.in);
			System.out.println("Reading...");
			while (sc.hasNext() && !stop.get()) {
				if (sc.nextLine().equals("STOP")) {
					stop.set(true);
					break;
				}
			}
			sc.close();
		});
		th.start();
		try{
			PrintWriter writer = new PrintWriter("fitness.csv", "UTF-8");
			generation = 0;
			createPopulation(individualFactory, Props.instance().getN());
			System.out.println(Math.round(1000 * endingAlgorithms.getMaxFitness(population)) / 1000.0);
			while (!shouldEnd() && !stop.get()) {
				writer.println(endingAlgorithms.getMaxFitness(population));
				if (generation % 200 == 0) {
					System.out.println("Generation: " + generation);
					System.out.println(Math.round(1000 * endingAlgorithms.getMaxFitness(population)) / 1000.0);
				}
				replacePopulation(Props.instance().getReplacementMethod());
			}
			System.out.println("Generation: " + (generation - 1));
			maxFitness = endingAlgorithms.getMaxFitness(population);
			System.out.println("Max Fitness: " + Math.round(1000 * maxFitness) / 1000.0);
			System.out.println(population.getIndividuals().stream()
				.filter(ind -> ind.getFitness() == maxFitness)
					.collect(Collectors.toSet()));
			writer.close();
		} catch (IOException e) {
			System.out.println("Error writing fitness");
		}
		stop.set(true);
	}
	
	private void createPopulation(IndividualFactory<T> individualFactory, int N) {
		this.population = new Population<>(individualFactory, N, generation++);
	}
	
	private void replacePopulation(ReplacementMethod replacementMethod) {
		switch (replacementMethod) {
		case METHOD_1:
			this.population = firstReplacementMethod(population);
			break;
		case METHOD_2:
			this.population = secondReplacementMethod(population);
			break;
		case METHOD_3:
			this.population = thirdReplacementMethod(population);
			break;
		case GENERATIONAL_GAP:
			this.population = generationalGap(population, Props.instance().getG());
			break;
		default:
			throw new UnsupportedOperationException(
					"Unknown replacement method: " + replacementMethod);
		}
	}
	
	private Population<T> firstReplacementMethod(Population<T> population) {
		List<Individual<T>> newIndividuals = new ArrayList<>();
		IntStream.range(0, population.size() / 2).forEach(i -> {
			SelectionMethod selectionMethod = i < (population.size() / 2) * Props.instance().getSelectionMethodP() ?
					Props.instance().getSelectionMethod1() : Props.instance().getSelectionMethod2(); 
			Couple<T> parents = new Couple<>(selectIndividuals(population, selectionMethod, 2));
			List<Individual<T>> children = crossIndividuals(Props.instance().getReproductionMethod(), parents).toList();
			children = mutateIndividuals(Props.instance().getMutationMethod(), children);
			newIndividuals.addAll(children);
		});
		return new Population<>(newIndividuals, generation++);
	}
	
	private Population<T> secondReplacementMethod(Population<T> population) {
		return generationalGap(population, Props.instance().getK() / (double) population.size());
	}
	
	private Population<T> thirdReplacementMethod(Population<T> population) {
		List<Individual<T>> newIndividuals = new ArrayList<>();		
		List<Individual<T>> children = getChildren(population, Props.instance().getK());
		List<Individual<T>> oldIndividuals = population.getIndividuals();
		newIndividuals.addAll(clone(selectIndividuals(new Population<T>(oldIndividuals, generation),
			Props.instance().getReplacementMethodP(), population.size() - children.size())));
		oldIndividuals.addAll(children);
		oldIndividuals = oldIndividuals.stream()
			.map(i -> i.clone())
			.collect(Collectors.toList());
		newIndividuals.addAll(clone(selectIndividuals(new Population<T>(oldIndividuals, generation),
			Props.instance().getReplacementMethodP(), children.size())));
		return new Population<>(newIndividuals, generation++);
	}
	
	private Population<T> generationalGap(Population<T> population, double G) {
		int k = (int) Math.round(G * population.size());
		List<Individual<T>> newIndividuals = new ArrayList<>();		
		List<Individual<T>> children = getChildren(population, k);
		newIndividuals.addAll(children);
		List<Individual<T>> oldIndividuals = population.getIndividuals();
		newIndividuals.addAll(clone(selectIndividuals(new Population<T>(oldIndividuals, generation),
			Props.instance().getReplacementMethodP(), population.size() - children.size())));
		return new Population<>(newIndividuals, generation++);
	}
	
	private List<Individual<T>> getChildren(Population<T> population, int K) {
		List<Couple<T>> parents = makeCouples(selectIndividuals(population, Props.instance().getSelectionMethodP(), K));
		return parents.stream()
				.map(couple -> crossIndividuals(Props.instance().getReproductionMethod(), couple).toList())
				.flatMap(List::stream)
				.map(individual -> mutateIndividual(Props.instance().getMutationMethod(), individual))
				.collect(Collectors.toList());
	}
	
	private List<Individual<T>> selectIndividuals(Population<T> population, double p, int K) {	
		int k1 = (int) (K * p);
		int k2 = K - k1;
		List<Individual<T>> selected = new ArrayList<>();
		selected.addAll(selectIndividuals(population, Props.instance().getSelectionMethod1(), k1));
		selected.addAll(selectIndividuals(population, Props.instance().getSelectionMethod2(), k2));
		return selected;
	}
	
	private List<Individual<T>> selectIndividuals(Population<T> population, SelectionMethod selectionMethod, int K) {	
		switch (selectionMethod) {
		case ELITE:
			return selectionAlgorithms.elite(population, K);	
		case BOLTZMANN:
			return selectionAlgorithms.boltzmann(population, K);
		case RANDOM:
			return selectionAlgorithms.random(population, K);
		case RANKING:
			return selectionAlgorithms.ranking(population, K);
		case ROULETTE:
			return selectionAlgorithms.roulette(population, K);
		case TOURNAMENT_DET:
			return selectionAlgorithms.detTournament(population, K);
		case TOURNAMENT_PROB:
			return selectionAlgorithms.probTournament(population, K);
		case UNIVERSAL:
			return selectionAlgorithms.universal(population, K);
		default:
			throw new UnsupportedOperationException(
					"Unknown selection method: " + selectionMethod);
		}
	}
	
	private List<Couple<T>> makeCouples(List<Individual<T>> individuals) {
		Collections.shuffle(individuals);
		List<Couple<T>> couples = new ArrayList<>();
		for (int i = 0; i < individuals.size(); i += 2) {
			couples.add(new Couple<>(individuals.get(i), individuals.get((i + 1) % individuals.size())));
		}
		return couples;
	}
	
	private Couple<T> crossIndividuals(ReproductionMethod reproductionMethod, Couple<T> couple) {
		if (RandomUtils.instance().nextDouble() > Props.instance().getCrossoverP()) {
			return couple.clone();
		}
		switch (reproductionMethod) {
		case ONE_POINT:
			return reproductionAlgorithms.onePoint(couple);
		case DOUBLE_POINT:
			return reproductionAlgorithms.twoPoints(couple);
		case RING:
			return reproductionAlgorithms.ring(couple);
		case UNIFORM:
			return reproductionAlgorithms.uniform(couple);
		default:
			throw new UnsupportedOperationException(
					"Unknown reproduction method: " + reproductionMethod);
		}
	}
	
	private List<Individual<T>> mutateIndividuals(MutationMethod mutationMethod, List<Individual<T>> individuals) {
		return individuals.stream()
				.map(individual -> this.mutateIndividual(mutationMethod, individual))
				.collect(Collectors.toList());
	}
	
	private Individual<T> mutateIndividual(MutationMethod mutationMethod, Individual<T> individual) {
		switch (mutationMethod) {
		case SINGLE_GENE:
			return mutationAlgorithms.singleGene(individual);
		case MULTI_GENE:
			return mutationAlgorithms.multiGene(individual);
		default:
			throw new UnsupportedOperationException(
					"Unknown mutation method: " + mutationMethod);
		}
	}
	
	private boolean shouldEnd() {
		boolean shouldEnd = false;
		for (EndingMethod endingMethod : Props.instance().getEndingMethods()) {
			switch (endingMethod) {
			case CONTENT:
				shouldEnd = shouldEnd || endingAlgorithms.content(population);
				break;
			case FITNESS_MIN:
				shouldEnd = shouldEnd || endingAlgorithms.fitnessMin(population);
				break;
			case MAX_GENERATIONS:
				shouldEnd = shouldEnd || endingAlgorithms.maxGenerations(population);
				break;
			case STRUCTURE:
				shouldEnd = shouldEnd || endingAlgorithms.structure(population);
				break;
			default:
				throw new UnsupportedOperationException(
						"Unknown ending method: " + endingMethod);
			}
		}
		return shouldEnd;
	}
	
	private List<Individual<T>> clone(List<Individual<T>> list) {
		return list.stream()
				.map(ind -> ind.clone())
				.collect(Collectors.toList());
	}

	/**
	 * @return the generation
	 */
	public int getGeneration() {
		return generation;
	}

	/**
	 * @param generation the generation to set
	 */
	public void setGeneration(int generation) {
		this.generation = generation;
	}

	public double getMaxFitness() {
		return maxFitness;
	}

	public void setMaxFitness(double maxFitness) {
		this.maxFitness = maxFitness;
	}
}
