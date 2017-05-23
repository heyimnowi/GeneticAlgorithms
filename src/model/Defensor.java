package model;

import java.util.List;

public class Defensor<T> extends Individual<T> {

	public Defensor(List<Gene<? extends T>> genes) {
		super(genes);
	}

	@Override
	public double getFitness() {
		return 0; // TODO
	}

}
