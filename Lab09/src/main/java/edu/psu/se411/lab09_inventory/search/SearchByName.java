package edu.psu.se411.lab09_inventory.search;

import edu.psu.se411.lab09_inventory.model.Item;

/** Case-insensitive exact name match; works for any Item subtype. */
public class SearchByName<T extends Item> implements SearchStrategy<T> {

	private final String name;

	public SearchByName(String name) {
		this.name = name;
	}

	@Override
	public boolean matches(T item) {
		return item.getName().equalsIgnoreCase(name);
	}
}
