package e3.ArtistAttributes;

public record Work(String title, String artists, String workID) {

    @Override
    public String toString() {
        return "Título: " + title + "\t\tArtistas: " + artists + "\t\tID: " + workID;
    }
}