package comp1206.sushi.common;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.HashMap;
import java.util.Map;

public class Dish extends Model {

    private SimpleStringProperty name;
    private SimpleStringProperty description;
    private SimpleFloatProperty price;
    private Map<Ingredient, Number> recipe;
    private SimpleIntegerProperty restockThreshold;
    private SimpleIntegerProperty restockAmount;

    public Dish(String name, String description, Number price, Number restockThreshold, Number restockAmount) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.price = new SimpleFloatProperty(price.floatValue());
        this.restockThreshold = new SimpleIntegerProperty(restockThreshold.intValue());
        this.restockAmount = new SimpleIntegerProperty(restockAmount.intValue());
        this.recipe = new HashMap<>();
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public String getDescription() {
        return description.getValue();
    }

    public void setDescription(String description) {
        this.description.setValue(description);
    }

    public Number getPrice() {
        return price.getValue();
    }

    public void setPrice(Number price) {
        this.price.setValue(price);
    }

    public Map<Ingredient, Number> getRecipe() {
        return recipe;
    }

    public void setRecipe(Map<Ingredient, Number> recipe) {
        this.recipe = recipe;
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
