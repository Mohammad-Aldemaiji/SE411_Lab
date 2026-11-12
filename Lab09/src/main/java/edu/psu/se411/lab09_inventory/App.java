package edu.psu.se411.lab09_inventory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab09_inventory.inventory.Inventory;
import edu.psu.se411.lab09_inventory.model.Book;
import edu.psu.se411.lab09_inventory.model.ElectronicDevice;
import edu.psu.se411.lab09_inventory.model.Item;
import edu.psu.se411.lab09_inventory.search.SearchByAuthor;
import edu.psu.se411.lab09_inventory.search.SearchByCategory;
import edu.psu.se411.lab09_inventory.search.SearchById;
import edu.psu.se411.lab09_inventory.search.SearchByName;
import edu.psu.se411.lab09_inventory.search.SearchStrategy;

public class App {

	static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		logger.info("Application is starting...");

		try {
			// ---------- Part 1: generic inventory holding books and devices ----------
			Inventory<Item> inventory = new Inventory<>();
			inventory.addItem(new Book(1, "Concurrency in Java", "Brian Goetz"));
			inventory.addItem(new Book(2, "Effective Java", "Joshua Bloch"));
			inventory.addItem(new Book(3, "Java Puzzlers", "Joshua Bloch"));
			inventory.addItem(new ElectronicDevice(4, "MacBook Air", "Laptop"));
			inventory.addItem(new ElectronicDevice(5, "ThinkPad X1", "Laptop"));
			inventory.addItem(new ElectronicDevice(6, "Galaxy S25", "Phone"));

			ElectronicDevice toRemove = new ElectronicDevice(7, "Old Router", "Network");
			inventory.addItem(toRemove);
			System.out.println("Removed 'Old Router': " + inventory.removeItem(toRemove));

			// ---------- Part 2: strategy-based search ----------
			print("Search by name 'Concurrency in Java'", inventory.findItems(new SearchByName<>("Concurrency in Java")));
			print("Search by name 'Galaxy S25'", inventory.findItems(new SearchByName<>("Galaxy S25")));
			print("Search by id 5", inventory.findItems(new SearchById<>(5)));
			print("Search books by author 'Joshua Bloch'", inventory.findItems(new SearchByAuthor("Joshua Bloch")));
			print("Search devices by category 'Laptop'", inventory.findItems(new SearchByCategory("Laptop")));

			// Lambda as a SearchStrategy (it is a functional interface)
			SearchStrategy<Item> nameContainsJava = item -> item.getName().toLowerCase().contains("java");
			print("Lambda: name contains 'java'", inventory.findItems(nameContainsJava));
			print("Lambda: id greater than 4", inventory.findItems(item -> item.getId() > 4));

			// The same strategies also work on a more specific inventory
			Inventory<Book> books = new Inventory<>();
			books.addItem(new Book(10, "Clean Code", "Robert Martin"));
			books.addItem(new Book(11, "Clean Architecture", "Robert Martin"));
			print("Inventory<Book> by author 'Robert Martin'", books.findItems(new SearchByAuthor("Robert Martin")));

			// ---------- Part 3: display any inventory ----------
			displayInventory(inventory);
			displayInventory(books);
		} catch (IllegalArgumentException e) {
			logger.error("Invalid inventory operation: {}", e.getMessage(), e);
		}

		logger.info("Application is closing...");
	}

	/** Works with an inventory of any type thanks to the unbounded wildcard. */
	public static void displayInventory(Inventory<?> inventory) {
		System.out.println("\n=== Inventory contents (" + inventory.size() + " items) ===");
		for (Object item : inventory.getAllItems()) {
			System.out.println("  " + item);
		}
	}

	private static void print(String title, List<? extends Item> results) {
		System.out.println("\n" + title + ":");
		if (results.isEmpty()) {
			System.out.println("  (no match)");
		}
		results.forEach(i -> System.out.println("  " + i));
	}
}
