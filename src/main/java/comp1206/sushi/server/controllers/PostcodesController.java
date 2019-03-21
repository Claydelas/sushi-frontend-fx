package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Restaurant;
import comp1206.sushi.common.Staff;
import comp1206.sushi.server.ServerInterface;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

import javax.naming.Binding;

public class PostcodesController extends MainViewController {

    @FXML
    private AnchorPane newPostcodeView;
    @FXML
    private TableView<Postcode> postcodeTable;
    @FXML
    private TableColumn<Postcode, Number> postcode;
    @FXML
    private TableColumn<Postcode, Number> latitude;
    @FXML
    private TableColumn<Postcode, Number> longitude;
    @FXML
    private TableColumn<Postcode, String> distance;
    @FXML
    private JFXTextField postcodeF;
    @FXML
    private JFXButton newPostcodeButton;
    @FXML
    private JFXButton deletePostcodeButton;
    @FXML
    private JFXButton addNewPostcodeButton;

    @FXML
    public void initialize() {

        RegexValidator rg = new RegexValidator();
        rg.setRegexPattern("^[A-Z]{1,2}\\d[A-Z\\d]? ?\\d[A-Z]{2}$");
        postcodeF.getValidators().addAll(new RequiredFieldValidator(), rg);

        //---------------------Postcode Column---------------------------------
        postcode.setCellValueFactory(new PropertyValueFactory<>("name"));

        //---------------------Latitude Column---------------------------------
        latitude.setCellValueFactory(latitude -> new SimpleDoubleProperty(latitude.getValue().getLatLong().get("lat")));

        //---------------------Longitude Column---------------------------------
        longitude.setCellValueFactory(longitude -> new SimpleDoubleProperty(longitude.getValue().getLatLong().get("long")));

        //---------------------Distance Column---------------------------------
        distance.setCellValueFactory(distance -> Bindings.createStringBinding(() -> distance.getValue().getDistance() + " km"));
        //distance.setCellValueFactory(new PropertyValueFactory<>("distance")); //change declaration to number

        //wraps the dishes list in a observable one, so table listens for updates on the model
        ObservableList<Postcode> postcodeData = FXCollections.observableList(server.getPostcodes());
        //sets the table's contents to the observable list
        postcodeTable.setItems(postcodeData);

        newPostcodeButton.setOnAction(e -> {
            if (newPostcodeView.isVisible()) newPostcodeView.setVisible(false);
            else newPostcodeView.setVisible(true);
            //testing
            postcodeData.add(new Postcode("SO14 0AX", new Restaurant("name", new Postcode("SO14 0BD"))));
        });

        deletePostcodeButton.setOnAction(actionEvent -> {
            Postcode tempSelect = postcodeTable.getSelectionModel().getSelectedItem();
            if (tempSelect != null) {
                try {
                    server.removePostcode(tempSelect);
                    postcodeTable.getSelectionModel().clearSelection();
                    newPostcodeView.setVisible(false);
                    postcodeTable.refresh();
                } catch (ServerInterface.UnableToDeleteException e) {
                    showToastNotification("Postcode is still in use!");
                }
            }
        });

        addNewPostcodeButton.setOnAction(e -> {

            if (postcodeF.validate()) {
                String valid = postcodeF.getText();
                        //.toLowerCase().replaceAll(" ", "");
                if (server.getPostcodes().stream().anyMatch(obj -> obj.getName().equals(valid))) {
                    showToastNotification("Postcode already exists!");
                } else {

                    //adds the new postcode to the data
                    postcodeData.add(new Postcode(postcodeF.getText().toUpperCase()));
                    newPostcodeView.setVisible(false);

                    showToastNotification("Successfully added " + postcodeF.getText() + "!");
                    //clears input
                    postcodeF.setText("");

                    //prints the data in the server for testing
                    System.out.println("Postcodes currently in the server: "
                            + server.getPostcodes() + "\nNewly added: "
                            + server.getPostcodes().get(server.getPostcodes().size() - 1));
                }
            }
        });
    }

    @Override
    public void refresh() {
        //dishesTable.refresh();
    }
}
