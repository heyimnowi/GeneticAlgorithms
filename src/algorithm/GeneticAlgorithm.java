package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import model.Couple;
import model.Individual;
import model.IndividualFactory;
import model.MutationMethod;
import model.Population;
import model.ReplacementMethod;
import model.ReproductionMethod;
import model.SelectionMethod;
import util.Props;

public class GeneticAlgorithm<T> {

	private static final int GENERATIONS = 10000; // TODO
	
	private int generation = 0;
	
	private Population<T> population;
	
	public void run(IndividualFactory<T> individualFactory) {
		createPopulation(individualFactory, Props.instance().getN());
		System.out.println(population.getIndividuals().stream()
				.mapToDouble(ind -> Math.round(ind.getFitness() * 100) / 100.0)
				.max());
		for (int i = 0; i < GENERATIONS; i++) {
//			System.out.println(population);
			replacePopulation(Props.instance().getReplacementMethod1());
		}
		System.out.println(population.getIndividuals().stream()
				.mapToDouble(ind -> Math.round(ind.getFitness() * 100) / 100.0)
				.max());
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
		default:
			throw new UnsupportedOperationException(
					"Unknown replacement method: " + replacementMethod);
		}
	}
	
	private Population<T> firstReplacementMethod(Population<T> population) {
		List<Individual<T>> newIndividuals = new ArrayList<>();
		IntStream.range(0, population.size() / 2).forEach(i -> {
			Couple<T> parents = new Couple<>(selectIndividuals(Props.instance().getSelectionMethod1(), 2));
			List<Individual<T>> children = crossIndividuals(Props.instance().getReproductionMethod(), parents).toList();
			children = mutateIndividuals(Props.instance().getMutationMethod(), children);
			newIndividuals.addAll(children);
		});
		return new Population<>(newIndividuals, generation++);
	}
	
	private Population<T> secondReplacementMethod(Population<T> population) {
		List<Individual<T>> newIndividuals = new ArrayList<>();
		List<Individual<T>> children = getChildren(population, Props.instance().getK());
		newIndividuals.addAll(children);
		List<Individual<T>> oldIndividuals = population.getIndividuals();
		Collections.shuffle(oldIndividuals);
		newIndividuals.addAll(oldIndividuals.subList(0, population.size() - children.size()));
		return new Population<>(newIndividuals, generation++);
	}
	
	private Population<T> thirdReplacementMethod(Population<T> population) {
		List<Individual<T>> newIndividuals = new ArrayList<>();		
		List<Individual<T>> children = getChildren(population, Props.instance().getK());
		List<Individual<T>> oldIndividuals = population.getIndividuals();
		Collections.shuffle(oldIndividuals);
		newIndividuals.addAll(oldIndividuals.subList(0, population.size() - children.size()));
		oldIndividuals.addAll(children);
		Collections.shuffle(oldIndividuals);
		newIndividuals.addAll(oldIndividuals.subList(0, children.size())
				.stream()
				.map(i -> i.clone())
				.collect(Collectors.toList()));
		return new Population<>(newIndividuals, generation++);
	}
	
	private List<Individual<T>> getChildren(Population<T> population, int K) {
		List<Couple<T>> parents = makeCouples(selectIndividuals(Props.instance().getSelectionMethod1(), K));
		return parents.stream()
				.map(couple -> crossIndividuals(Props.instance().getReproductionMethod(), couple).toList())
				.flatMap(List::stream)
				.map(individual -> mutateIndividual(Props.instance().getMutationMethod(), individual))
				.collect(Collectors.toList());
	}
	
	private List<Individual<T>> selectIndividuals(SelectionMethod selectionMethod, int K) {	
		switch (selectionMethod) {
		case ELITE:
			return SelectionAlgorithms.elite(population, K);	
		case BOLTZMANN:
			return SelectionAlgorithms.boltzmann(population, K);
		case RANDOM:
			return SelectionAlgorithms.random(population, K);
		case RANKING:
			return SelectionAlgorithms.ranking(population, K);
		case ROULETTE:
			return SelectionAlgorithms.roulette(population, K);
		case TOURNAMENT_DET:
			return SelectionAlgorithms.detTournament(population, K);
		case TOURNAMENT_PROB:
			return SelectionAlgorithms.probTournament(population, K);
		case UNIVERSAL:
			return SelectionAlgorithms.universal(population, K);
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
		switch (reproductionMethod) {
		case ONE_POINT:
			return ReproductionAlgorithms.onePoint(couple);
		case DOUBLE_POINT:
			return ReproductionAlgorithms.twoPoints(couple);
		case RING:
			return ReproductionAlgorithms.ring(couple);
		case UNIFORM:
			return ReproductionAlgorithms.uniform(couple);
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
			return MutationAlgorithms.singleGene(individual);
		case MULTI_GENE:
			return MutationAlgorithms.multiGene(individual);
		default:
			throw new UnsupportedOperationException(
					"Unknown mutation method: " + mutationMethod);
		}
	}
}
