package model;

import java.util.List;

public interface IndividualFactory<T> {

	public Individual<T> getIndividual();
	
	public Individual<T> getIndividual(List<Gene<? extends T>> genes);
}
