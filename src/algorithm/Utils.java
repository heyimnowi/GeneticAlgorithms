package algorithm;

import java.util.Comparator;

import model.Individual;

public class Utils {

	public static <T> Comparator<Individual<T>> getFitnessComparator() {
		return (Individual<T> i1, Individual<T> i2)-> {
			double diff = i1.getFitness() - i2.getFitness();
			return diff < 0 ? -1 : (diff == 0 ? 0 : 1);
		};
	}
}
