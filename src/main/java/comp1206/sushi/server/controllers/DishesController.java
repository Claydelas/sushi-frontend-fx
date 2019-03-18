package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXButton;
import comp1206.sushi.common.Dish;
import comp1206.sushi.server.components.NumericTableCell;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.PopOver;

import java.net.URL;
import java.util.ResourceBundle;

public class DishesController extends MainViewController implements Initializable {

    private PopOver popOver;
    @FXML private AnchorPane recipe;
    @FXML private TableView<Dish> dishesTable;
    @FXML private TableColumn<Dish, String> name;
    @FXML private TableColumn<Dish, String> description;
    @FXML private TableColumn<Dish, Number> price;
    @FXML private TableColumn<Dish, Number> restockThreshold;
    @FXML private TableColumn<Dish, Number> restockAmount;
    @FXML private TableColumn<Dish, Number> stock;
    @FXML private JFXButton editRecipeButton;
    protected Dish currentlySelectedDish;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        popOver = new PopOver(recipe);
        popOver.setHeaderAlwaysVisible(true);

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

        dishesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) currentlySelectedDish = dishesTable.getSelectionModel().getSelectedItem();
            popOver.setTitle(currentlySelectedDish.getName() + "'s recipe");
        });

        editRecipeButton.setOnAction(e -> {
            if (currentlySelectedDish != null) popOver.show(editRecipeButton);
        });
    }
}
