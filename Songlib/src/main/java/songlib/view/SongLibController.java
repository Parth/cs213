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
import java.util.Collections;
import java.util.Comparator;

public class SongLibController {

	/*
	 * TODO Sort song list alphabetically every time a new song is added 
	 * 
	 */

	@FXML
	private ListView<Song> listView;

	@FXML
	private Button cancel;
	@FXML
	private Button addNew;
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

	private boolean addingNewSong = false;

	public void start(Stage mainStage) {
		this.mainStage = mainStage;

		// set the change methods for the text fields
		name.textProperty().addListener(editEnableListener);
		artist.textProperty().addListener(editEnableListener);
		album.textProperty().addListener(editEnableListener);
		year.textProperty().addListener(editEnableListener);

		save.setOnAction(saveSong);
		delete.setOnAction(deleteSong);
		addNew.setOnAction(addNewSong);
		cancel.setOnAction(cancelButton);

		obsList = FXCollections.observableArrayList();

		IOException ioe = null;
		try {
			String content = new String(Files.readAllBytes(Paths.get("library.dat")));
			Gson gson = new Gson();
			Type songList = new TypeToken<ArrayList<Song>>() {
			}.getType();
			ArrayList<Song> tempList = gson.fromJson(content, songList);
			obsList.addAll(tempList);
		} catch (IOException e) {
			System.err.println("No save file");
			ioe = e;
		}

		obsList.addListener((ListChangeListener) (c -> {
			saveList();
		}));
		listView.setItems(obsList);

		// select the first item
		listView.getSelectionModel().clearSelection();
		makeBlank();

		// set listener for the items
		listView.setOnMouseClicked(showSong);

	}

	private void sortCollection() {
		FXCollections.sort(obsList, new Comparator<Song>() {
			@Override
			public int compare(Song s1, Song s2) {
				
				if (s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase()) == 0)
					return s1.getArtist().toLowerCase().compareTo(s2.getArtist().toLowerCase());
				else
					return s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase());
			}
		});

	}

	// save library to disk for persistence
	private void saveList() {
		Gson gson = new Gson();
		String json = gson.toJson(obsList);

		try {
			PrintWriter out = new PrintWriter("library.dat");
			out.println(json);
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println("Save file not accessible");
		}
	}

	// What to do when add new song button is clicked
	private EventHandler<ActionEvent> addNewSong = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			listView.getSelectionModel().clearSelection();
			addingNewSong = true;
			name.disableProperty().set(false);
			artist.disableProperty().set(false);
			album.disableProperty().set(false);
			year.disableProperty().set(false);
			
			name.textProperty().set("");
			name.setPromptText("New Song Name");
			artist.textProperty().set("");
			artist.setPromptText("New Song Artist");
			album.textProperty().set("");
			album.setPromptText("New Song Album");
			year.textProperty().set("");
			year.setPromptText("New Song Year");

			// you cannot delete the add song field
			delete.setDisable(true);
			cancel.disableProperty().set(false);

		}
	};

	

	// For Text fields, after any change in made enable saving
	private ChangeListener<String> editEnableListener = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			if (!oldValue.equals(newValue)) {
				save.disableProperty().set(false);
			}
		}
	};

	// After a song is clicked on, show its details
	private EventHandler<MouseEvent> showSong = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent e) {
			int index = listView.getSelectionModel().getSelectedIndex();
			showSongAtIndex(index);
		}

	};

	// this method is a general form to show a song at a certain index
	private void showSongAtIndex(int index) {
		if (obsList.size() > 0 && index >= 0 && index < obsList.size()) {
			name.disableProperty().set(false);
			artist.disableProperty().set(false);
			album.disableProperty().set(false);
			year.disableProperty().set(false);
			
			listView.getSelectionModel().select(index);

			Song newSong = obsList.get(index);

			name.textProperty().set(newSong.getName());
			name.setPromptText("");
			artist.textProperty().set(newSong.getArtist());
			artist.setPromptText("");
			album.textProperty().set(newSong.getAlbum());
			album.setPromptText("");
			year.textProperty().set(newSong.getYear() == -1 ? "" : Integer.toString(newSong.getYear()));
			year.setPromptText("");

			delete.setDisable(false);
			cancel.disableProperty().set(false);
		}

	}

	// Saves songs after save button is clicked
	private EventHandler<ActionEvent> saveSong = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			String newName = name.getText();
			String newArtist = artist.getText();
			String newAlbum = album.getText();
			// -1 if year is empty to avoid Number parsing errors
			int newYear;
			try {
				newYear = Integer.parseInt(year.getText().equals("") ? "-1" : year.getText());
			} catch (Exception exc) {
				exc.printStackTrace();
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(mainStage);
				alert.setTitle("Year Error!");
				alert.setHeaderText("Your year is not valid!");
				alert.showAndWait();
				return;
			}

			if (newName.equals("") || newArtist.equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(mainStage);
				alert.setTitle("Field Error!");
				alert.setHeaderText("Please fill out at least name and artist!");
				alert.showAndWait();
				return;
			}

			Song newSong = new Song(newName, newArtist, newAlbum, newYear);
			// adding a new song
			if (addingNewSong) {
				if (checkUnique(newSong, -1)) {
					obsList.add(newSong);
					sortCollection();
					showSongAtIndex((getIndex(newSong)));
					addingNewSong = false;
					return;
				} else
					showUniquenessError();
				return;
			}

			// editing a song
			int index = listView.getSelectionModel().getSelectedIndex();

			if (checkUnique(newSong, index)) {
				obsList.set(listView.getSelectionModel().getSelectedIndex(), newSong);
				sortCollection();
				showSongAtIndex((getIndex(newSong)));
				return;
			} else {
				showUniquenessError();
				return;
			}

		}
	};
	
	private void showUniquenessError() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(mainStage);
		alert.setTitle("Uniqueness Error!");
		alert.setHeaderText("The song that you filled out already exists!!");
		alert.showAndWait();
		return;
	}

	// For delete button
	private EventHandler<ActionEvent> deleteSong = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			int index = listView.getSelectionModel().getSelectedIndex();
			obsList.remove(index);

			if (index >= obsList.size()) {
				if (index - 1 >= 0)
					showSongAtIndex(index - 1);
				else
					makeBlank();
			} else {
				showSongAtIndex(index);
			}
		}

	};
	
	private EventHandler<ActionEvent> cancelButton = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			makeBlank();
		}
		
	};

	// getting the index of a song in the library
	private int getIndex(Song s) {
		for (int i = 0; i < obsList.size(); i++) {
			if (obsList.get(i).equals(s))
				return i;
		}
		return -1;
	}

	// check if a song is already in the list or not, 
	// index param is the index to ignore, necessary to edit a song 
	private boolean checkUnique(Song s, int index) {
		for (int i = 0; i < obsList.size(); i++) {
			if (i == index)
				continue;
			Song song = obsList.get(i);
			if (song.getName().toLowerCase().equals(s.getName().toLowerCase()) 
					&& song.getArtist().toLowerCase().equals(s.getArtist().toLowerCase())) {
				return false;
			}
		}
		return true;
	}
	
	// Make all fields blank and reset view
	private void makeBlank() {
		listView.getSelectionModel().clearSelection();
		name.textProperty().set("");
		name.disableProperty().set(true);
		artist.textProperty().set("");
		artist.disableProperty().set(true);
		album.textProperty().set("");
		album.disableProperty().set(true);
		year.textProperty().set("");
		year.disableProperty().set(true);
		
		save.disableProperty().set(true);
		delete.disableProperty().set(true);
		cancel.disableProperty().set(true);
		
	}
}
