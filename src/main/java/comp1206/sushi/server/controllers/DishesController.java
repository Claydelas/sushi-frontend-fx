package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import comp1206.sushi.common.Dish;
import comp1206.sushi.server.components.NumericTableCell;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

public class DishesController extends MainViewController {

    protected static Dish currentlySelectedDish;

    private static PopOver editRecipeView;

    @FXML
    private AnchorPane recipe;
    @FXML
    private AnchorPane newDishView;
    @FXML
    private TableView<Dish> dishesTable;
    @FXML
    private TableColumn<Dish, String> name;
    @FXML
    private TableColumn<Dish, String> description;
    @FXML
    private TableColumn<Dish, Number> price;
    @FXML
    private TableColumn<Dish, Number> restockThreshold;
    @FXML
    private TableColumn<Dish, Number> restockAmount;
    @FXML
    private TableColumn<Dish, Number> stock;
    @FXML
    private JFXButton editRecipeButton;
    @FXML
    private RecipeController recipeController;
    @FXML
    private JFXTextField nameF;
    @FXML
    private JFXTextField descriptionF;
    @FXML
    private JFXTextField priceF;
    @FXML
    private JFXTextField restockValF;
    @FXML
    private JFXTextField restockAtF;
    @FXML
    private JFXButton newDishButton;


    //static method for hiding the popover from other controllers
    static void hideRecipeView() {
        editRecipeView.hide(Duration.seconds(0.5));
    }

    @FXML
    public void initialize() {

        setupRecipeView();
        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
        NumberValidator numberValidator = new NumberValidator();

        nameF.getValidators().add(requiredFieldValidator);
        descriptionF.getValidators().add(requiredFieldValidator);
        priceF.getValidators().addAll(requiredFieldValidator, numberValidator);
        restockValF.getValidators().addAll(requiredFieldValidator, numberValidator);
        restockAtF.getValidators().addAll(requiredFieldValidator, numberValidator);


        //---------------------Name Column---------------------------------
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        //off-spec, modifies directly due to lack of api implementation
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(e -> dishesTable.getSelectionModel().getSelectedItem().setName(e.getNewValue()));

        //---------------------Description Column---------------------------------
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        //off-spec, modifies directly due to lack of api implementation
        description.setCellFactory(TextFieldTableCell.forTableColumn());
        description.setOnEditCommit(e -> dishesTable.getSelectionModel().getSelectedItem().setDescription(e.getNewValue()));

        //---------------------Price Column---------------------------------
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        //off-spec, modifies directly due to lack of api implementation
        price.setCellFactory(column -> new NumericTableCell<>());
        price.setOnEditCommit(e -> dishesTable.getSelectionModel().getSelectedItem().setPrice(e.getNewValue()));

        //---------------------Restock Threshold Column---------------------------------
        restockThreshold.setCellValueFactory(new PropertyValueFactory<>("restockThreshold"));
        restockThreshold.setCellFactory(column -> new NumericTableCell<>(0, 0));
        restockThreshold.setOnEditCommit(e -> dishesTable.getSelectionModel().getSelectedItem().setRestockThreshold(e.getNewValue()));

        //---------------------Restock Amount Column---------------------------------
        restockAmount.setCellValueFactory(new PropertyValueFactory<>("restockAmount"));
        restockAmount.setCellFactory(c -> new NumericTableCell<>(0, 0));
        restockAmount.setOnEditCommit(e -> dishesTable.getSelectionModel().getSelectedItem().setRestockAmount(e.getNewValue()));

        //---------------------Stock Column---------------------------------
        stock.setCellValueFactory(stock -> new SimpleIntegerProperty(server.getDishStockLevels().get(stock.getValue()).intValue()));

        //wraps the dishes list in a observable one, so table listens for updates on the model
        ObservableList<Dish> dishData = FXCollections.observableList(server.getDishes());
        //sets the table's contents to the observable list
        dishesTable.setItems(dishData);

        //Selecting in the table triggers an update to the recipe editing view
        dishesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                currentlySelectedDish = dishesTable.getSelectionModel().getSelectedItem();
                recipeController.initIngredientList();
                editRecipeView.setTitle(currentlySelectedDish.getName() + "'s recipe");
            }
        });

        //Opens recipe editing view and updates the list of a dish's ingredients
        editRecipeButton.setOnAction(e -> {
            //if no dish was selected in the table, pick the first one as a default
            if (currentlySelectedDish == null) currentlySelectedDish = server.getDishes().get(0);
            //initialises ingredients in dish list in the popover view
            recipeController.initIngredientList();
            //sets the title of the popover to the dish's name
            editRecipeView.setTitle(currentlySelectedDish.getName() + "'s recipe");
            //cancels any cell edit in progress
            dishesTable.edit(-1, null);
            //makes the table not editable to avoid conflicts with popover
            dishesTable.setEditable(false);
            if (!editRecipeView.isShowing()) {
                newDishView.setVisible(false);
                //shows the recipe editing popover
                editRecipeView.show(editRecipeButton);
            }
        });

        //clicking in the table but not on a Dish cancels any ongoing edit
        // dishesTable.focusedProperty().addListener((p, oldFocus, newFocus) -> {
        //    if (newFocus) dishesTable.edit(-1, null);
        // });
        //when the popover is closed, restores the table's editing ability
        editRecipeView.setOnHidden(e -> {
            newDishView.setVisible(true);
            dishesTable.setEditable(true);
        });
        newDishButton.setOnAction(e -> {
            //if all fields are correctly filled, adds the dish to the server
            if (descriptionF.validate() && priceF.validate() && restockValF.validate()
                    && restockAtF.validate() && nameF.validate()) {

                //adds the new dish to the data
                dishData.add(new Dish(nameF.getText(), descriptionF.getText(), Float.valueOf(priceF.getText()),
                        Float.valueOf(restockAtF.getText()), Float.valueOf(restockValF.getText())));

                //prints the data in the server for testing
                System.out.println("Dishes currently in the server: "
                        + server.getDishes() + "\nNewly added: "
                        + server.getDishes().get(server.getDishes().size()-1));
            }
        });
    }

    //configures the popover view
    private void setupRecipeView() {
        //new popover with the recipe pane as content
        editRecipeView = new PopOver(recipe);
        //shows title
        editRecipeView.setHeaderAlwaysVisible(true);
        editRecipeView.setDetachable(false);
        editRecipeView.setCloseButtonEnabled(false);
        editRecipeView.setAutoHide(false);
        editRecipeView.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        //loads styling
        editRecipeView.getRoot().getStylesheets().add("/css/popup.css");
    }
}
