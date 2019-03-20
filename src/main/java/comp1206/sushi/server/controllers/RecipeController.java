package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import comp1206.sushi.common.Ingredient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RecipeController extends DishesController {

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
    public void initialize() {

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

        //selecting an ingredient in the list updates the instance variable, updates the slider and unit label
        ingredientsInDishList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                quantitySlider.setDisable(false);
                currentlySelectedIngredient = newSelection;
                quantitySlider.setValue(server.getRecipe(currentlySelectedDish).get(newSelection).doubleValue());
                ingredientUnit.setText(currentlySelectedIngredient.getUnit());
            }
        });
        //listens for updates on the slider and updates the ingredient's recipe accordingly
        quantitySlider.valueProperty().addListener(((observableValue, number, newNumber) -> {
            if(currentlySelectedIngredient != null)
            server.addIngredientToDish(currentlySelectedDish, currentlySelectedIngredient, newNumber);
            System.out.println(server.getRecipe(currentlySelectedDish));

        }));
        //hides the popover and requests focus to reset the button's rippler
        confirmRecipe.setOnAction(e -> {
            hideRecipeView();
            availableIngredientsList.requestFocus();
        });
        if (currentlySelectedIngredient == null) quantitySlider.setDisable(true);
    }

    //sets the right list to display the currently selected dish's recipe
    void initIngredientList() {
        ingredientsInDish = FXCollections.observableSet(server.getRecipe(currentlySelectedDish).keySet());
        updateIngredientList();
    }

    //only updates the ingredients in dish list, without requesting info from the server
    private void updateIngredientList() {
        ingredientsInDishList.setItems(FXCollections.observableArrayList(ingredientsInDish));
    }
}