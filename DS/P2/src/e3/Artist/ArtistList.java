package e3.Artist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArtistList {

    //variable declaration
    private final ArrayList<Artist> artist_list = new ArrayList<>();
    Comparator<Artist> comparator = null;

    //list functions
    public void addArtist(Artist artist) {
        this.artist_list.add(artist);
    }
    public void removeArtist(Artist artist) {
        this.artist_list.remove(artist);
    }


    public void setComparator(Comparator<Artist> comparator) {
        this.comparator = comparator;
    }

    public void sort() {
        if (this.comparator == null) Collections.sort(artist_list);
        else
            Collections.sort(artist_list, comparator);
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        for (Artist artist : artist_list) {
            builder.append("\n").append(artist.toString()).append("\n");
        }

        return builder.toString();
    }
}
