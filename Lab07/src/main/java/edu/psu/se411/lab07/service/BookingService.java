package edu.psu.se411.lab07.service;

import java.util.List;

import edu.psu.se411.lab07.exceptions.MissingInformationException;
import edu.psu.se411.lab07.model.Booking;

/**
 * Works with any Booking type through the base class only (polymorphism).
 */
public class BookingService {

	/** Works for Flight, Train and Car rental bookings alike. */
	public double computeTotalPrice(Booking booking) throws MissingInformationException {
		return booking.computePrice();
	}

	/** Sum of all bookings of any type. */
	public double computeTotalPrice(List<? extends Booking> bookings) throws MissingInformationException {
		double total = 0;
		for (Booking b : bookings) {
			total += computeTotalPrice(b);
		}
		return total;
	}
}
