package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXButton;
import comp1206.sushi.common.User;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class UsersController extends MainViewController {

    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> user;
    @FXML
    private TableColumn<User, String> address;
    @FXML
    private TableColumn<User, String> distance;
    @FXML
    private JFXButton removeUserButton;

    @FXML
    public void initialize() {

        //---------------------Name Column---------------------------------
        user.setCellValueFactory(new PropertyValueFactory<>("name"));

        //---------------------Address Column---------------------------------
        address.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getPostcode().getName()));

        //---------------------Distance Column---------------------------------
        distance.setCellValueFactory(user -> Bindings.createStringBinding(() -> user.getValue().getPostcode().getDistance() + " km"));
        //distance.setCellValueFactory(order -> new SimpleFloatProperty(order.getValue().getPostcode().getDistance().floatValue()));

        ObservableList<User> usersData = FXCollections.observableList(server.getUsers());
        usersTable.setItems(usersData);

        removeUserButton.setOnAction(actionEvent -> usersData.remove(usersTable.getSelectionModel().getSelectedItem()));
    }

    @Override
    public void refresh() {
        usersTable.refresh();
    }
}
