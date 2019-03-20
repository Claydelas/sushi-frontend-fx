package comp1206.sushi.common;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

public class Drone extends Model {

    private SimpleFloatProperty speed;
    private SimpleFloatProperty progress;

    private SimpleFloatProperty capacity;
    private SimpleFloatProperty battery;

    private SimpleStringProperty status;

    private Postcode source;
    private Postcode destination;

    public Drone(Number speed) {
        this.speed = new SimpleFloatProperty(speed.floatValue());
        this.capacity = new SimpleFloatProperty(1);
        this.battery = new SimpleFloatProperty(100);
    }

    public Number getSpeed() {
        return speed.getValue();
    }

    public void setSpeed(Number speed) {
        this.speed.setValue(speed);
    }

    public Number getProgress() {
        return progress.getValue();
    }

    public void setProgress(Number progress) {
        this.progress.setValue(progress);
    }

    @Override
    public String getName() {
        return "Drone (" + getSpeed() + " speed)";
    }

    public Postcode getSource() {
        return source;
    }

    public void setSource(Postcode source) {
        this.source = source;
    }

    public Postcode getDestination() {
        return destination;
    }

    public void setDestination(Postcode destination) {
        this.destination = destination;
    }

    public Number getCapacity() {
        return capacity.getValue();
    }

    public void setCapacity(Number capacity) {
        this.capacity.setValue(capacity);
    }

    public Number getBattery() {
        return battery.getValue();
    }

    public void setBattery(Number battery) {
        this.battery.setValue(battery);
    }

    public String getStatus() {
        return status.getValue();
    }

    public void setStatus(String status) {
        notifyUpdate("status", this.status, status);
        this.status.setValue(status);
    }

}
