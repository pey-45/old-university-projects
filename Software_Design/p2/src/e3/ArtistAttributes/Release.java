package e3.ArtistAttributes;

public record Release(String title, String artist, String releaseID) {

    @Override
    public String toString() {
        return "Título: " + title + "\t\tArtista: " + artist + "\t\tID: " + releaseID;
    }
}