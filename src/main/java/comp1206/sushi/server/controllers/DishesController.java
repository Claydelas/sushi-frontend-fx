package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXButton;
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
    private static PopOver popOver;
    @FXML
    private AnchorPane recipe;
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

    static void hideRecipeView() {
        popOver.hide(Duration.seconds(0.5));
    }

    @FXML
    public void initialize() {

        setupRecipeView();

        //---------------------Name Column---------------------------------
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(e -> dishesTable.getSelectionModel().getSelectedItem().setName(e.getNewValue()));

        //---------------------Description Column---------------------------------
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        description.setCellFactory(TextFieldTableCell.forTableColumn());
        description.setOnEditCommit(e -> dishesTable.getSelectionModel().getSelectedItem().setDescription(e.getNewValue()));

        //---------------------Price Column---------------------------------
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
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

        ObservableList<Dish> dishData = FXCollections.observableList(server.getDishes());
        dishesTable.setItems(dishData);

        //Selecting in the table triggers an update to the recipe editing view
        dishesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                currentlySelectedDish = dishesTable.getSelectionModel().getSelectedItem();
                recipeController.initIngredientList();
                popOver.setTitle(currentlySelectedDish.getName() + "'s recipe");
            }
        });

        //Opens recipe editing view and updates the list of a dish's ingredients
        editRecipeButton.setOnAction(e -> {
            //if no dish was selected in the table, pick the first one as a default
            if (currentlySelectedDish == null) currentlySelectedDish = server.getDishes().get(0);
            //initialises ingredients in dish list in the popover view
            recipeController.initIngredientList();
            //sets the title of the popover to the dish's name
            popOver.setTitle(currentlySelectedDish.getName() + "'s recipe");
            //cancels any cell edit in progress
            dishesTable.edit(-1, null);
            //makes the table not editable to avoid conflicts with popover
            dishesTable.setEditable(false);
            //shows the recipe editing popover
            popOver.show(editRecipeButton);
        });

        //clicking in the table but not on a Dish cancels any ongoing edit
        dishesTable.focusedProperty().addListener((p, oldFocus, newFocus) -> {
            if (newFocus) dishesTable.edit(-1, null);
        });
        //when the popover is closed, restores the table's editing ability
        popOver.setOnHidden(e -> dishesTable.setEditable(true));
    }

    //configures the popover view
    private void setupRecipeView() {
        //new popover with the recipe pane as content
        popOver = new PopOver(recipe);
        //shows title
        popOver.setHeaderAlwaysVisible(true);
        popOver.setDetachable(false);
        popOver.setCloseButtonEnabled(false);
        popOver.setAutoHide(false);
        //loads styling
        popOver.getRoot().getStylesheets().add("/css/popup.css");
    }
}
