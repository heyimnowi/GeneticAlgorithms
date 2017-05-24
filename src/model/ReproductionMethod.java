package model;

import java.util.Arrays;

public enum ReproductionMethod {

	ONE_POINT, DOUBLE_POINT, RING, UNIFORM;
	
	public static ReproductionMethod get(String name) {
		return Arrays.stream(ReproductionMethod.values())
			.filter(method -> method.toString().toLowerCase().equals(name))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown reproduction method: " + name));
	}
}
