/**
 * Shantanu Laghate, Parth Mehrotra
 */
package songlib.view;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import songlib.app.Song;

public class SongLibController {
    
	   @FXML private ListView<Song> listView; 
	   
	   @FXML private Button save;
	   @FXML private Button delete;
	   @FXML private TextField name;
	   @FXML private TextField artist;
	   @FXML private TextField album;
	   @FXML private TextField year;
	   

	   private ObservableList<Song> obsList;              
	  private Stage mainStage;
	  
	  
	   public void start(Stage mainStage) {  
		   this.mainStage = mainStage;
		  //set the change methods for the text fields
		  //this listener just enables the save button
		  name.textProperty().addListener(editEnableListener);
		  artist.textProperty().addListener(editEnableListener);
		  album.textProperty().addListener(editEnableListener);
		  year.textProperty().addListener(editEnableListener);
		 		  
		   
	      // create an ObservableList 
	      // from an ArrayList              
	      obsList = FXCollections.observableArrayList();  
	      obsList.add(new Song());
	      listView.setItems(obsList);  
	      
	      // select the first item
	      listView.getSelectionModel().select(0);

	      // set listener for the items
	      listView
	        .getSelectionModel()
	        .selectedIndexProperty()
	        .addListener(
	           (obs, oldVal, newVal) -> 
	               showSong(mainStage));

	   }
	   
	   public void update(ActionEvent e) {
		   /*
		   try {
			   Button b = (Button) e.getSource();
			   if (b == save) {
				   saveSong();
			   }
			   else if (b == delete) {
				   
			   }
		   }
		   catch (ClassCastException ce) {
			   TextField tf = (TextField) e.getSource();
			   
		   }*/
		   
	   }
	   
	   private ChangeListener<String> editEnableListener = new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {

		        if (!oldValue.equals(newValue)) {
		        	save.disableProperty().set(false);
		        }
		    }
		};
	   
	   private void showSong(Stage mainStage) {
		   int index = listView.getSelectionModel().getSelectedIndex();
		   Song newSong = obsList.get(index);
		   
		   name.textProperty().set(newSong.getName());
		   artist.textProperty().set(newSong.getArtist());
		   album.textProperty().set(newSong.getAlbum());
		   year.textProperty().set(Integer.toString(newSong.getYear()));
		   if (index == 1) {
			   //you cannot delete the add song field
			   delete.disableProperty().set(true);
		   }
	   }
	   
	   public void saveSong() {
		   int index = listView.getSelectionModel().getSelectedIndex();
		   // If it is the first song, that means a new song is being added
		   if (index != 0) {
			   Song s = new Song(name.getText(), 
					   artist.getText(), 
					   album.getText(), 
					   Integer.parseInt(year.getText()));
			   obsList.set(listView.getSelectionModel().getSelectedIndex(), s);
		   }
		   //Otherwise, an existing song is being edited
		   else {
			   obsList.add(new Song(name.getText(), 
					   artist.getText(), 
					   album.getText(), 
					   Integer.parseInt(year.getText())));
		   }
	   }
	   
	   public void deleteSong(ActionEvent e) {
		   if (obsList.size() == 1) {
			   //Add some error texts
			   Alert alert = new Alert(AlertType.ERROR);
			   alert.initOwner(mainStage);
			   alert.setTitle("Deletion Error!");
			   alert.setHeaderText("Cannot Delete an Empty Song");
			   alert.showAndWait();
		   }
		   else {
			   int index = listView.getSelectionModel().getSelectedIndex();
			   
			   obsList.remove(index);
		   }
		   listView.getSelectionModel().select(0);
		   
	   }
	}

