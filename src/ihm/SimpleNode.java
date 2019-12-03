package ihm;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SimpleNode extends AnchorPane {

    public SimpleNode() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ressources/simple_node.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException exc) {
            // handle exception
            System.out.println("Element not created !");
            exc.printStackTrace();

        }
    }

}
