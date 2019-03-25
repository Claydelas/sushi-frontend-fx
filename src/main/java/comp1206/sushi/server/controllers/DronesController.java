package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import comp1206.sushi.common.Drone;
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

public class DronesController extends MainViewController {

    @FXML
    private AnchorPane newDroneView;
    @FXML
    private TableView<Drone> dronesTable;
    @FXML
    private TableColumn<Drone, String> drone;
    @FXML
    private TableColumn<Drone, String> status;
    @FXML
    private TableColumn<Drone, Number> speed;
    @FXML
    private TableColumn<Drone, String> progress;
    @FXML
    private JFXTextField speedF;
    @FXML
    private JFXButton newDroneButton;
    @FXML
    private JFXButton deleteDroneButton;
    @FXML
    private JFXButton addNewDroneButton;

    @FXML
    public void initialize() {
        speedF.getValidators().addAll(new RequiredFieldValidator(), new NumberValidator());

        //---------------------Name Column---------------------------------
        drone.setCellValueFactory(new PropertyValueFactory<>("name"));

        //---------------------Status Column---------------------------------
        status.setCellValueFactory(drone -> new SimpleStringProperty(server.getDroneStatus(drone.getValue())));

        //---------------------Speed Column---------------------------------
        speed.setCellValueFactory(new PropertyValueFactory<>("speed"));

        //---------------------Progress Column---------------------------------
        progress.setCellValueFactory(drone -> Bindings.createStringBinding(() -> server.getDroneProgress(drone.getValue()) + "%"));

        //wraps the dishes list in a observable one, so table listens for updates on the model
        ObservableList<Drone> dronesData = FXCollections.observableList(server.getDrones());
        //sets the table's contents to the observable list
        dronesTable.setItems(dronesData);

        newDroneButton.setOnAction(e -> {
            if (newDroneView.isVisible()) newDroneView.setVisible(false);
            else newDroneView.setVisible(true);
        });

        deleteDroneButton.setOnAction(actionEvent -> {
            Drone tempSelect = dronesTable.getSelectionModel().getSelectedItem();
            if (tempSelect != null) {
                try {
                    server.removeDrone(tempSelect);
                    dronesTable.getSelectionModel().clearSelection();
                    newDroneView.setVisible(false);
                    dronesTable.refresh();
                } catch (ServerInterface.UnableToDeleteException e) {
                    showToastNotification("Cannot remove a drone while it's in flight!");
                }
            }
        });

        addNewDroneButton.setOnAction(e -> {

            if (speedF.validate()) {

                dronesData.add(new Drone(Float.valueOf(speedF.getText())));
                newDroneView.setVisible(false);

                //clears input
                speedF.setText("");

                showToastNotification("Drone added successfully!");

                //prints the data in the server for testing
                System.out.println("Drones currently in the server: "
                        + server.getDrones() + "\nNewly added: "
                        + server.getDrones().get(server.getDrones().size() - 1));
            }
        });
    }

    @Override
    public void refresh() {
        dronesTable.refresh();
    }
}
