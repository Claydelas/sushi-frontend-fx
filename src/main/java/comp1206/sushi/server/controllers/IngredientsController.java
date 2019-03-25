package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.server.ServerInterface;
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

public class IngredientsController extends MainViewController {

    @FXML
    private AnchorPane newIngredientView;
    @FXML
    private TableView<Ingredient> ingredientsTable;
    @FXML
    private TableColumn<Ingredient, String> name;
    @FXML
    private TableColumn<Ingredient, String> unit;
    @FXML
    private TableColumn<Ingredient, Supplier> supplier;
    @FXML
    private TableColumn<Ingredient, Number> restockThreshold;
    @FXML
    private TableColumn<Ingredient, Number> restockAmount;
    @FXML
    private TableColumn<Ingredient, Number> stock;
    @FXML
    private JFXTextField nameF;
    @FXML
    private JFXTextField unitF;
    @FXML
    private JFXComboBox<Supplier> supplierF;
    @FXML
    private JFXTextField restockValF;
    @FXML
    private JFXTextField restockAtF;
    @FXML
    private JFXButton addNewIngredientButton;
    @FXML
    private JFXButton newIngredientButton;
    @FXML
    private JFXButton deleteIngredientButton;
    private JFXTextField[] input;

    @FXML
    public void initialize() {
        input = new JFXTextField[]{nameF, restockValF, unitF, restockAtF};

        //---------------------Name Column---------------------------------
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        //name.setCellFactory(TextFieldTableCell.forTableColumn());
        //name.setOnEditCommit(e -> ingredientsTable.getSelectionModel().getSelectedItem().setName(e.getNewValue()));

        //---------------------Unit Column---------------------------------
        unit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        //unit.setCellFactory(TextFieldTableCell.forTableColumn());
        //unit.setOnEditCommit(e -> ingredientsTable.getSelectionModel().getSelectedItem().setUnit(e.getNewValue()));

        //---------------------Supplier Column---------------------------------
        supplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        //supplier.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableList(server.getSuppliers())));
        //supplier.setOnEditCommit(e -> ingredientsTable.getSelectionModel().getSelectedItem().setSupplier(e.getNewValue()));

        //---------------------Restock Threshold Column---------------------------------
        restockThreshold.setCellValueFactory(new PropertyValueFactory<>("restockThreshold"));
        restockThreshold.setCellFactory(column -> new NumericTableCell<>(0, 0));
        restockThreshold.setOnEditCommit(e -> ingredientsTable.getSelectionModel().getSelectedItem().setRestockThreshold(e.getNewValue()));

        //---------------------Restock Amount Column---------------------------------
        restockAmount.setCellValueFactory(new PropertyValueFactory<>("restockAmount"));
        restockAmount.setCellFactory(c -> new NumericTableCell<>(0, 0));
        restockAmount.setOnEditCommit(e -> ingredientsTable.getSelectionModel().getSelectedItem().setRestockAmount(e.getNewValue()));

        //---------------------Stock Column---------------------------------
        stock.setCellValueFactory(stock -> new SimpleIntegerProperty(server.getIngredientStockLevels().get(stock.getValue()).intValue()));

        //wraps the dishes list in a observable one, so table listens for updates
        ObservableList<Ingredient> ingredientsData = FXCollections.observableList(server.getIngredients());
        //sets the table's contents to the observable list
        ingredientsTable.setItems(ingredientsData);

        supplierF.setItems(FXCollections.observableList(server.getSuppliers()));

        newIngredientButton.setOnAction(e -> {
            if (newIngredientView.isVisible()) newIngredientView.setVisible(false);
            else newIngredientView.setVisible(true);
            //cancels any cell edit in progress
            ingredientsTable.edit(-1, null);
        });

        deleteIngredientButton.setOnAction(actionEvent -> {

            Ingredient tempSelect = ingredientsTable.getSelectionModel().getSelectedItem();

            if (tempSelect != null) {

                for (Dish dish : server.getDishes()) {
                    if (dish.getRecipe().keySet().contains(tempSelect)) {
                        showToastNotification("This ingredient is still part of a recipe!");
                        return;
                    }
                }
                try {
                    server.removeIngredient(tempSelect);
                    ingredientsTable.getSelectionModel().clearSelection();
                    ingredientsTable.refresh();
                } catch (ServerInterface.UnableToDeleteException e) {
                    showToastNotification("Remove the ingredient from all recipes first!");
                }
            } else showToastNotification("Select an ingredient first!");
        });

        addNewIngredientButton.setOnAction(e -> {
            if (nameF.validate() && restockValF.validate() && unitF.validate() && restockAtF.validate() && supplierF.validate()) {

                ingredientsData.add(new Ingredient(nameF.getText(), unitF.getText(), supplierF.getSelectionModel().getSelectedItem(),
                        Float.valueOf(restockAtF.getText()), Float.valueOf(restockValF.getText())));
                newIngredientView.setVisible(false);

                //clears inputs
                for (JFXTextField field : input) {
                    field.setText("");
                }
                supplierF.getSelectionModel().clearSelection();

                showToastNotification("Ingredient added successfully!");

                //prints the data in the server for testing
                System.out.println("Ingredients currently in the server: "
                        + server.getIngredients() + "\nNewly added: "
                        + server.getIngredients().get(server.getIngredients().size() - 1));
            }
        });
        RequiredFieldValidator evalInput = new RequiredFieldValidator();
        NumberValidator evalNum = new NumberValidator();

        supplierF.getValidators().add(evalInput);
        nameF.getValidators().add(evalInput);
        unitF.getValidators().add(evalInput);
        restockValF.getValidators().addAll(evalInput, evalNum);
        restockAtF.getValidators().addAll(evalInput, evalNum);
    }

    @Override
    public void refresh() {
        ingredientsTable.refresh();
    }

    public void clearSupplier(){
        supplierF.getSelectionModel().clearSelection();
    }
}
