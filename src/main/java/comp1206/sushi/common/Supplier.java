package comp1206.sushi.common;

import javafx.beans.property.SimpleStringProperty;

public class Supplier extends Model {

	private SimpleStringProperty name;
	private Postcode postcode;

	public Supplier(String name, Postcode postcode) {
		this.name = new SimpleStringProperty(name);
		this.postcode = postcode;
	}

	public String getName() {
		return name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public Postcode getPostcode() {
		return this.postcode;
	}
	
	public void setPostcode(Postcode postcode) {
		this.postcode = postcode;
	}

	public Number getDistance() {
		return postcode.getDistance();
	}

}
