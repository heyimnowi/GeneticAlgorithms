package util;

import java.util.Random;
import java.util.stream.DoubleStream;

public class RandomUtils {

	private static final Random rand = new Random();
	private static RandomUtils _instance;
	
	public static RandomUtils instance() {
		if (RandomUtils._instance == null) {
			try {
				synchronized (RandomUtils.class) {
					if(_instance == null) {
						RandomUtils._instance = new RandomUtils();
					 }
				}
			} catch (Exception e) {
				throw new RuntimeException("Error while loading props.", e);
			}
		}
		return RandomUtils._instance;
	}
	
	public double nextDouble() {
		return rand.nextDouble();
	}
	
	public DoubleStream doubles(int streamSize, double randomNumberOrigin, double randomNumberBound) {
		return rand.doubles(streamSize, randomNumberOrigin, randomNumberBound);
	}
	
	public int nextInt(int bound) {
		return rand.nextInt(bound);
	}

	private RandomUtils() {
	}
}
