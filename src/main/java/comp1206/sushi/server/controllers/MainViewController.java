package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTabPane;
import comp1206.sushi.server.ServerWindow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController extends ServerWindow implements Initializable {

    @FXML private JFXTabPane leftDrawer;
    @FXML private AnchorPane mainWindowView;
    private JFXSnackbar snackbar;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        snackbar = new JFXSnackbar(mainWindowView);

    }

    public void showToastNotification(String toast) {
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(toast)));
    }
}
