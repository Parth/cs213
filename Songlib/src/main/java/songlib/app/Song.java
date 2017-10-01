/**
 * Shantanu Laghate, Parth Mehrotra
 */
package songlib.app;

public class Song {
	public String name, artist, album;
	public int year;
	
	public Song () {
		name = "Song name";
		artist = "Song artist";
		album = "Song album";
		year = 1998;
	}
	
	public Song(String name, String artist, 
			String album, int year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public boolean equals (Object o) {
		if (o == null || !(o instanceof Song)) return false;
		Song s = (Song) o;
		return (s.name.equals(name) && s.artist.equals(artist) 
				&& s.album.equals(album) && s.year == year);
	}
}
