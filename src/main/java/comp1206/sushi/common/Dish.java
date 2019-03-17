package comp1206.sushi.common;

import java.util.HashMap;
import java.util.Map;

import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Ingredient;
import javafx.beans.property.*;
import javafx.collections.ObservableMap;

public class Dish extends Model {

	private SimpleStringProperty name;
	private SimpleStringProperty description;
	private SimpleFloatProperty price;
	private Map<Ingredient,Number> recipe;
	private SimpleIntegerProperty restockThreshold;
	private SimpleIntegerProperty restockAmount;

	public Dish(String name, String description, Number price, Number restockThreshold, Number restockAmount) {
		this.name = new SimpleStringProperty(name);
		this.description = new SimpleStringProperty(description);
		this.price = new SimpleFloatProperty(price.floatValue());
		this.restockThreshold = new SimpleIntegerProperty(restockThreshold.intValue());
		this.restockAmount = new SimpleIntegerProperty(restockAmount.intValue());
		this.recipe = new HashMap<Ingredient,Number>();
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

	public Map <Ingredient,Number> getRecipe() {
		return recipe;
	}

	public void setRecipe(Map <Ingredient,Number> recipe) {
		this.recipe = recipe;
	}

	public void setRestockThreshold(Number restockThreshold) {
		this.restockThreshold.setValue(restockThreshold);
	}
	
	public void setRestockAmount(Number restockAmount) {
		this.restockAmount.setValue(restockAmount);
	}

	public Number getRestockThreshold() {
		return this.restockThreshold.getValue();
	}

	public Number getRestockAmount() {
		return this.restockAmount.getValue();
	}

}
