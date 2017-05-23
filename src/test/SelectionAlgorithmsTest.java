package test;

import algorithm.GeneticAlgorithm;

public class SelectionAlgorithmsTest {

	public static void main(String[] args) {
		GeneticAlgorithm<Integer> ga = new GeneticAlgorithm<>();
		ga.run(new TestIndividualFactory());
	}
}
