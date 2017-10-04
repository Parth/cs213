/**
 * Shantanu Laghate, Parth Mehrotra
 */
package songlib.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import java.io.PrintWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import songlib.app.Song;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.io.FileNotFoundException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.ArrayList;

public class SongLibController {
	
	/*TODO
	 * 1. Sort song list alphabetically every time a new song is added
	 * 2. Check if unique name and artist for add new song
	 * 3. After deletion next song in the list should be displayed (or previous if no next, or blank of empty list)
	 * 4. Add a cancel button, which reverts all fields back to original.
	 * 5. Make add song a separate Button (maybe, this may or may not be necessary)
	 * 
	 */
	
	
	@FXML
	private ListView<Song> listView;

	@FXML
	private Button save;
	@FXML
	private Button delete;
	@FXML
	private TextField name;
	@FXML
	private TextField artist;
	@FXML
	private TextField album;
	@FXML
	private TextField year;

	private ObservableList<Song> obsList;
	private Stage mainStage;

	public void start(Stage mainStage) {
		this.mainStage = mainStage;
		
		// set the change methods for the text fields
		// this listener just enables the save button
		name.textProperty().addListener(editEnableListener);
		artist.textProperty().addListener(editEnableListener);
		album.textProperty().addListener(editEnableListener);
		year.textProperty().addListener(editEnableListener);

		save.setOnAction(saveSong);
		delete.setOnAction(deleteSong);
		
		// create an ObservableList
		// from an ArrayList

		obsList = FXCollections.observableArrayList();

		IOException ioe = null;
		try {
			String content = new String(Files.readAllBytes(Paths.get("library.dat")));
			Gson gson = new Gson(); 
			Type songList = new TypeToken<ArrayList<Song>>(){}.getType();
			ArrayList<Song> tempList = gson.fromJson(content, songList);
			obsList.addAll(tempList);
		} catch (IOException e) {
			System.err.println("No save file");
			ioe = e;
		}

		if (ioe != null || obsList == null) {
			obsList.add(new Song());
		}
		obsList.addListener((ListChangeListener) (c -> {saveList();}));
		listView.setItems(obsList);

		// select the first item
		listView.getSelectionModel().select(0);

		// set listener for the items
		listView.setOnMouseClicked(showSong);

	}

	private void saveList() {
		System.out.println("Saving List");
		Gson gson = new Gson();
		String json = gson.toJson(obsList);
		System.out.println(json);
		
		try {
			PrintWriter out = new PrintWriter("library.dat");
			out.println(json);
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println("Save file not accessible");
		}
	}
	
	// For Text fields
	// Tested, and works
	private ChangeListener<String> editEnableListener = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			System.out.println("Inside edit listener");
			if (!oldValue.equals(newValue)) {
				save.disableProperty().set(false);
			}
		}
	};
	
	// For ListView
	private EventHandler<MouseEvent> showSong = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent e) {
			
			int index = listView.getSelectionModel().getSelectedIndex();
			System.out.println("clicked on " +  index);
			Song newSong = obsList.get(index);
	
			name.textProperty().set(newSong.getName());
			artist.textProperty().set(newSong.getArtist());
			album.textProperty().set(newSong.getAlbum());
			year.textProperty().set(Integer.toString(newSong.getYear()));
			if (index == 0) {
				// you cannot delete the add song field
				delete.setDisable(true);
			}
			else {
				delete.setDisable(false);
			}
		}
	};
	
	//For the Save Button
	// tested and works
	private EventHandler<ActionEvent> saveSong = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			int index = listView.getSelectionModel().getSelectedIndex();
			// If it is the first song, that means a new song is being added
			String newName = name.getText();
			String newArtist = artist.getText();
			String newAlbum = album.getText();
			String newYear = year.getText();
			if (newName.equals("") || newArtist.equals("") || newAlbum.equals("") || newYear.equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(mainStage);
				alert.setTitle("Field Error!");
				alert.setHeaderText("Please Fill out all fields!");
				alert.showAndWait();
			}
			
			
			
			if (index != 0) {
				
				Song s = new Song(newName, newArtist, newAlbum, Integer.parseInt(newYear));
				obsList.set(listView.getSelectionModel().getSelectedIndex(), s);
			}
			// Otherwise, an existing song is being edited
			else {
				obsList.add(new Song(newName, newArtist, newAlbum, Integer.parseInt(newYear)));
			}
			resetToZero();
		}
	};

	// For delete button
	private EventHandler<ActionEvent> deleteSong = new EventHandler<ActionEvent>() {
		public void handle (ActionEvent e) {
			if (obsList.size() == 1) {
				// Add some error texts
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(mainStage);
				alert.setTitle("Deletion Error!");
				alert.setHeaderText("Cannot Delete an Empty Song");
				alert.showAndWait();
			} else {
				int index = listView.getSelectionModel().getSelectedIndex();
	
				obsList.remove(index);
			}
			
			resetToZero();
			
		}

	};
	
	// This function should always be called when returning to the
	// add song menu
	private void resetToZero() {
		listView.getSelectionModel().select(0);
		Song newSong = obsList.get(0);
		name.setPromptText(newSong.getName());
		artist.setPromptText(newSong.getArtist());
		album.setPromptText(newSong.getAlbum());
		year.setPromptText(Integer.toString(newSong.getYear()));
		delete.setDisable(true);
	}
}
