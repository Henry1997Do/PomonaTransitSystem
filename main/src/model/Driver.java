package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Driver {
	private String driverName;
	private String driverPhoneNumber;
	private BooleanProperty updated;

	public Driver(String name, String phoneNumber) {
		driverName = name;
		driverPhoneNumber = phoneNumber;
		this.updated = new SimpleBooleanProperty(false);
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverPhoneNumber() {
		return driverPhoneNumber;
	}

	public void setDriverPhoneNumber(String driverPhoneNumber) {
		this.driverPhoneNumber = driverPhoneNumber;
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
