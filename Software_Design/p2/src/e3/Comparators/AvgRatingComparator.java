package e3.Comparators;

import e3.Artist.Artist;

import java.util.Comparator;

public class AvgRatingComparator implements Comparator<Artist> {

    @Override
    public int compare(Artist artist1, Artist artist2) {
        return Double.compare(artist1.getAvg_rating(), artist2.getAvg_rating());
    }
}
