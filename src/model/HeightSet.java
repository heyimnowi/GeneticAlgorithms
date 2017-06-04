package model;

import util.RandomUtils;

public class HeightSet implements AleleSet<Double> {

	@Override
	public Double getRandomAlele() {
		return RandomUtils.instance().doubles(1, 1.3, 2).toArray()[0];
	}
}
