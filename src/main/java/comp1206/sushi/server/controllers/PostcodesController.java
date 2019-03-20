package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import comp1206.sushi.common.Dish;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class PostcodesController extends MainViewController {

    protected static Dish currentlySelectedDish;
    @FXML
    private AnchorPane recipeView;
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
    private RecipeController recipeViewController;
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
    private JFXButton addNewDishButton;
    @FXML
    private JFXButton newDishButton;
    @FXML
    private JFXButton deleteDishButton;
    private JFXTextField[] inputfields;

    @FXML
    public void initialize() {

    }
    @Override
    public void refresh(){
        //dishesTable.refresh();
    }
}
