package e3.ArtistAttributes;

public record Recording(String title, String artist, String recordingID) {

    @Override
    public String toString() {
        return "Título: " + title + "\t\tArtista: " + artist + "\t\tID: " + recordingID;
    }
}