package model;

import java.util.List;

public class Defensor<T> extends Individual<T> {

	public Defensor(DefensorFactory<T> factory, List<Gene<? extends T>> genes) {
		super(factory, genes);
	}

	@Override
	public double getFitness() {
		return 0; // TODO
	}

}
