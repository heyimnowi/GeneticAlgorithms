package model;

import java.util.ArrayList;
import java.util.List;

import util.EquipmentReader;

public class DefensorFactory implements IndividualFactory<Object> {

	public Individual<Object> getIndividual() {
		List<AleleSet<? extends Object>> aleleSets = new ArrayList<>();		
		aleleSets.add(new HeightSet());
		aleleSets.addAll(EquipmentReader.instance().getItemSets());
		
		List<Gene<? extends Object>> genes = new ArrayList<>();
		for (AleleSet<? extends Object> aleleSet: aleleSets) {
			genes.add(new Gene<>(aleleSet));
		}
		
		return getIndividual(genes);
	}

	@Override
	public Individual<Object> getIndividual(List<Gene<? extends Object>> genes) {
		return new Defensor(this, genes);
	}
}
