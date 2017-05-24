package model;

import java.util.Arrays;


public enum SelectionMethod {

	ELITE, RANDOM, ROULETTE, UNIVERSAL, BOLTZMANN, TOURNAMENT_DET, TOURNAMENT_PROB, RANKING;
	
	public static SelectionMethod get(String name) {
		return Arrays.stream(SelectionMethod.values())
			.filter(method -> method.toString().toLowerCase().equals(name))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown selection method: " + name));
	}
}
