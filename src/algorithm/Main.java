package algorithm;

import model.DefensorFactory;


public class Main {

	public static void main(String[] args) {
		GeneticAlgorithm<Object> geneticAlgorithms = new GeneticAlgorithm<>();
		geneticAlgorithms.run(new DefensorFactory());
	}
}
