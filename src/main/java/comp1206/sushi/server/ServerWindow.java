package comp1206.sushi.server;

import com.jfoenix.controls.JFXDecorator;
import comp1206.sushi.mock.MockServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Provides the Sushi Server user interface
 */
public class ServerWindow extends Application {

    //dont know what this does but im keeping it
    private static final long serialVersionUID = -4661566573959270000L;
    protected static ServerInterface server;

    @Override
    public void start(Stage primaryStage) throws Exception {

        //starts up an instance of a server
        server = new MockServer();

        //loads main fxml view
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));

        //applies decor styling
        JFXDecorator styleDecor = new JFXDecorator(primaryStage, root, false, true, true);
        styleDecor.getStylesheets().add("/css/guiDecor.css");

        //new scene with the custom decor 860, 700
        Scene scene = new Scene(styleDecor, 1270, 800);

        //adjusts default styling
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle(server.getRestaurantName() + " Server");
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1270);

        //starts up GUI
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
