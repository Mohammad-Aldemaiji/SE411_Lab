package edu.psu.se411.lab07.model;

import java.time.LocalDate;

import edu.psu.se411.lab07.exceptions.InvalidArgumentException;
import edu.psu.se411.lab07.exceptions.MissingInformationException;

/**
 * Base class for every booking type. Holds the shared data and the shared
 * validation helpers; each subclass only adds its own data and price rule.
 */
public abstract class Booking {

	private final String bookingId;
	private final String customerName;
	private final LocalDate travelDate;
	private final String destinationCity;

	protected Booking(String bookingId, String customerName, LocalDate travelDate, String destinationCity)
			throws InvalidArgumentException {
		this.bookingId = requireText(bookingId, "Booking ID");
		this.customerName = requireText(customerName, "Customer name");
		if (travelDate == null) {
			throw new InvalidArgumentException("Travel date is required");
		}
		this.travelDate = travelDate;
		this.destinationCity = requireText(destinationCity, "Destination city");
	}

	/**
	 * Each booking type computes its total price with its own rule (polymorphism).
	 */
	public abstract double computePrice() throws MissingInformationException;

	/** Short label used when printing the booking, e.g. "Flight". */
	public abstract String getType();

	// ---------- reusable validation helpers shared by all subclasses ----------

	protected static void checkRange(double value, double min, double max, String field)
			throws InvalidArgumentException {
		if (value < min || value > max) {
			throw new InvalidArgumentException(
					String.format("%s must be between %s and %s (got %s)", field, fmt(min), fmt(max), fmt(value)));
		}
	}

	protected void requireProvided(Object value, String field) throws MissingInformationException {
		if (value == null) {
			throw new MissingInformationException(field + " was not provided for booking " + bookingId);
		}
	}

	private static String requireText(String value, String field) throws InvalidArgumentException {
		if (value == null || value.isBlank()) {
			throw new InvalidArgumentException(field + " cannot be empty");
		}
		return value;
	}

	private static String fmt(double d) {
		return d == Math.rint(d) ? String.valueOf((long) d) : String.valueOf(d);
	}

	// ---------- getters ----------

	public String getBookingId() {
		return bookingId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public LocalDate getTravelDate() {
		return travelDate;
	}

	public String getDestinationCity() {
		return destinationCity;
	}

	@Override
	public String toString() {
		return String.format("%s booking %s for %s to %s on %s", getType(), bookingId, customerName,
				destinationCity, travelDate);
	}
}
