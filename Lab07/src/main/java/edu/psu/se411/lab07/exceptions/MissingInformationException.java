package edu.psu.se411.lab07.exceptions;

/**
 * Thrown when a value needed to compute a booking's price has not been provided yet.
 */
public class MissingInformationException extends Exception {

	private static final long serialVersionUID = 1L;

	public MissingInformationException(String message) {
		super(message);
	}
}
