package ihm;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ressources/sample.fxml"));

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        //primaryStage.setFullScreen(true);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<>() {
            public void handle(WindowEvent we) {
                System.exit(0); // force l'arrêt des threads s'il y en a en cours d'execution
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
