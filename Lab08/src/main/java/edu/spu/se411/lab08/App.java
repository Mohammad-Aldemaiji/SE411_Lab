package edu.spu.se411.lab08;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.spu.se411.lab08.observers.DashboardObserver;
import edu.spu.se411.lab08.observers.LoggerObserver;
import edu.spu.se411.lab08.sensors.HumiditySensor;
import edu.spu.se411.lab08.sensors.TemperatureSensor;

/**
 * Build and run with: mvn clean package exec:java
 */
public class App {

	static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		logger.info("Application is starting...");

		// 1- Two subjects and two observers
		TemperatureSensor temp = new TemperatureSensor("Temperature");
		HumiditySensor humidity = new HumiditySensor("Humidity");

		DashboardObserver dashboard = new DashboardObserver("Dashboard");
		LoggerObserver fileLogger = new LoggerObserver();

		temp.register(dashboard);
		temp.register(fileLogger);
		humidity.register(dashboard);
		humidity.register(fileLogger);

		// 2- Simulate sensor readings changing
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			System.out.printf("%n--- Reading %d ---%n", i + 1);
			temp.setReading(20 + random.nextDouble() * 15);
			humidity.setReading(40 + random.nextDouble() * 20);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error("Simulation interrupted", e);
				Thread.currentThread().interrupt();
			}
		}

		// 3- Cloning: the clone keeps the reading but NOT the original's observers
		System.out.println("\n--- Cloning the temperature sensor ---");
		TemperatureSensor tempClone = temp.clone();
		tempClone.setName("Temperature (clone)");
		System.out.printf("Original observers: %d, clone observers: %d, clone reading: %.2f%n",
				temp.getObserverCount(), tempClone.getObserverCount(), tempClone.getReading());

		System.out.println("Changing the clone before it has any observers -> nobody is notified:");
		tempClone.setReading(99.0);

		DashboardObserver backupDashboard = new DashboardObserver("Backup dashboard");
		tempClone.register(backupDashboard);
		System.out.println("Clone now has its own observer (Backup dashboard):");
		tempClone.setReading(18.5);

		System.out.println("Changing the original -> only the original's observers react:");
		temp.setReading(30.0);

		// 4- Unregistering
		System.out.println("\n--- Logger unregistered from humidity ---");
		humidity.unregister(fileLogger);
		humidity.setReading(55.0);

		logger.info("Application is closing...");
	}
}
