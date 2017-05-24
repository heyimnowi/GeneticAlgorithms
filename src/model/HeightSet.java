package model;

import java.util.Random;

public class HeightSet implements AleleSet<Double> {

	@Override
	public Double getRandomAlele() {
		return new Random().doubles(1, 1.3, 2).toArray()[0];
	}
}
