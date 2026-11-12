package edu.spu.se411.lab08.sensors;

import java.util.ArrayList;
import java.util.List;

import edu.spu.se411.lab08.observer.Observer;
import edu.spu.se411.lab08.observer.Subject;

/**
 * Common subject logic for all sensors: observer list, notification and cloning.
 * Concrete sensors only provide their name and unit.
 */
public abstract class Sensor implements Subject, Cloneable {

	private String name;
	private final String unit;
	private double reading;
	private List<Observer> observers = new ArrayList<>();

	protected Sensor(String name, String unit) {
		this.name = name;
		this.unit = unit;
	}

	@Override
	public void register(Observer o) {
		if (o != null && !observers.contains(o)) {
			observers.add(o);
		}
	}

	@Override
	public void unregister(Observer o) {
		observers.remove(o);
	}

	@Override
	public void notifyObservers() {
		// iterate over a copy so an observer may unregister itself during update()
		for (Observer o : new ArrayList<>(observers)) {
			o.update(this);
		}
	}

	/** Changing the reading notifies every registered observer. */
	public void setReading(double reading) {
		if (Double.compare(this.reading, reading) != 0) {
			this.reading = reading;
			notifyObservers();
		}
	}

	/**
	 * The clone copies the sensor's state (name, unit, reading) but starts with
	 * its OWN empty observer list: it is not observed by the original's observers.
	 */
	@Override
	public Sensor clone() {
		try {
			Sensor copy = (Sensor) super.clone();
			copy.observers = new ArrayList<>();
			return copy;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("Sensor implements Cloneable", e);
		}
	}

	public int getObserverCount() {
		return observers.size();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public double getReading() {
		return reading;
	}
}
