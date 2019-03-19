package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import comp1206.sushi.common.Ingredient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class RecipeController extends DishesController implements Initializable {

    @FXML
    private JFXButton plus;
    @FXML
    private JFXButton minus;
    private ObservableSet<Ingredient> ingredientsInDish;
    @FXML
    private JFXListView<Ingredient> availableIngredientsList;
    @FXML
    private JFXListView<Ingredient> ingredientsInDishList;
    @FXML
    private JFXSlider quantitySlider;
    @FXML
    private JFXButton confirmRecipe;
    private Ingredient currentlySelectedIngredient;
    @FXML
    private Label ingredientUnit;

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

        //removes a ingredient from a dish and updates the list
        minus.setOnAction(e -> {
            Ingredient selectedIngredient = ingredientsInDishList.getSelectionModel().getSelectedItem();
            if (selectedIngredient != null && ingredientsInDish.contains(selectedIngredient)) {
                server.removeIngredientFromDish(currentlySelectedDish, selectedIngredient);
                updateIngredientList();
            }
            //if a selected ingredient is deleted, move the selection the first ingredient in the list
            if (ingredientsInDishList.getSelectionModel().getSelectedItem() == null) {
                ingredientsInDishList.getSelectionModel().selectFirst();
            }
        });

        ingredientsInDishList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                currentlySelectedIngredient = newSelection;
                quantitySlider.setValue(server.getRecipe(currentlySelectedDish).get(newSelection).doubleValue());
                ingredientUnit.setText(currentlySelectedIngredient.getUnit());
            }
        });
        quantitySlider.valueProperty().addListener(((observableValue, number, newNumber) -> {
            server.addIngredientToDish(currentlySelectedDish, currentlySelectedIngredient, newNumber);
        }));
        confirmRecipe.setOnAction(e -> {
            hideRecipeView();
            availableIngredientsList.requestFocus();
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