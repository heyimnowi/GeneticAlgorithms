package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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
	private final double T;
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
			this.T = Double.parseDouble(props.getProperty("T").trim());
			this.m = Integer.parseInt(props.getProperty("m").trim());
			if (K < 2 || K > N) {
				throw new IllegalArgumentException("K must be between 2 and N.");
			}
			// Reproduction
			this.reproductionMethod = ReproductionMethod.get(props.getProperty("reproduction_method").trim());
			this.reproductionP = Double.parseDouble(props.getProperty("reproduction_p").trim());
			checkProbability(reproductionP, "Reproduction method p");
			// Mutation
			this.mutationMethod = MutationMethod.get(props.getProperty("mutation_method").trim());
			this.mutationP = Double.parseDouble(props.getProperty("mutation_p").trim());
			checkProbability(mutationP, "Mutation method p");
			// Replacement
			this.replacementMethod1 = ReplacementMethod.get(props.getProperty("replacement_method_1").trim());
			this.replacementMethod2 = ReplacementMethod.get(props.getProperty("replacement_method_2").trim());
			this.replacementMethodP = Double.parseDouble(props.getProperty("replacement_method_1_p").trim());
			checkProbability(replacementMethodP, "Replacement method p");
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
			this.debug = props.getProperty("debug").trim().equals(true);
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

	public double getT() {
		return T;
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
