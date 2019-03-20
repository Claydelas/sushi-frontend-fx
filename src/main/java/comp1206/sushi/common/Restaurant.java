package comp1206.sushi.common;

import javafx.beans.property.SimpleStringProperty;

public class Restaurant {

	private SimpleStringProperty name;
	private Postcode location;

	public Restaurant(String name, Postcode location) {
		this.name = new SimpleStringProperty(name);
		this.location = location;
	}
	
	public String getName() {
		return name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public Postcode getLocation() {
		return location;
	}

	public void setLocation(Postcode location) {
		this.location = location;
	}
	
}
