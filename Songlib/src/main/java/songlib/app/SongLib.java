package songlib.app;
 
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

 
public class SongLib extends Application {
	
	@Override
	public void start(Stage primaryStage) 
	throws Exception {
		FXMLLoader loader = new FXMLLoader();   
	      loader.setLocation(
	         getClass().getResource("/view/List.fxml"));
	      AnchorPane root = (AnchorPane)loader.load();

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
