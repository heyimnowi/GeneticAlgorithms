package model;

import java.util.Arrays;

public enum ReplacementMethod {

	METHOD_1, METHOD_2, METHOD_3;
	
	public static ReplacementMethod get(String name) {
		return Arrays.stream(ReplacementMethod.values())
			.filter(method -> method.toString().toLowerCase().equals(name))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown replacement method: " + name));
	}
}
