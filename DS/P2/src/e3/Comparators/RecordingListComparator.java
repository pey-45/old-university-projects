package e3.Comparators;

import e3.Artist.Artist;

import java.util.Comparator;

public class RecordingListComparator implements Comparator<Artist> {

    @Override
    public int compare(Artist artist1, Artist artist2) {
        //Al añadir el "-", la comparación se hace de forma inversa
        return -Integer.compare(artist1.getRecordingListSize(), artist2.getRecordingListSize());
    }
}