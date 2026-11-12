package edu.psu.se411.lab09_inventory.search;

/**
 * Strategy: defines how to check if an item matches the criteria.
 * It has a single abstract method, so a lambda can be used as a strategy.
 */
@FunctionalInterface
public interface SearchStrategy<T> {

	boolean matches(T item);
}
