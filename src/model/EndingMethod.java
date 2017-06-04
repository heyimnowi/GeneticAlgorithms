package model;

import java.util.Arrays;

public enum EndingMethod {

	MAX_GENERATIONS, FITNESS_MIN, STRUCTURE, CONTENT, ALL;
	
	public static EndingMethod get(String name) {
		return Arrays.stream(EndingMethod.values())
			.filter(method -> method.toString().toLowerCase().equals(name))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown ending method: " + name));
	}
}
