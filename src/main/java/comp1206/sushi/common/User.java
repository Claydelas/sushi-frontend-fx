package comp1206.sushi.common;

import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.User;
import javafx.beans.property.SimpleStringProperty;

public class User extends Model {
	
	private SimpleStringProperty name;
	private SimpleStringProperty password;
	private SimpleStringProperty address;
	private Postcode postcode;

	public User(String username, String password, String address, Postcode postcode) {
		this.name = new SimpleStringProperty(username);
		this.password = new SimpleStringProperty(password);
		this.address = new SimpleStringProperty(address);
		this.postcode = postcode;
	}

	public String getName() {
		return name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public Number getDistance() {
		return postcode.getDistance();
	}

	public Postcode getPostcode() {
		return this.postcode;
	}
	
	public void setPostcode(Postcode postcode) {
		this.postcode = postcode;
	}

}
