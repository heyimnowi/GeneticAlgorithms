package algorithm;

import java.util.ArrayList;
import java.util.List;

import model.Couple;
import model.Individual;
import model.IndividualFactory;
import model.MutationMethod;
import model.Population;
import model.ReplacementMethod;
import model.ReproductionMethod;
import model.SelectionMethod;

public class GeneticAlgorithm<T> {

	// TODO
	private static final SelectionMethod SELECTION_METHOD = SelectionMethod.ELITE;
	private static final ReproductionMethod REPRODUCTION_METHOD = ReproductionMethod.ONE_POINT;
	private static final MutationMethod MUTATION_METHOD = MutationMethod.SINGLE_GENE;
	private static final ReplacementMethod REPLACEMENT_METHOD = ReplacementMethod.METHOD_1;
	private static final int N = 0;
	private static final int GENERATIONS = 0;
	
	private Population<T> population;
	
	public void run(IndividualFactory<T> individualFactory) {
		createPopulation(individualFactory, N);
		for (int i = 0; i < GENERATIONS; i++) {
			replacePopulation(REPLACEMENT_METHOD);
		}
	}
	
	private void createPopulation(IndividualFactory<T> individualFactory, int N) {
		this.population = new Population<>(individualFactory, N);
	}
	
	private void replacePopulation(ReplacementMethod replacementMethod) {
		switch (replacementMethod) {
		case METHOD_1:
			this.population = firstReplacementMethod(this.population);
		default:
			throw new UnsupportedOperationException(
					"Unknown replacement method: " + replacementMethod);
		}
	}
	
	private Population<T> firstReplacementMethod(Population<T> population) {
		List<Individual<T>> newIndividuals = new ArrayList<>();
		for (int i = 0; i < population.size() / 2; i++) {
			Couple<T> parents = new Couple<>(selectIndividuals(SELECTION_METHOD, 2));
			List<Individual<T>> children = crossIndividuals(REPRODUCTION_METHOD, parents).toList();
			mutateIndividuals(MUTATION_METHOD, children);
			newIndividuals.addAll(children);
		}
		return new Population<>(newIndividuals);
	}
	
	private List<Individual<T>> selectIndividuals(SelectionMethod selectionMethod, int K) {	
		switch (selectionMethod) {
		case ELITE:
			return SelectionAlgorithms.elite(this.population, K);
		default:
			throw new UnsupportedOperationException(
					"Unknown selection method: " + selectionMethod);
		}
	}
	
	@SuppressWarnings("unused")
	private List<Couple<T>> makeCouples(List<Individual<T>> individuals) {
		List<Couple<T>> couples = new ArrayList<>();
		for (int i = 0; i < individuals.size(); i += 2) {
			couples.add(new Couple<>(individuals.get(0), individuals.get(1)));
		}
		return couples;
	}
	
	private Couple<T> crossIndividuals(ReproductionMethod reproductionMethod, Couple<T> couple) {
		switch (reproductionMethod) {
		case ONE_POINT:
			return ReproductionAlgorithms.onePoint(couple);
		default:
			throw new UnsupportedOperationException(
					"Unknown reproduction method: " + reproductionMethod);
		}
	}
	
	private void mutateIndividuals(MutationMethod mutationMethod, List<Individual<T>> individuals) {
		for (Individual<T> individual : individuals) {
			this.mutateIndividual(mutationMethod, individual);
		}
	}
	
	private void mutateIndividual(MutationMethod mutationMethod, Individual<T> individual) {
		switch (mutationMethod) {
		case SINGLE_GENE:
			MutationAlgorithms.singleGene(individual);
		default:
			throw new UnsupportedOperationException(
					"Unknown mutation method: " + mutationMethod);
		}
	}
}
