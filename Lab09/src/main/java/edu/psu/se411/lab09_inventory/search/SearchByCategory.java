package edu.psu.se411.lab09_inventory.search;

import edu.psu.se411.lab09_inventory.model.ElectronicDevice;
import edu.psu.se411.lab09_inventory.model.Item;

/**
 * ElectronicDevice-specific criteria. Typed on Item so it can also be used on a
 * mixed Inventory<Item>: anything that is not an ElectronicDevice does not match.
 */
public class SearchByCategory implements SearchStrategy<Item> {

	private final String category;

	public SearchByCategory(String category) {
		this.category = category;
	}

	@Override
	public boolean matches(Item item) {
		return item instanceof ElectronicDevice device && device.getCategory().equalsIgnoreCase(category);
	}
}
