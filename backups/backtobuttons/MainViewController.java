package comp1206.sushi.server.controllers;

import com.jfoenix.controls.*;
import comp1206.sushi.common.Dish;
import comp1206.sushi.server.ServerWindow;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController extends ServerWindow implements Initializable {

    @FXML
    private Tab Orders;
    @FXML
    private Tab Dishes;
    @FXML
    private AnchorPane OrdersView;
    @FXML
    private AnchorPane DishesView;
    @FXML
    private AnchorPane root;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXTabPane drawerContent;
    @FXML // fx:id="ordersTable"
    private TableView<Dish> ordersTable; // Value injected by FXMLLoader
    @FXML // fx:id="firstColumn"
    private TableColumn<Dish, String> firstColumn; // Value injected by FXMLLoader
    @FXML // fx:id="secondColumn"
    private TableColumn<Dish, String> secondColumn; // Value injected by FXMLLoader
    private JFXSnackbar snackbar;
    private boolean isAnimating = false;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        snackbar = new JFXSnackbar(root);

        firstColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        ObservableList<Dish> dishData = FXCollections.observableList(server.getDishes());
        ordersTable.setItems(dishData);

        /*drawer.setSidePane(drawerContent);
        hamburger.setOnMousePressed(e -> {
            drawer.toggle();
        });*/

        
        showToastNotification("hi");
        showToastNotification("free!");

        hamburger.setOnMousePressed(e->animate());
    }

    public void showToastNotification(String toast) {
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(toast)));
    }

    public void animate() {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), drawerContent);

        if (isAnimating)
            return;

        isAnimating = true;

        tt.setFromX(drawerContent.getTranslateX());

        if (drawerContent.getTranslateX() < 0) {
            tt.setToX(drawerContent.getTranslateX() + 256);
        } else {
            tt.setToX(drawerContent.getTranslateX() - 256);
        }
        tt.setOnFinished(e -> isAnimating = false);

        tt.setInterpolator(new Interpolator() {
            @Override
            protected double curve(double t) {
                //return (t == 0.0) ? 0.0 : Math.pow(2.0, 10 * (t - 1));
                return (t == 1.0) ? 1.0 : 1 - Math.pow(2.0, -10 * t);
            }
        });
        tt.play();
    }
}
