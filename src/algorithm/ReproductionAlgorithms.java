package algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Couple;
import model.Gene;

public class ReproductionAlgorithms {

	private static final double p = 0.5; // TODO
	
	public static <T> Couple<T> onePoint(Couple<T> couple) {
		int locus = new Random().nextInt(couple.chromosomeSize());
		return between(couple, locus, couple.chromosomeSize(), false);
	}
	
	public static <T> Couple<T> twoPoints(Couple<T> couple) {
		int locusStart = new Random().nextInt(couple.chromosomeSize());
		int locusEnd = new Random().nextInt(couple.chromosomeSize());
		return between(couple, locusStart, locusEnd, false);
	}
	
	public static <T> Couple<T> ring(Couple<T> couple) {
		int locusStart = new Random().nextInt(couple.chromosomeSize());
		int l = new Random().nextInt(couple.chromosomeSize() / 2 + 2) + 1;
		int locusEnd = (locusStart + l) % couple.chromosomeSize();
		boolean invert = false;
		if (locusEnd < locusStart) {
			invert = true;
			locusStart = locusStart | locusEnd;
			locusEnd = locusStart | locusEnd;
			locusStart = locusStart | locusEnd; 
		}
		return between(couple, locusStart, locusEnd, invert);
	}
	
	public static <T> Couple<T> uniform(Couple<T> couple) {
		List<Gene<? extends T>> genes1 = new ArrayList<>();
		List<Gene<? extends T>> genes2 = new ArrayList<>();
		for (int i = 0; i < couple.chromosomeSize(); i++) {
			Gene<? extends T> gene1 = couple.getIndividual1().getGene(i);
			Gene<? extends T> gene2 = couple.getIndividual2().getGene(i);
			if (gene1.equals(gene2) || new Random().nextDouble() >= p) {
				genes1.add(gene1);
				genes2.add(gene2);
			} else {
				genes1.add(gene2);
				genes2.add(gene1);
			}
		}
		return makeCouple(couple, genes1, genes2);
	}
	
	private static <T> Couple<T> between(Couple<T> couple, int start, int end, boolean invert) {
		List<Gene<? extends T>> genes1 = new ArrayList<>();
		List<Gene<? extends T>> genes2 = new ArrayList<>();
		for (int i = 0; i < couple.chromosomeSize(); i++) {
			if ((i <= start || i >= end) && !invert) {
				genes1.add(couple.getIndividual1().getGene(i));
				genes2.add(couple.getIndividual2().getGene(i));
			} else {
				genes2.add(couple.getIndividual1().getGene(i));
				genes1.add(couple.getIndividual2().getGene(i));
			}
		}
		return makeCouple(couple, genes1, genes2);
	}
	
	private static <T> Couple<T> makeCouple(Couple<T> couple, 
			List<Gene<? extends T>> genes1, List<Gene<? extends T>> genes2) {
		return new Couple<>(couple.getIndividual1().mutate(genes1), couple.getIndividual2().mutate(genes2));
	}
}
