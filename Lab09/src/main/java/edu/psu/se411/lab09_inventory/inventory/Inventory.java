package edu.psu.se411.lab09_inventory.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.psu.se411.lab09_inventory.search.SearchStrategy;

/** Type-safe container for any kind of product. */
public class Inventory<T> {

	private final List<T> items = new ArrayList<>();

	public void addItem(T item) {
		if (item == null) {
			throw new IllegalArgumentException("Cannot add a null item");
		}
		items.add(item);
	}

	public boolean removeItem(T item) {
		return items.remove(item);
	}

	/** Read-only view so callers cannot bypass addItem/removeItem. */
	public List<T> getAllItems() {
		return Collections.unmodifiableList(items);
	}

	/**
	 * Returns every item that matches the given strategy. Accepts a strategy for T
	 * or any supertype of T (e.g. a SearchStrategy<Item> on an Inventory<Book>).
	 */
	public List<T> findItems(SearchStrategy<? super T> strategy) {
		List<T> result = new ArrayList<>();
		for (T item : items) {
			if (strategy.matches(item)) {
				result.add(item);
			}
		}
		return result;
	}

	public int size() {
		return items.size();
	}
}
