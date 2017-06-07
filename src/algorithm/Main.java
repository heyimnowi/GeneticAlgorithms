package algorithm;

import java.util.concurrent.ExecutionException;

import model.DefensorFactory;


public class Main {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		boolean RUN_MANY_TIMES = false;
		
		int times = 1;
		double genAvg = 0;
		double maxFitAvg = 0;
		if (RUN_MANY_TIMES) {
			times = 10;
		}
		DefensorFactory factory = new DefensorFactory();
		
		for (int i = 0; i < times; i++) {
			GeneticAlgorithm<Object> geneticAlgorithms = new GeneticAlgorithm<>();
			geneticAlgorithms.run(factory);
			genAvg += geneticAlgorithms.getGeneration() - 1;
			maxFitAvg += geneticAlgorithms.getMaxFitness();
		}	
		
		genAvg /= times;
		maxFitAvg /= times;
		System.out.println("Gen avg: " + genAvg + " maxFitAvg: " + maxFitAvg);
	}
}
