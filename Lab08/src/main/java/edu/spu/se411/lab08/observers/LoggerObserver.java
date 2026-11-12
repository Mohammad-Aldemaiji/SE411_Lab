package edu.spu.se411.lab08.observers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.spu.se411.lab08.observer.Observer;
import edu.spu.se411.lab08.observer.Subject;
import edu.spu.se411.lab08.sensors.Sensor;

/** Writes every sensor change to the log file (and to the console for checking). */
public class LoggerObserver implements Observer {

	private static final Logger logger = LoggerFactory.getLogger(LoggerObserver.class);

	@Override
	public void update(Subject subject) {
		if (subject instanceof Sensor sensor) {
			String value = String.format("%.2f %s", sensor.getReading(), sensor.getUnit());
			System.out.printf("[Logger] logged %s = %s%n", sensor.getName(), value);
			logger.info("{} changed to {}", sensor.getName(), value);
		}
	}
}
