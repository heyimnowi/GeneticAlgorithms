package model;

public class Gene<T> {

	private final T alele;
	private final AleleSet<T> aleleSet;
	
	public Gene(AleleSet<T> aleleSet, T alele) {
		this.alele = alele;
		this.aleleSet = aleleSet;
	}
	
	public Gene(AleleSet<T> aleleSet) {
		this(aleleSet, aleleSet.getRandomAlele());
	}
	
	public Gene<T> mutate() {
		return new Gene<>(aleleSet);
	}
	
	public T getAlele() {
		return alele;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gene<?> other = (Gene<?>) obj;
		if (alele == null) {
			if (other.alele != null)
				return false;
		} else if (!alele.equals(other.alele))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return alele.toString();
	}
}
