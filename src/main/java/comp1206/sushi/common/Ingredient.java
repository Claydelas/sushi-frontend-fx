package comp1206.sushi.common;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Ingredient extends Model {

    private SimpleStringProperty name;
    private SimpleStringProperty unit;
    private Supplier supplier;
    private SimpleIntegerProperty restockThreshold;
    private SimpleIntegerProperty restockAmount;

    public Ingredient(String name, String unit, Supplier supplier, Number restockThreshold,
                      Number restockAmount) {
        this.name = new SimpleStringProperty(name);
        this.unit = new SimpleStringProperty(unit);
        this.supplier = supplier;
        this.restockThreshold = new SimpleIntegerProperty(restockThreshold.intValue());
        this.restockAmount = new SimpleIntegerProperty(restockAmount.intValue());
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public String getUnit() {
        return unit.getValue();
    }

    public void setUnit(String unit) {
        this.unit.setValue(unit);
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Number getRestockThreshold() {
        return restockThreshold.getValue();
    }

    public void setRestockThreshold(Number restockThreshold) {
        this.restockThreshold.setValue(restockThreshold);
    }

    public Number getRestockAmount() {
        return restockAmount.getValue();
    }

    public void setRestockAmount(Number restockAmount) {
        this.restockAmount.setValue(restockAmount);
    }

}
