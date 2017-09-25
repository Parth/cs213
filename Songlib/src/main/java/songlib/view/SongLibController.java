package songlib.view;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import songlib.app.Song;

public class SongLibController {
    
	   @FXML         
	   ListView<Song> listView; 
	   
	   @FXML Button save;
	   @FXML Button delete;
	   @FXML TextField name;
	   @FXML TextField artist;
	   @FXML TextField album;
	   @FXML TextField year;
	   

	   private ObservableList<Song> obsList;              
	  
	   public void start(Stage mainStage) {                
	      // create an ObservableList 
	      // from an ArrayList              
	      obsList = FXCollections.observableArrayList();               
	      listView.setItems(obsList);  
	      
	      // select the first item
	      listView.getSelectionModel().select(0);

	      // set listener for the items
	      listView
	        .getSelectionModel()
	        .selectedIndexProperty()
	        .addListener(
	           (obs, oldVal, newVal) -> 
	               showItemInfo(mainStage));

	   }
	   
	   public void saveSong(ActionEvent e) {
		   
	   }
	   
	   public void deleteSong(ActionEvent e) {
		   if (obsList.size() == 1) {
			   //Add some error texts
		   }
		   else {
			   int index = listView.getSelectionModel().getSelectedIndex();	
			   obsList.remove(index);
		   }
		   listView.getSelectionModel().select(0);
		   
	   }
	   
	   private void showItemInfo(Stage mainStage) {                
		   Song item = listView.getSelectionModel().getSelectedItem();
		   int index = listView.getSelectionModel().getSelectedIndex();
		   
		   
		   
		   
		   /*
		   TextInputDialog dialog = new TextInputDialog(item);
		   dialog.initOwner(mainStage); dialog.setTitle("List Item");
		   dialog.setHeaderText("Selected Item (Index: " + index + ")");
		   dialog.setContentText("Enter name: ");
*/
		   //Optional<String> result = dialog.showAndWait();
		   //if (result.isPresent()) { obsList.set(index, result.get()); }
	   }


	}

