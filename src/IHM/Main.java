package IHM;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	protected MainScene mainScene;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Workflow Engine");
		this.mainScene = new MainScene();
		stage.setScene(this.mainScene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
