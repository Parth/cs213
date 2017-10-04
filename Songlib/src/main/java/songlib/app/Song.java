/**
 * Shantanu Laghate, Parth Mehrotra
 */
package songlib.app;

public class Song {
	private String name;
	private String artist;
	private String album;
	private int year;

	public Song () {
		name = "Add new song";
		artist = "";
		album = "";
		year = 0;
	}
	
	public Song(String name, String artist, 
			String album, int year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Song song = (Song) o;

		if (getYear() != song.getYear()) return false;
		if (getName() != null ? !getName().equals(song.getName()) : song.getName() != null) return false;
		if (getArtist() != null ? !getArtist().equals(song.getArtist()) : song.getArtist() != null) return false;
		return getAlbum() != null ? getAlbum().equals(song.getAlbum()) : song.getAlbum() == null;
	}

	@Override
	public int hashCode() {
		int result = getName() != null ? getName().hashCode() : 0;
		result = 31 * result + (getArtist() != null ? getArtist().hashCode() : 0);
		result = 31 * result + (getAlbum() != null ? getAlbum().hashCode() : 0);
		result = 31 * result + getYear();
		return result;
	}

	@Override
	public String toString() {
		return name + "\n" + artist;
	}
	
}
