package edu.psu.se411.lab07.model;

import java.time.LocalDate;

import edu.psu.se411.lab07.config.Config;
import edu.psu.se411.lab07.exceptions.InvalidArgumentException;
import edu.psu.se411.lab07.exceptions.MissingInformationException;

public class FlightBooking extends Booking {

	private final double basePrice;
	private Double luggageWeight; // entered later by the customer

	public FlightBooking(String bookingId, String customerName, LocalDate travelDate, String destinationCity,
			double basePrice) throws InvalidArgumentException {
		super(bookingId, customerName, travelDate, destinationCity);
		if (basePrice < 0) {
			throw new InvalidArgumentException("Base ticket price cannot be negative");
		}
		this.basePrice = basePrice;
	}

	public void setLuggageWeight(double luggageWeight) throws InvalidArgumentException {
		checkRange(luggageWeight, Config.MIN_LUGGAGE_WEIGHT, Config.MAX_LUGGAGE_WEIGHT, "Luggage weight (kg)");
		this.luggageWeight = luggageWeight;
	}

	@Override
	public double computePrice() throws MissingInformationException {
		requireProvided(luggageWeight, "Luggage weight");
		return basePrice + luggageWeight * Config.EXTRA_LUGGAGE_RATE;
	}

	@Override
	public String getType() {
		return "Flight";
	}

	public double getBasePrice() {
		return basePrice;
	}

	public Double getLuggageWeight() {
		return luggageWeight;
	}
}
