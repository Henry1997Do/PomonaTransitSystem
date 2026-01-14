package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Bus {
	private String busId;
	private String model;
	private String year;
	private BooleanProperty updated;
	
	public Bus(String id, String model, String year) {
		this.busId = id;
		this.model = model;
		this.year = year;
		this.updated = new SimpleBooleanProperty(false);
	}
	
	public String getBusId() {
		return busId;
	}
	public void setBusId(String busId) {
		this.busId = busId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
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
