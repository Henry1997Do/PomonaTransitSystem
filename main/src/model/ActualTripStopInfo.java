package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ActualTripStopInfo extends TripDisplay {
	private int stopNumber;
	private String actualStartTime;
	private String actualArrivalTime;
	private int numPassengersIn;
	private int numPassengersOut;
	private BooleanProperty updated;

	public ActualTripStopInfo(int tripNumber, String date, String scheduledStartTime, int stopNumber,
			String scheduledArrivalTime, String actualStartTime, String actualArrivalTime, int numPassengersIn,
			int numPassengersOut) {
		super(tripNumber, date, scheduledStartTime, scheduledArrivalTime);
		this.stopNumber = stopNumber;
		this.actualStartTime = actualStartTime;
		this.actualArrivalTime = actualArrivalTime;
		this.numPassengersIn = numPassengersIn;
		this.numPassengersOut = numPassengersOut;
		this.updated = new SimpleBooleanProperty(false);
	}

	public int getStopNumber() {
		return stopNumber;
	}

	public void setStopNumber(int stopNumber) {
		this.stopNumber = stopNumber;
	}

	public String getActualStartTime() {
		return actualStartTime;
	}

	public void setActualStartTime(String actualStartTime) {
		this.actualStartTime = actualStartTime;
	}

	public String getActualArrivalTime() {
		return actualArrivalTime;
	}

	public void setActualArrivalTime(String actualArrivalTime) {
		this.actualArrivalTime = actualArrivalTime;
	}

	public int getNumPassengersIn() {
		return numPassengersIn;
	}

	public void setNumPassengersIn(int numPassengersIn) {
		this.numPassengersIn = numPassengersIn;
	}

	public int getNumPassengersOut() {
		return numPassengersOut;
	}

	public void setNumPassengersOut(int numPassengersOut) {
		this.numPassengersOut = numPassengersOut;
	}

	public boolean isUpdated() {
		return updated.get();
	}

	public void setUpdated(boolean updated) {
		this.updated.set(updated);
	}

	public BooleanProperty updatedProperty() {
		return updated;
	}
}
