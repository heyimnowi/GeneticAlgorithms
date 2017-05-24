package model;

import java.util.Arrays;

public enum MutationMethod {

	SINGLE_GENE, MULTI_GENE;
	
	public static MutationMethod get(String name) {
		return Arrays.stream(MutationMethod.values())
			.filter(method -> method.toString().toLowerCase().equals(name))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown mutation method: " + name));
	}
}
