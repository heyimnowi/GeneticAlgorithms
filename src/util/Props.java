package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import model.EndingMethod;
import model.MutationMethod;
import model.ReplacementMethod;
import model.ReproductionMethod;
import model.SelectionMethod;

public class Props {

	private static Props _instance;
	
	public static Props instance() {
		if (Props._instance == null) {
			try {
				Props._instance = new Props();
			} catch (Exception e) {
				throw new RuntimeException("Error while loading props.", e);
			}
		}
		return Props._instance;
	}
	
	private static final String FILENAME = "config.properties";
	private final Properties props;
	
	// Population
	private final int N;
	// Selection
	private final SelectionMethod selectionMethod1;
	private final SelectionMethod selectionMethod2;
	private final double selectionMethodP;
	private final int K;
	private final double tempDuration;
	private final double minTemp;
	private final double initialTemp;
	private final int m;
	// Reproduction
	private final ReproductionMethod reproductionMethod;
	private final double reproductionP;
	// Mutation
	private final MutationMethod mutationMethod;
	private final double mutationP;
	// Replacement
	private final ReplacementMethod replacementMethod1;
	private final ReplacementMethod replacementMethod2;
	private final double replacementMethodP;
	// Ending condition
	private Set<EndingMethod> endingMethods;
	private int maxGenerations;
	private double fitnessMin;
	private double structure;
	private double content;
	// Player
	private final double attMult;
	private final double defMult;
	private final double forceItem;
	private final double agilityItem;
	private final double experienceItem;
	private final double resistanceItem;
	private final double healthItem;
	// Equipment
	private final String equipmentDirectory;
	private final List<String> equipmentFiles;
	// Other
	private final boolean debug;
	
	private Props() throws FileNotFoundException, IOException {
		this.props = new Properties();
		try(FileInputStream input = new FileInputStream(FILENAME)) {
			props.load(input);
			// Population
			this.N = Integer.parseInt(props.getProperty("N").trim());
			if (N % 2 == 1) {
				throw new IllegalArgumentException("Population must be even sized.");
			}
			// Selection
			this.selectionMethod1 = SelectionMethod.get(props.getProperty("selection_method_1").trim());
			this.selectionMethod2 = SelectionMethod.get(props.getProperty("selection_method_2").trim());
			this.selectionMethodP = Double.valueOf(props.getProperty("selection_method_1_p").trim());
			checkProbability(selectionMethodP, "Selection method p");
			this.K = Integer.parseInt(props.getProperty("K").trim());
			if (K < 2 || K > N) {
				throw new IllegalArgumentException("K must be between 2 and N.");
			}
			this.tempDuration = Double.parseDouble(props.getProperty("tempDuration").trim());
			if (tempDuration <= 0) {
				throw new IllegalArgumentException("TempDuration must be positive.");
			}
			this.minTemp = Double.parseDouble(props.getProperty("minTemp").trim());
			if (minTemp < 0) {
				throw new IllegalArgumentException("TempDuration must be positive or cero.");
			}
			this.initialTemp = Double.parseDouble(props.getProperty("initialTemp").trim());
			if (initialTemp < minTemp) {
				throw new IllegalArgumentException("InitialTemp should be greater than or equal to minTemp.");
			}
			this.m = Integer.parseInt(props.getProperty("m").trim());
			if (m < 2 || m > K) {
				throw new IllegalArgumentException("M must be between 2 and K.");
			}
			// Reproduction
			this.reproductionMethod = ReproductionMethod.get(props.getProperty("reproduction_method").trim());
			this.reproductionP = Double.parseDouble(props.getProperty("reproduction_p").trim());
			if (reproductionMethod.equals(ReproductionMethod.UNIFORM)) {				
				checkProbability(reproductionP, "Reproduction method p");
			}
			// Mutation
			this.mutationMethod = MutationMethod.get(props.getProperty("mutation_method").trim());
			this.mutationP = Double.parseDouble(props.getProperty("mutation_p").trim());
			checkProbability(mutationP, "Mutation method p");
			// Replacement
			this.replacementMethod1 = ReplacementMethod.get(props.getProperty("replacement_method_1").trim());
			this.replacementMethod2 = ReplacementMethod.get(props.getProperty("replacement_method_2").trim());
			this.replacementMethodP = Double.parseDouble(props.getProperty("replacement_method_1_p").trim());
			checkProbability(replacementMethodP, "Replacement method p");
			// Ending condition
			this.endingMethods = Arrays.stream(props.getProperty("ending_methods").replaceAll(" ", "").split(","))
					.map(str -> EndingMethod.get(str))
					.collect(Collectors.toSet());
			if (this.endingMethods.contains(EndingMethod.ALL)) {
				this.endingMethods = new HashSet<>(Arrays.asList(EndingMethod.values()));
				this.endingMethods.remove(EndingMethod.ALL);
			}
			this.maxGenerations = Integer.parseInt(props.getProperty("max_generations").trim());
			if (this.endingMethods.contains(EndingMethod.MAX_GENERATIONS) && maxGenerations <= 0) {
				throw new IllegalArgumentException("Max generations must be greater than 0.");
			}
			this.fitnessMin = Double.parseDouble(props.getProperty("fitness_min").trim());
			if (this.endingMethods.contains(EndingMethod.FITNESS_MIN) && fitnessMin <= 0) {
				throw new IllegalArgumentException("Min fitness must be greater than 0.");
			}
			this.structure = Double.parseDouble(props.getProperty("structure").trim());
			if (this.endingMethods.contains(EndingMethod.STRUCTURE)) {
				checkProbability(structure, "Structure percentage");
			}
			this.content = Double.parseDouble(props.getProperty("content").trim());
			if (this.endingMethods.contains(EndingMethod.CONTENT) && content < 1) {
				throw new IllegalArgumentException("Content must be greater than 1.");
			}
			// Player
			this.attMult = Double.parseDouble(props.getProperty("att_mult").trim());
			this.defMult = Double.parseDouble(props.getProperty("def_mult").trim());
			this.forceItem = Double.parseDouble(props.getProperty("force_item").trim());
			this.agilityItem = Double.parseDouble(props.getProperty("agility_item").trim());
			this.experienceItem = Double.parseDouble(props.getProperty("experience_item").trim());
			this.resistanceItem = Double.parseDouble(props.getProperty("resistance_item").trim());
			this.healthItem = Double.parseDouble(props.getProperty("health_item").trim());
			// Equipment
			this.equipmentDirectory = props.getProperty("equipment_directory").trim();
			this.equipmentFiles = Arrays.asList(props.getProperty("equipment_files").replaceAll(" ", "").split(","));
			// Other
			this.debug = props.getProperty("debug").trim().equals("true");
		}
	}
	
	public Properties getProps() {
		return props;
	}
	
	public int getN() {
		return N;
	}

	public SelectionMethod getSelectionMethod1() {
		return selectionMethod1;
	}

	public SelectionMethod getSelectionMethod2() {
		return selectionMethod2;
	}

	public double getSelectionMethodP() {
		return selectionMethodP;
	}

	public int getK() {
		return K;
	}

	public double getTempDuration() {
		return tempDuration;
	}

	public double getMinTemp() {
		return minTemp;
	}

	public double getInitialTemp() {
		return initialTemp;
	}

	public int getM() {
		return m;
	}

	public ReproductionMethod getReproductionMethod() {
		return reproductionMethod;
	}

	public double getReproductionP() {
		return reproductionP;
	}

	public MutationMethod getMutationMethod() {
		return mutationMethod;
	}

	public double getMutationP() {
		return mutationP;
	}

	public ReplacementMethod getReplacementMethod1() {
		return replacementMethod1;
	}

	public ReplacementMethod getReplacementMethod2() {
		return replacementMethod2;
	}

	public double getReplacementMethodP() {
		return replacementMethodP;
	}

	public Set<EndingMethod> getEndingMethods() {
		return endingMethods;
	}

	public int getMaxGenerations() {
		return maxGenerations;
	}

	public double getFitnessMin() {
		return fitnessMin;
	}

	public double getStructure() {
		return structure;
	}

	public double getContent() {
		return content;
	}
	
	public double getAttMult() {
		return attMult;
	}

	public double getDefMult() {
		return defMult;
	}

	public double getForceItem() {
		return forceItem;
	}

	public double getAgilityItem() {
		return agilityItem;
	}

	public double getExperienceItem() {
		return experienceItem;
	}

	public double getResistanceItem() {
		return resistanceItem;
	}

	public double getHealthItem() {
		return healthItem;
	}

	public String getEquipmentDirectory() {
		return equipmentDirectory;
	}

	public List<String> equipmentFiles() {
		return equipmentFiles;
	}
	
	public boolean debug() {
		return debug;
	}
	
	private void checkProbability(double value, String name) {
		if (value < 0 || value > 1) {
			throw new IllegalArgumentException(name + " must be between 0 and 1.");
		}
	}
}
