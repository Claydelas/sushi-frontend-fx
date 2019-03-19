package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTabPane;
import comp1206.sushi.server.ServerWindow;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MainViewController extends ServerWindow {

    @FXML private JFXTabPane leftDrawer;
    @FXML private AnchorPane mainWindowView;
    private static JFXSnackbar snackbar;

    @FXML
    public void initialize() {
        //initialises a global snackbar container
        snackbar = new JFXSnackbar(mainWindowView);

    }

    public static void showToastNotification(String toast) {
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(toast)));
    }
}
