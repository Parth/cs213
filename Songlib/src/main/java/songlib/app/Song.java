package songlib.app;

public class Song {
	public String name, artist, album;
	public int year;
	
	public Song () {
		
	}
	
	public Song(String name, String artist, 
			String album, int year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
}
