package edu.psu.se411.lab09_inventory.search;

import edu.psu.se411.lab09_inventory.model.Item;

/** Works for any Item subtype, so it can be used with Inventory<Item>, Inventory<Book>, ... */
public class SearchById<T extends Item> implements SearchStrategy<T> {

	private final int id;

	public SearchById(int id) {
		this.id = id;
	}

	@Override
	public boolean matches(T item) {
		return item.getId() == id;
	}
}
