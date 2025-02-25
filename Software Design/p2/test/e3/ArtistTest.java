package e3;

import e3.Artist.Artist;
import e3.ArtistAttributes.Recording;
import e3.ArtistAttributes.Release;
import e3.ArtistAttributes.Track;
import e3.ArtistAttributes.Work;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArtistTest {
    static Artist artist1;
    static Artist artist2;
    static Artist auxiliar;

    static Recording recording1;
    static Release release1;
    static Track track1;
    static Work work1;

    static Recording recording2;
    static Release release2;
    static Track track2;
    static Work work2;

    @BeforeAll
    static void setup() {
        //both artists attribute list elements initialization
        recording1 = new Recording("Rec1", "Artist1", "001");
        release1 = new Release("rel1", "artist1", "000");
        track1 = new Track("1", "rec1", "artist1", "track1", 240);
        work1 = new Work("work1", "artists1", "000");

        recording2 = new Recording("rec2", "artist2", "001");
        release2 = new Release("rel2", "artist2", "001");
        track2 = new Track("2", "rec2", "artist2", "track2", 240);
        work2 = new Work("work2", "artists2", "001");

        //artists initialization
        artist1 = new Artist("Pablo", "000");
        artist2 = new Artist("Sara", "001");
        auxiliar = new Artist();

        //artist lists
        artist1.addRecording(recording1);
        artist1.addRelease(release1);
        artist1.addTrack(track1);
        artist1.addWork(work1);
        artist1.addGenre("Rock");
        artist1.addGenre("Metal");
        artist1.addRating(1);

        artist2.addRecording(recording2);
        artist2.addRelease(release2);
        artist2.addTrack(track2);
        artist2.addWork(work2);
        artist2.addGenre("Rap");
        artist2.addRating(4);
        artist2.addRating(3);
    }

    @Test
    void TestSetterGetter() {

        auxiliar.setName("Bob");
        auxiliar.setId("420");

        assertEquals("Bob", auxiliar.getName());
        assertEquals("420", auxiliar.getId());
    }

    @Test
    void TestArtistExceptions() {

        assertThrows(IllegalArgumentException.class, () -> new Artist(null, null));
        assertThrows(IllegalArgumentException.class, () -> auxiliar.addRating(10));
        assertThrows(IllegalArgumentException.class, () -> auxiliar.addGenre(null));
    }

    @Test
    void testRelease() {
        auxiliar.addRelease(release1);
        auxiliar.addRelease(release1);
        auxiliar.addRelease(release2);
        assertEquals(3, auxiliar.getReleaseListSize());
        assertEquals(release2, auxiliar.getReleaseFromIndex(2));
        auxiliar.removeRelease(release2);
        auxiliar.removeRelease(release2);
        assertThrows(IndexOutOfBoundsException.class, () -> auxiliar.getReleaseFromIndex(2));
        auxiliar.emptyReleaseList();
        assertThrows(IndexOutOfBoundsException.class, () -> auxiliar.getReleaseFromIndex(0));
    }

    @Test
    void testRecording() {
        auxiliar.addRecording(recording1);
        auxiliar.addRecording(recording1);
        auxiliar.addRecording(recording2);
        assertEquals(3, auxiliar.getRecordingListSize());
        assertEquals(recording2, auxiliar.getRecordingFromIndex(2));
        auxiliar.removeRecording(recording2);
        auxiliar.removeRecording(recording2);
        assertThrows(IndexOutOfBoundsException.class, () -> auxiliar.getRecordingFromIndex(2));
        auxiliar.emptyRecordingList();
        assertThrows(IndexOutOfBoundsException.class, () -> auxiliar.getRecordingFromIndex(0));
    }

    @Test
    void testWork() {
        auxiliar.addWork(work1);
        auxiliar.addWork(work1);
        auxiliar.addWork(work2);
        assertEquals(3, auxiliar.getWorkListSize());
        assertEquals(work2, auxiliar.getWorkFromIndex(2));
        auxiliar.removeWork(work2);
        auxiliar.removeWork(work2);
        assertThrows(IndexOutOfBoundsException.class, () -> auxiliar.getWorkFromIndex(2));
        auxiliar.emptyWorkList();
        assertThrows(IndexOutOfBoundsException.class, () -> auxiliar.getWorkFromIndex(0));
    }

    @Test
    void testTrack() {
        auxiliar.addTrack(track1);
        auxiliar.addTrack(track1);
        auxiliar.addTrack(track2);
        assertEquals(3, auxiliar.getTrackListSize());
        assertEquals(track2, auxiliar.getTrackFromIndex(2));
        auxiliar.removeTrack(track2);
        auxiliar.removeTrack(track2);
        assertThrows(IndexOutOfBoundsException.class, () -> auxiliar.getTrackFromIndex(2));
        auxiliar.emptyTrackList();
        assertThrows(IndexOutOfBoundsException.class, () -> auxiliar.getTrackFromIndex(0));
    }

    @Test
    void testRating() {
        auxiliar.addRating(0);
        auxiliar.addRating(3);
        auxiliar.addRating(0);
        assertEquals(3, auxiliar.getRatingListSize());
        assertThrows(IllegalArgumentException.class, () -> auxiliar.addRating(-1));
        assertThrows(IllegalArgumentException.class, () -> auxiliar.addRating(6));
        assertEquals(0, auxiliar.getRatingFromIndex(0));
        auxiliar.removeRating(0);
        auxiliar.removeRating(4);
        assertThrows(IndexOutOfBoundsException.class, () -> auxiliar.getRatingFromIndex(2));
        assertEquals(1.5, auxiliar.getAvg_rating());
        auxiliar.emptyRatingList();
        assertThrows(IndexOutOfBoundsException.class, () -> auxiliar.getReleaseFromIndex(0));
        assertEquals(0, auxiliar.getAvg_rating());
    }

    @Test
    void testGenre() {
        auxiliar.addGenre("Rock");
        auxiliar.addGenre("Rap");
        auxiliar.addGenre("Pop");
        auxiliar.addGenre("Rap");
        assertEquals(3, auxiliar.getGenreListSize());
        assertEquals("Pop", auxiliar.getGenreFromIndex(2));
        auxiliar.removeGenre("Pop");
        auxiliar.removeGenre("Genero que no existe");
        assertThrows(IndexOutOfBoundsException.class, () -> auxiliar.getGenreFromIndex(2));
        assertEquals(2, auxiliar.getGenreListSize());
        auxiliar.emptyGenreList();
        assertThrows(IndexOutOfBoundsException.class, () -> auxiliar.getGenreFromIndex(0));
        assertEquals(0, auxiliar.getGenreListSize());
    }
}