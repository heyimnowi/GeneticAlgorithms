package algorithm;

import java.util.concurrent.ExecutionException;

import model.DefensorFactory;


public class Main {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		boolean TEST = false;
		
		int times = 1;
		double genAvg = 0;
		double maxFitAvg = 0;
		if (TEST) {
			times = 10;
		}
//		ExecutorService executor = Executors.newFixedThreadPool(10);
//		CompletionService<GeneticAlgorithm<Object>> completionService = 
//				new ExecutorCompletionService<>(executor);
		DefensorFactory factory = new DefensorFactory();
		
		for (int i = 0; i < times; i++) {
			System.out.println("I: " + i);
//			completionService.submit(() -> {
			GeneticAlgorithm<Object> geneticAlgorithms = new GeneticAlgorithm<>();
			geneticAlgorithms.run(factory);
//				return geneticAlgorithms;
//			});
//			GeneticAlgorithm<Object> geneticAlgorithms = completionService.take().get();
			genAvg += geneticAlgorithms.getGeneration() - 1;
			maxFitAvg += geneticAlgorithms.getMaxFitness();
		}	
		
//		int taken = 0;
//		while (taken++ < times) {
//			GeneticAlgorithm<Object> genAlg = completionService.take().get();
//			genAvg += genAlg.getGeneration() - 1;
//			maxFitAvg += genAlg.getMaxFitness();
//		}
//		executor.shutdown();
		
		genAvg /= times;
		maxFitAvg /= times;
		System.out.println("Gen avg: " + genAvg + " maxFitAvg: " + maxFitAvg);
	}
}
