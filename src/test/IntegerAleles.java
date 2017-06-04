package test;

import model.AleleSet;
import util.RandomUtils;

public class IntegerAleles implements AleleSet<Integer> {
	@Override
	public Integer getRandomAlele() {
		return RandomUtils.instance().nextInt(10);
	}
}
