package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.server.ServerInterface;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class SuppliersController extends MainViewController {

    @FXML
    private AnchorPane newSupplierView;
    @FXML
    private TableView<Supplier> suppliersTable;
    @FXML
    private TableColumn<Supplier, String> supplier;
    @FXML
    private TableColumn<Supplier, String> postcode;
    @FXML
    private TableColumn<Supplier, String> distance;
    @FXML
    private JFXTextField nameF;
    @FXML
    private JFXComboBox<Postcode> postcodeF;
    @FXML
    private JFXButton addNewSupplierButton;
    @FXML
    private JFXButton newSupplierButton;
    @FXML
    private JFXButton deleteSupplierButton;

    @FXML
    public void initialize() {

        //---------------------Name Column---------------------------------
        supplier.setCellValueFactory(new PropertyValueFactory<>("name"));

        //---------------------Postcode Column---------------------------------
        postcode.setCellValueFactory(supplier -> new SimpleStringProperty(supplier.getValue().getPostcode().getName()));

        //---------------------Distance Column---------------------------------
        distance.setCellValueFactory(user -> Bindings.createStringBinding(() -> user.getValue().getPostcode().getDistance() + " km"));

        ObservableList<Supplier> suppliersData = FXCollections.observableList(server.getSuppliers());
        suppliersTable.setItems(suppliersData);

        postcodeF.setItems(FXCollections.observableList(server.getPostcodes()));

        newSupplierButton.setOnAction(e -> {
            if (newSupplierView.isVisible()) newSupplierView.setVisible(false);
            else newSupplierView.setVisible(true);
        });

        deleteSupplierButton.setOnAction(actionEvent -> {

            Supplier tempSelect = suppliersTable.getSelectionModel().getSelectedItem();
            if (tempSelect != null) {
                if (server.getIngredients().stream().noneMatch(ingredient -> ingredient.getSupplier().equals(tempSelect))) {
                    try {
                        server.removeSupplier(tempSelect);
                        suppliersTable.getSelectionModel().clearSelection();
                        suppliersTable.refresh();
                    } catch (ServerInterface.UnableToDeleteException e) {
                        showToastNotification("Ingredients still depend on this supplier!");
                    }
                } else showToastNotification("Ingredients still depend on this supplier!");
            } else showToastNotification("Select a supplier first!");
        });

        addNewSupplierButton.setOnAction(e -> {
            if (nameF.validate() && postcodeF.validate()) {

                suppliersData.add(new Supplier(nameF.getText(), postcodeF.getSelectionModel().getSelectedItem()));
                newSupplierView.setVisible(false);

                //clears inputs
                nameF.setText("");
                postcodeF.getSelectionModel().clearSelection();

                showToastNotification("Supplier added successfully!");

                //prints the data in the server for testing
                System.out.println("Suppliers currently in the server: "
                        + server.getSuppliers() + "\nNewly added: "
                        + server.getSuppliers().get(server.getSuppliers().size() - 1));
            }
        });

        RequiredFieldValidator evalInput = new RequiredFieldValidator();
        nameF.getValidators().add(evalInput);
        postcodeF.getValidators().add(evalInput);
    }

    @Override
    public void refresh() {
        suppliersTable.refresh();
    }

    public void clearPostcode(){
        postcodeF.getSelectionModel().clearSelection();
    }
}
