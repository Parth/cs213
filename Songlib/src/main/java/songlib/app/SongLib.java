package songlib.app;
 
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import songlib.view.SongLibController;

import java.nio.file.Paths;


public class SongLib extends Application {
	
	@Override
	public void start(Stage primaryStage) 
	throws Exception {
		FXMLLoader loader = new FXMLLoader();

		System.out.println("test");
		System.out.println(getClass().getResource("/SongLib.fxml"));
		loader.setLocation(getClass().getResource("/SongLib.fxml"));

	      Parent root = loader.load();

	      SongLibController listController = 
	         loader.getController();
	      listController.start(primaryStage);

	      Scene scene = new Scene(root, 200, 300);
	      primaryStage.setScene(scene);
	      primaryStage.show(); 

	}
	
	public static void main(String[] args) {
		launch(args);
	}
	

}
