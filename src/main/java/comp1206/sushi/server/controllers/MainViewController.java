package comp1206.sushi.server.controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTabPane;
import comp1206.sushi.server.ServerWindow;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class MainViewController extends ServerWindow implements Refreshable {

    private static JFXSnackbar snackbar;
    @FXML
    private AnchorPane mainWindowView;
    @FXML
    private JFXTabPane leftDrawer;
    @FXML
    private DishesController DishesController;
    @FXML
    private DronesController DronesController;
    @FXML
    private IngredientsController IngredientsController;
    @FXML
    private OrdersController OrdersController;
    @FXML
    private PostcodesController PostcodesController;
    @FXML
    private StaffController StaffController;
    @FXML
    private SuppliersController SuppliersController;
    @FXML
    private UsersController UsersController;

    private MainViewController[] controllers;

    public static void showToastNotification(String toast) {
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(toast)));
    }

    @FXML
    public void initialize() {

        //initialises a global snackbar container
        snackbar = new JFXSnackbar(mainWindowView);
        controllers = new MainViewController[]{DishesController, DronesController,
                IngredientsController, OrdersController, PostcodesController,
                StaffController, SuppliersController, UsersController};

        leftDrawer.getSelectionModel().selectedItemProperty().addListener((val, tab, newtab) -> {
            SuppliersController.clearPostcode();
            IngredientsController.clearSupplier();
        });

        startRefreshService(10);
    }

    //calls refresh on all tabs (implemented in each specific instance)
    private void startRefreshService(double seconds) {
        ScheduledService<Void> svc = new ScheduledService<>() {
            protected Task<Void> createTask() {
                return new Task<>() {
                    protected Void call() {
                        for (MainViewController controller : controllers) {
                            controller.refresh();
                        }
                        return null;
                    }
                };
            }
        };
        svc.setPeriod(Duration.seconds(seconds));
        svc.setDelay(Duration.seconds(10));
        svc.start();
    }

    @Override
    public void refresh() {

    }
}
