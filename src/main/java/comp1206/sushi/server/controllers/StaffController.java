package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Staff;
import comp1206.sushi.server.ServerInterface;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

public class StaffController extends MainViewController {

    @FXML
    private AnchorPane newStaffView;
    @FXML
    private TableView<Staff> staffTable;
    @FXML
    private TableColumn<Staff, String> name;
    @FXML
    private TableColumn<Staff, String> status;
    @FXML
    private TableColumn<Staff, Number> fatigue;
    @FXML
    private JFXTextField nameF;
    @FXML
    private JFXButton newStaffButton;
    @FXML
    private JFXButton deleteStaffButton;
    @FXML
    private JFXButton addNewStaffButton;

    @FXML
    public void initialize() {

        nameF.getValidators().add(new RequiredFieldValidator());

        //---------------------Name Column---------------------------------
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        //off-spec, modifies directly due to lack of api implementation
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(e -> staffTable.getSelectionModel().getSelectedItem().setName(e.getNewValue()));

        //---------------------Status Column---------------------------------
        status.setCellValueFactory(staff -> new SimpleStringProperty(server.getStaffStatus(staff.getValue())));

        //---------------------Fatigue Column---------------------------------
        fatigue.setCellValueFactory(new PropertyValueFactory<>("fatigue"));

        //wraps the dishes list in a observable one, so table listens for updates on the model
        ObservableList<Staff> staffData = FXCollections.observableList(server.getStaff());
        //sets the table's contents to the observable list
        staffTable.setItems(staffData);

        newStaffButton.setOnAction(e -> {
            if (newStaffView.isVisible()) newStaffView.setVisible(false);
            else newStaffView.setVisible(true);
        });

        deleteStaffButton.setOnAction(actionEvent -> {
            Staff tempSelect = staffTable.getSelectionModel().getSelectedItem();
            if (tempSelect != null) {
                try {
                    server.removeStaff(tempSelect);
                    staffTable.getSelectionModel().clearSelection();
                    newStaffView.setVisible(false);
                    staffTable.refresh();
                } catch (ServerInterface.UnableToDeleteException e) {
                    showToastNotification("Cannot fire staff members while they are still working!");
                }
            }
        });

        addNewStaffButton.setOnAction(e -> {
            //if all fields are correctly filled, adds the dish to the server
            if (nameF.validate()) {

                //adds the new dish to the data
                staffData.add(new Staff(nameF.getText()));
                newStaffView.setVisible(false);

                showToastNotification("Welcome aboard " + nameF.getText() + "!");
                //clears input
                nameF.setText("");

                //prints the data in the server for testing
                System.out.println("Staff currently in the server: "
                        + server.getStaff() + "\nNewly added: "
                        + server.getStaff().get(server.getStaff().size() - 1));
            }
        });
    }
    @Override
    public void refresh(){
        staffTable.refresh();
    }
}
