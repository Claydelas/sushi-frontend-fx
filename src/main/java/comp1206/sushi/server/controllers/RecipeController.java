package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class RecipeController extends DishesController implements Initializable {

    @FXML
    private JFXButton plus;
    private ObservableSet<Ingredient> ingredientsInDish;
    @FXML
    private JFXListView<Ingredient> availableIngredientsList;
    @FXML
    private JFXListView<Ingredient> ingredientsInDishList;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        //Populates the left list view with all available ingredients in the server
        ObservableList<Ingredient> availableIngredients = FXCollections.observableList(server.getIngredients());
        availableIngredientsList.setItems(availableIngredients);

        //when pressing the plus button, adds the selected ingredient to the dish
        plus.setOnAction(e -> {
            Ingredient selectedIngredient = availableIngredientsList.getSelectionModel().getSelectedItem();
            if (selectedIngredient != null && !ingredientsInDish.contains(selectedIngredient)) {
                server.addIngredientToDish(currentlySelectedDish, selectedIngredient, 1);
                updateIngredientList();
            }
        });
    }

    void initIngredientList() {
        ingredientsInDish = FXCollections.observableSet(server.getRecipe(currentlySelectedDish).keySet());
        updateIngredientList();
    }

    private void updateIngredientList() {
        ingredientsInDishList.setItems(FXCollections.observableArrayList(ingredientsInDish));
    }
}