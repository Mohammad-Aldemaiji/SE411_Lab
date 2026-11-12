package edu.psu.se411.lab09_inventory.search;

import edu.psu.se411.lab09_inventory.model.Book;
import edu.psu.se411.lab09_inventory.model.Item;

/**
 * Book-specific criteria. Typed on Item so it can also be used on a mixed
 * Inventory<Item>: anything that is not a Book simply does not match.
 */
public class SearchByAuthor implements SearchStrategy<Item> {

	private final String author;

	public SearchByAuthor(String author) {
		this.author = author;
	}

	@Override
	public boolean matches(Item item) {
		return item instanceof Book book && book.getAuthor().equalsIgnoreCase(author);
	}
}
