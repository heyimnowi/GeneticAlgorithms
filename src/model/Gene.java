package model;

public class Gene<T> {

	private final T alele;
	private final AleleSet<T> aleleSet;
	
	public Gene(AleleSet<T> aleleSet, T alele) {
		this.alele = alele;
		this.aleleSet = aleleSet;
	}
	
	public Gene(AleleSet<T> aleleSet) {
		this(aleleSet, aleleSet.getAlele());
	}
	
	public Gene<T> mutate() {
		return new Gene<>(aleleSet);
	}
	
	T getAlele() {
		return alele;
	}
}
