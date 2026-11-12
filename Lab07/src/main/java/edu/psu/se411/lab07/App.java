package edu.psu.se411.lab07;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab07.exceptions.InvalidArgumentException;
import edu.psu.se411.lab07.exceptions.MissingInformationException;
import edu.psu.se411.lab07.model.Booking;
import edu.psu.se411.lab07.model.CarRentalBooking;
import edu.psu.se411.lab07.model.FlightBooking;
import edu.psu.se411.lab07.model.SeatClass;
import edu.psu.se411.lab07.model.TrainBooking;
import edu.psu.se411.lab07.service.BookingService;

/**
 * Build and run with: mvn clean package exec:java
 */
public class App {

	static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		logger.info("Application is starting...");

		BookingService service = new BookingService();
		List<Booking> bookings = new ArrayList<>();

		try {
			// 1- Valid bookings of each type
			FlightBooking flight = new FlightBooking("F-001", "Sara Ahmed", LocalDate.of(2026, 12, 20), "Dubai", 850.0);
			flight.setLuggageWeight(23);

			TrainBooking train = new TrainBooking("T-001", "Omar Khalid", LocalDate.of(2026, 12, 21), "Dammam",
					SeatClass.FIRST_CLASS);
			train.setDistanceKm(410);

			CarRentalBooking car = new CarRentalBooking("C-001", "Lina Saleh", LocalDate.of(2026, 12, 22), "Abha", 180.0);
			car.setRentalDays(5);

			bookings.add(flight);
			bookings.add(train);
			bookings.add(car);

			// 2- Same call works for every booking type (polymorphism)
			System.out.println("=== Valid bookings ===");
			for (Booking b : bookings) {
				System.out.printf("%-60s -> %10.2f SAR%n", b, service.computeTotalPrice(b));
			}
			System.out.printf("Total of all bookings: %.2f SAR%n%n", service.computeTotalPrice(bookings));
		} catch (InvalidArgumentException | MissingInformationException e) {
			logger.error("Unexpected error with valid bookings: {}", e.getMessage(), e);
		}

		System.out.println("=== Error cases ===");

		// 3- Missing information: luggage weight never entered
		try {
			FlightBooking noLuggage = new FlightBooking("F-002", "Ali Hassan", LocalDate.of(2027, 1, 5), "Cairo", 1200.0);
			service.computeTotalPrice(noLuggage);
		} catch (MissingInformationException | InvalidArgumentException e) {
			report(e);
		}

		// 4- Invalid luggage weight (> 40 kg)
		try {
			FlightBooking heavy = new FlightBooking("F-003", "Noura Fahad", LocalDate.of(2027, 1, 6), "London", 2500.0);
			heavy.setLuggageWeight(55);
		} catch (InvalidArgumentException e) {
			report(e);
		}

		// 5- Missing train distance
		try {
			TrainBooking noDistance = new TrainBooking("T-002", "Faisal Nasser", LocalDate.of(2027, 1, 7), "Qassim",
					SeatClass.STANDARD);
			service.computeTotalPrice(noDistance);
		} catch (MissingInformationException | InvalidArgumentException e) {
			report(e);
		}

		// 6- Invalid train distance
		try {
			TrainBooking far = new TrainBooking("T-003", "Reem Majed", LocalDate.of(2027, 1, 8), "Jeddah",
					SeatClass.STANDARD);
			far.setDistanceKm(2500);
		} catch (InvalidArgumentException e) {
			report(e);
		}

		// 7- Missing rental days
		try {
			CarRentalBooking noDays = new CarRentalBooking("C-002", "Yousef Adel", LocalDate.of(2027, 1, 9), "Taif", 150.0);
			service.computeTotalPrice(noDays);
		} catch (MissingInformationException | InvalidArgumentException e) {
			report(e);
		}

		// 8- Invalid rental days
		try {
			CarRentalBooking tooLong = new CarRentalBooking("C-003", "Hind Saad", LocalDate.of(2027, 1, 10), "AlUla", 200.0);
			tooLong.setRentalDays(45);
		} catch (InvalidArgumentException e) {
			report(e);
		}

		logger.info("Application is closing...");
	}

	private static void report(Exception e) {
		System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
		logger.error("{}: {}", e.getClass().getSimpleName(), e.getMessage());
	}
}
