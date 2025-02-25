package e3.ArtistAttributes;

public record Track(String position, String recording, String artist, String title, int duration) {

    @Override
    public String toString() {
        return "Posición: " + position + "\t\tGrabación: " + recording + "\t\tArtista: " + artist + "\t\tTítulo: " + title + "\t\tDuración: " + duration;
    }
}