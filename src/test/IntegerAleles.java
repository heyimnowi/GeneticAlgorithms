package test;

import java.util.Random;

import model.AleleSet;

public class IntegerAleles implements AleleSet<Integer> {
	@Override
	public Integer getRandomAlele() {
		return new Random().nextInt(10);
	}
}
