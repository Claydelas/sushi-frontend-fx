package comp1206.sushi.common;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Staff extends Model {

	private SimpleStringProperty name;
	private SimpleStringProperty status;
	private SimpleIntegerProperty fatigue;
	
	public Staff(String name) {
		this.name = new SimpleStringProperty(name);
		this.fatigue = new SimpleIntegerProperty(0);
		this.status = new SimpleStringProperty();
	}

	public String getName() {
		return name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public Number getFatigue() {
		return fatigue.getValue();
	}

	public void setFatigue(Number fatigue) {
		this.fatigue.setValue(fatigue);
	}

	public String getStatus() {
		return status.getValue();
	}

	public void setStatus(String status) {
		notifyUpdate("status",this.status,status);
		this.status.setValue(status);
	}

}
