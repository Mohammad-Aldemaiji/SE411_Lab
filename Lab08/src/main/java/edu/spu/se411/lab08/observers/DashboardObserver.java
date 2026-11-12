package edu.spu.se411.lab08.observers;

import edu.spu.se411.lab08.observer.Observer;
import edu.spu.se411.lab08.observer.Subject;
import edu.spu.se411.lab08.sensors.Sensor;

/** Displays the latest reading of every sensor it observes. */
public class DashboardObserver implements Observer {

	private final String name;

	public DashboardObserver(String name) {
		this.name = name;
	}

	@Override
	public void update(Subject subject) {
		if (subject instanceof Sensor sensor) {
			System.out.printf("[%s] %s = %.2f %s%n", name, sensor.getName(), sensor.getReading(), sensor.getUnit());
		}
	}
}
