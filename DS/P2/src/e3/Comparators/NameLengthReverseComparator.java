package e3.Comparators;

import e3.Artist.Artist;

import java.util.Comparator;

public class NameLengthReverseComparator implements Comparator<Artist> {

    @Override
    public int compare(Artist artist1, Artist artist2) {
        //Al añadir el "-", la comparación se hace de forma inversa
        return -Integer.compare(artist1.getName().length(), artist2.getName().length());
    }
}