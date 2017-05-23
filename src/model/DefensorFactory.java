package model;

import java.util.ArrayList;
import java.util.List;

public class DefensorFactory<T> implements IndividualFactory<T> {

	public Individual<T> getIndividual() {
		// TODO
		List<AleleSet<? extends T>> aleleSets = new ArrayList<>();
		
		List<Gene<? extends T>> genes = new ArrayList<>();
		for (AleleSet<? extends T> aleleSet: aleleSets) {
			genes.add(new Gene<>(aleleSet));
		}
		
		return new Defensor<T>(genes);
	}
}
