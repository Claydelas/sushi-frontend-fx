package comp1206.sushi.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class Postcode extends Model {

    private SimpleStringProperty name;
    private Map<String, Double> latLong;
    private SimpleFloatProperty distance;

    public Postcode(String code) {
        this.name = new SimpleStringProperty(code);
        calculateLatLong();
        this.distance = new SimpleFloatProperty(0);
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

    public Map<String, Double> getLatLong() {
        return this.latLong;
    }

    protected void calculateDistance(Restaurant restaurant) {
        //This function needs implementing
        Postcode destination = restaurant.getLocation();
        this.distance = new SimpleFloatProperty((float) convert(destination.getLatLong().get("lat"), destination.getLatLong().get("long"), getLatLong().get("lat"), getLatLong().get("long")));
        System.out.println("Distance between '" + getName() + "' and '" + destination + "' -> " + getDistance() + "km");
    }

    protected void calculateLatLong() {

        this.latLong = new HashMap<>();
        String sURL = "https://www.southampton.ac.uk/~ob1a12/postcode/postcode.php?postcode=" + getName().replaceAll(" ", "");

        try {
            URL url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonObject = root.getAsJsonObject();

            System.out.println("Raw GET content: " + root);

            latLong.put("lat", jsonObject.get("lat").getAsDouble());
            latLong.put("long", jsonObject.get("long").getAsDouble());

            System.out.println("Mapped entries: " + latLong.entrySet());

        } catch (Exception e) {
        }
    }

    private double convert(
            double lat1, double lng1, double lat2, double lng2) {
        int r = 6371; // average radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r * c;
        return d;
    }

}
