package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

public class GeneticAlgorithm<T> {

	private int generation = 0;
	
	private Population<T> population;
	
	public void run(IndividualFactory<T> individualFactory) {
		createPopulation(individualFactory, Props.instance().getN());
		System.out.println(Math.round(100 * EndingAlgorithms.getMaxFitness(population)) / 100.0);
		while (!shouldEnd()) {
			if (generation % 100 == 0) {
				System.out.println("Generation: " + generation);
				System.out.println(Math.round(100 * EndingAlgorithms.getMaxFitness(population)) / 100.0);
	//			System.out.println(population);
			}
			replacePopulation(Props.instance().getReplacementMethod());
		}
		System.out.println(Math.round(100 * EndingAlgorithms.getMaxFitness(population)) / 100.0);
		System.out.println(population.getIndividuals().stream()
				.filter(ind -> ind.getFitness() == EndingAlgorithms.getMaxFitness(population))
				.collect(Collectors.toList()));
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
		List<Individual<T>> newIndividuals = new ArrayList<>();
		List<Individual<T>> children = getChildren(population, Props.instance().getK());
		newIndividuals.addAll(children);
		List<Individual<T>> oldIndividuals = population.getIndividuals();
		newIndividuals.addAll(clone(selectIndividuals(new Population<T>(oldIndividuals, generation),
			Props.instance().getReplacementMethodP(), population.size() - children.size())));
		return new Population<>(newIndividuals, generation++);
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
	
	private boolean shouldEnd() {
		boolean shouldEnd = false;
		for (EndingMethod endingMethod : Props.instance().getEndingMethods()) {
			switch (endingMethod) {
			case CONTENT:
				shouldEnd = shouldEnd || EndingAlgorithms.content(population);
				break;
			case FITNESS_MIN:
				shouldEnd = shouldEnd || EndingAlgorithms.fitnessMin(population);
				break;
			case MAX_GENERATIONS:
				shouldEnd = shouldEnd || EndingAlgorithms.maxGenerations(population);
				break;
			case STRUCTURE:
				shouldEnd = shouldEnd || EndingAlgorithms.structure(population);
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
}
