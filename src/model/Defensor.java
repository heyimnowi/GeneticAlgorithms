package model;

import java.util.List;
import java.util.stream.Collectors;

import util.Props;

public class Defensor extends Individual<Object> {

	public Defensor(DefensorFactory factory, List<Gene<? extends Object>> genes) {
		super(factory, genes);
	}

	@Override
	public double getFitness() {
		double height = (Double) this.getGenes().get(0).getAlele();
		List<Item> items = this.getGenes().subList(1, this.getGenes().size()).stream()
				.map(gene -> (Item) gene.getAlele())
				.collect(Collectors.toList());
		return Props.instance().getAttMult() * getForce(height, items)
				+ Props.instance().getDefMult() * getDefense(height, items);
	}

	private double getForce(double height, List<Item> items) {
		return (getAgilityP(items) + getExperienceP(items)) * getForceP(items) * getAtm(height);
	}
	
	private double getDefense(double height, List<Item> items) {
		return (getResistanceP(items) + getExperienceP(items)) * getHealthP(items) * getDem(height);
	}
	
	private double getAtm(double h) {
		return 0.5 - Math.pow(3 * h - 5, 4) + Math.pow(3 * h - 5, 2) + h / 2;
	}
	
	private double getDem(double h) {
		return 2 + Math.pow(3 * h - 5, 4) - Math.pow(3 * h - 5, 2) - h/2;
	}
	
	private double getForceP(List<Item> items) {
		return 100 * Math.tanh(0.01 * Props.instance().getForceItem()
				* items.stream().mapToDouble(Item::getForce).sum());
	}
	
	private double getAgilityP(List<Item> items) {
		return Math.tanh(0.01 * Props.instance().getAgilityItem()
				* items.stream().mapToDouble(Item::getAgility).sum());
	}
	
	private double getExperienceP(List<Item> items) {
		return 0.6 * Math.tanh(0.01 * Props.instance().getExperienceItem()
				* items.stream().mapToDouble(Item::getExperience).sum());
	}
	
	private double getResistanceP(List<Item> items) {
		return Math.tanh(0.01 * Props.instance().getResistanceItem()
				* items.stream().mapToDouble(Item::getResistance).sum());
	}
	
	private double getHealthP(List<Item> items) {
		return 100 * Math.tanh(0.01 * Props.instance().getHealthItem()
				* items.stream().mapToDouble(Item::getHealth).sum());
	}
	
	public String toString() {
		return Props.instance().debug() ? "Ind: [" + String.join(", ", getGenes().stream()
				.skip(1)
				.map(g -> g.toString())
				.collect(Collectors.toList())) + "]"
				: "Ind(" + Math.round(getFitness() * 100) / 100.0 + ")";
	}
}
