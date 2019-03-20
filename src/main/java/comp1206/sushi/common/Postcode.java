package comp1206.sushi.common;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.HashMap;
import java.util.Map;

public class Postcode extends Model {

	private SimpleStringProperty name;
	private Map<String,Double> latLong;
	private SimpleDoubleProperty distance;

	public Postcode(String code) {
		this.name = new SimpleStringProperty(code);
		calculateLatLong();
		this.distance = new SimpleDoubleProperty(0);
	}
	
	public Postcode(String code, Restaurant restaurant) {
		this.name = new SimpleStringProperty(code);
		calculateLatLong();
		calculateDistance(restaurant);
	}
	
	@Override
	public String getName() {
		return this.name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}
	
	public Number getDistance() {
		return this.distance.getValue();
	}

	public Map<String,Double> getLatLong() {
		return this.latLong;
	}
	
	protected void calculateDistance(Restaurant restaurant) {
		//This function needs implementing
		Postcode destination = restaurant.getLocation();
		this.distance.setValue(0);
	}
	
	protected void calculateLatLong() {
		//This function needs implementing
		this.latLong = new HashMap<String,Double>();
		latLong.put("lat", 0d);
		latLong.put("lon", 0d);
		this.distance.setValue(0);
	}
	
}
