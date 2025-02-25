package e3.Comparators;

import e3.Artist.Artist;

import java.util.Comparator;


public class EclecticismComparator implements Comparator<Artist> {

    @Override
    public int compare(Artist artist1, Artist artist2) {
        return Integer.compare(artist1.getGenreListSize(), artist2.getGenreListSize());
    }
}
