package edu.psu.se411.lab07.model;

import java.time.LocalDate;

import edu.psu.se411.lab07.config.Config;
import edu.psu.se411.lab07.exceptions.InvalidArgumentException;
import edu.psu.se411.lab07.exceptions.MissingInformationException;

public class CarRentalBooking extends Booking {

	private final double dailyRate;
	private Integer rentalDays; // entered later by the customer

	public CarRentalBooking(String bookingId, String customerName, LocalDate travelDate, String destinationCity,
			double dailyRate) throws InvalidArgumentException {
		super(bookingId, customerName, travelDate, destinationCity);
		if (dailyRate <= 0) {
			throw new InvalidArgumentException("Daily rental rate must be positive");
		}
		this.dailyRate = dailyRate;
	}

	public void setRentalDays(int rentalDays) throws InvalidArgumentException {
		checkRange(rentalDays, Config.MIN_RENTAL_DAYS, Config.MAX_RENTAL_DAYS, "Rental days");
		this.rentalDays = rentalDays;
	}

	@Override
	public double computePrice() throws MissingInformationException {
		requireProvided(rentalDays, "Number of rental days");
		return dailyRate * rentalDays;
	}

	@Override
	public String getType() {
		return "Car rental";
	}

	public double getDailyRate() {
		return dailyRate;
	}

	public Integer getRentalDays() {
		return rentalDays;
	}
}
