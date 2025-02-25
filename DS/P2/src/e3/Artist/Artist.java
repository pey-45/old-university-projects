package e3.Artist;

import e3.ArtistAttributes.Recording;
import e3.ArtistAttributes.Release;
import e3.ArtistAttributes.Track;
import e3.ArtistAttributes.Work;

import java.util.ArrayList;

public class Artist implements Comparable<Artist> {

    private final ArrayList<Release> release_list = new ArrayList<>();
    private final ArrayList<Recording> recording_list = new ArrayList<>();
    private final ArrayList<Work> work_list = new ArrayList<>();
    private final ArrayList<Track> track_list = new ArrayList<>();
    private final ArrayList<Integer> rating_list = new ArrayList<>();
    private final ArrayList<String> genre_list = new ArrayList<>();
    private String name;
    private String id;
    private double avg_rating = 0;

    public Artist(String name, String id) {

        if (name != null && id != null) {
            this.name = name;
            this.id = id;
        } else throw new IllegalArgumentException("Valor de nombre o ID nulos");
    }

    public Artist() {}


    //Getters  Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    //Release functions
    public void addRelease(Release release) {
        this.release_list.add(release);
    }
    public void removeRelease(Release release) {
        this.release_list.remove(release);
    }
    public Release getReleaseFromIndex(int i) {
        return release_list.get(i);
    }
    public int getReleaseListSize() {
        return this.release_list.size();
    }
    public void emptyReleaseList() {
        while (!this.release_list.isEmpty())
            this.release_list.remove(0);
    }

    //Recording functions
    public void addRecording(Recording recording) {
        this.recording_list.add(recording);
    }
    public void removeRecording(Recording recording) {
        this.recording_list.remove(recording);
    }
    public Recording getRecordingFromIndex(int i) {
        return recording_list.get(i);
    }
    public int getRecordingListSize() {
        return this.recording_list.size();
    }
    public void emptyRecordingList() {
        while (!this.recording_list.isEmpty())
            this.recording_list.remove(0);
    }

    //Work functions
    public void addWork(Work work) {
        this.work_list.add(work);
    }
    public void removeWork(Work work) {
        this.work_list.remove(work);
    }
    public Work getWorkFromIndex(int i) {
        return work_list.get(i);
    }
    public int getWorkListSize() {
        return this.work_list.size();
    }
    public void emptyWorkList() {
        while (!this.work_list.isEmpty())
            this.work_list.remove(0);
    }

    //Track functions
    public void addTrack(Track track) {
        this.track_list.add(track);
    }
    public void removeTrack(Track track) {
        this.track_list.remove(track);
    }
    public Track getTrackFromIndex(int i) {
        return track_list.get(i);
    }
    public int getTrackListSize() {
        return this.track_list.size();
    }
    public void emptyTrackList() {
        while (!this.track_list.isEmpty())
            this.track_list.remove(0);
    }

    //Rating functions
    public void addRating(int rating) {
        if (rating < 0 || rating > 5) throw new IllegalArgumentException("El rating debe estar entre 0 y 5");
        this.rating_list.add(rating);
    }
    public void removeRating(int rating) {
        this.rating_list.remove((Integer) rating);
    }
    public int getRatingFromIndex(int i) {
        return rating_list.get(i);
    }
    public int getRatingListSize() {
        return this.rating_list.size();
    }
    public void emptyRatingList() {
        while (!this.rating_list.isEmpty())
            this.rating_list.remove(0);
    }
    public double getAvg_rating() {

        int i;

        if (this.rating_list.isEmpty()) return 0;

        for (i = 0; i < this.rating_list.size(); i++)
            avg_rating += this.rating_list.get(i);
        return avg_rating /= this.rating_list.size();
    }

    //Genre functions
    public void addGenre(String genre) {

        if (genre == null) throw new IllegalArgumentException("Valor nulo no permitido");

        else if (this.genre_list.contains(genre)) return;
        this.genre_list.add(genre);
    }
    public void removeGenre(String genre) {
        this.genre_list.remove(genre);
    }
    public String getGenreFromIndex(int i) {
        return genre_list.get(i);
    }
    public int getGenreListSize() {
        return this.genre_list.size();
    }
    public void emptyGenreList() {
        while (!this.genre_list.isEmpty())
            this.genre_list.remove(0);
    }


    //Natural compareTo
    @Override
    public int compareTo(Artist artist) {
        return this.id.compareTo(artist.id);
    }

    //Auxiliar toString
    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append("\nNombre: ").append(name).append("\t\tID: ").append(id).append("\n");

        builder.append("Release List:\n");
        for (Release release : release_list) builder.append("\t\t").append(release.toString()).append("\n");

        builder.append("Recording List:\n");
        for (Recording recording : recording_list) builder.append("\t\t").append(recording.toString()).append("\n");

        builder.append("Work List:\n");
        for (Work work : work_list) builder.append("\t\t").append(work.toString()).append("\n");

        builder.append("Track List:\n");
        for (Track track : track_list) builder.append("\t\t").append(track.toString()).append("\n");

        builder.append("Genre List:\n");
        for (String genre : genre_list) builder.append("\t\t").append(genre).append("\n");

        builder.append("Rating List:\n");
        for (Integer rating : rating_list) builder.append("\t\t").append(rating.toString());


        return builder.toString();
    }
}
