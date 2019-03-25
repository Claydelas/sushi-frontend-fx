package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXButton;
import comp1206.sushi.common.Order;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class OrdersController extends MainViewController {

    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, String> date;
    @FXML
    private TableColumn<Order, String> distance;
    @FXML
    private TableColumn<Order, String> price;
    @FXML
    private TableColumn<Order, String> status;
    @FXML
    private JFXButton clearOrdersButton;

    @FXML
    public void initialize() {

        //---------------------Date Column---------------------------------
        date.setCellValueFactory(new PropertyValueFactory<>("name"));

        //---------------------Status Column---------------------------------
        status.setCellValueFactory(order -> new SimpleStringProperty(server.getOrderStatus(order.getValue())));

        //---------------------Distance Column---------------------------------
        distance.setCellValueFactory(order -> Bindings.createStringBinding(() -> server.getOrderDistance(order.getValue()) + " km"));
        //distance.setCellValueFactory(order -> new SimpleFloatProperty(server.getOrderDistance(order.getValue()).floatValue()));

        //---------------------Price Column---------------------------------
        price.setCellValueFactory(order -> Bindings.createStringBinding(() -> server.getOrderCost(order.getValue()) + " £"));
        //price.setCellValueFactory(order -> new SimpleFloatProperty(server.getOrderCost(order.getValue()).floatValue()));

        ObservableList<Order> ordersData = FXCollections.observableList(server.getOrders());
        ordersTable.setItems(ordersData);

        clearOrdersButton.setOnAction(actionEvent -> {

            //isOrderComplete is not implemented correctly, so this would remove all orders, regardless of status
            ordersData.removeIf(order -> server.isOrderComplete(order));
            showToastNotification("Successfully cleared completed orders!");

            /*ordersData.filtered(order -> server.getOrderStatus(order).equals("Complete")).forEach(order -> {
                try {
                    server.removeOrder(order);
                    ordersTable.refresh();
                } catch (ServerInterface.UnableToDeleteException e) {
                    showToastNotification("Order still in progress?!");
                }
            });*/
        });
    }

    @Override
    public void refresh() {
        ordersTable.refresh();
    }
}
