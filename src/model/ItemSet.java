package model;

import java.util.Collections;
import java.util.List;

public class ItemSet implements AleleSet<Item> {

	private final List<Item> itemSet;
	private final String name;

	public ItemSet(List<Item> itemSet, String name) {
		this.itemSet = itemSet;
		this.name = name;
	}

	public List<Item> getItemSet() {
		return itemSet;
	}

	public String getName() {
		return name;
	}

	@Override
	public Item getRandomAlele() {
		Collections.shuffle(itemSet);
		return itemSet.get(0);
	}
}
