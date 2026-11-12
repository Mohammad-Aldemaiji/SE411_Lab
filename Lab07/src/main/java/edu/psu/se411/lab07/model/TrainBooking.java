package edu.psu.se411.lab07.model;

import java.time.LocalDate;

import edu.psu.se411.lab07.config.Config;
import edu.psu.se411.lab07.exceptions.InvalidArgumentException;
import edu.psu.se411.lab07.exceptions.MissingInformationException;

public class TrainBooking extends Booking {

	private final SeatClass seatClass;
	private Integer distanceKm; // entered later by the system

	public TrainBooking(String bookingId, String customerName, LocalDate travelDate, String destinationCity,
			SeatClass seatClass) throws InvalidArgumentException {
		super(bookingId, customerName, travelDate, destinationCity);
		if (seatClass == null) {
			throw new InvalidArgumentException("Seat class is required");
		}
		this.seatClass = seatClass;
	}

	public void setDistanceKm(int distanceKm) throws InvalidArgumentException {
		checkRange(distanceKm, Config.MIN_TRAIN_DISTANCE, Config.MAX_TRAIN_DISTANCE, "Distance (km)");
		this.distanceKm = distanceKm;
	}

	@Override
	public double computePrice() throws MissingInformationException {
		requireProvided(distanceKm, "Distance");
		return distanceKm * seatClass.getRatePerKm();
	}

	@Override
	public String getType() {
		return "Train (" + seatClass + ")";
	}

	public SeatClass getSeatClass() {
		return seatClass;
	}

	public Integer getDistanceKm() {
		return distanceKm;
	}
}
