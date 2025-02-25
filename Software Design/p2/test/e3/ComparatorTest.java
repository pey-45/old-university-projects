package e3;

import e3.Artist.Artist;
import e3.Artist.ArtistList;
import e3.ArtistAttributes.Recording;
import e3.ArtistAttributes.Release;
import e3.ArtistAttributes.Track;
import e3.ArtistAttributes.Work;
import e3.Comparators.AvgRatingComparator;
import e3.Comparators.EclecticismComparator;
import e3.Comparators.NameLengthReverseComparator;
import e3.Comparators.RecordingListComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComparatorTest {

    Artist artist1, artist2, artist3;
    Release release1, release2, release3;
    Recording recording1, recording2, recording3;
    Work work1, work2, work3;
    Track track1, track2, track3;

    ArtistList artistList = new ArtistList();

    @BeforeEach
    void setup(){
        artist1 = new Artist("Eminem", "001");
        artist2 = new Artist("Freddie Mercury", "002");
        artist3 = new Artist("Michael Jackson","003");

        release1 = new Release("Rel1", "Artist1", "300");
        release2 = new Release("Rel2", "Artist1", "200");
        release3 = new Release("Rel3", "Artist1", "100");

        recording1 = new Recording("Rec1", "Artist1", "001");
        recording2 = new Recording("Rec2", "Artist2", "002");
        recording3 = new Recording("Rec3", "Artist3", "003");

        work1 = new Work("Work1", "Artists1", "010");
        work2 = new Work("Work2", "Artists2", "020");
        work3 = new Work("Work3", "Artists3", "030");

        track1 = new Track("1", "Rec1", "Artist1", "Track1", 360);
        track2 = new Track("2", "Rec2", "Artist2", "Track2", 720);
        track3 = new Track("3", "Rec3", "Artist3", "Track3", 1080);


        artist1.addRelease(release1);
        artist1.addRecording(recording1);
        artist1.addWork(work1);
        artist1.addTrack(track1);
        artist1.addRating(4);
        artist1.addGenre("Rap");

        artist2.addRelease(release1);       artist2.addRelease(release2);
        artist2.addRecording(recording1);   artist2.addRecording(recording2);
        artist2.addWork(work1);             artist2.addWork(work2);
        artist2.addTrack(track1);           artist2.addTrack(track2);
        artist2.addRating(4);               artist2.addRating(5);
        artist2.addGenre("Rock");           artist2.addGenre("New Age");

        artist3.addRelease(release1);       artist3.addRelease(release2);       artist3.addRelease(release3);
        artist3.addRecording(recording1);   artist3.addRecording(recording2);   artist3.addRecording(recording3);
        artist3.addWork(work1);             artist3.addWork(work2);             artist3.addWork(work3);
        artist3.addTrack(track1);           artist3.addTrack(track2);           artist3.addTrack(track3);
        artist3.addRating(4);               artist3.addRating(5);               artist3.addRating(3);
        artist3.addGenre("Pop");            artist3.addGenre("Dance");          artist3.addGenre("Hip Hop");

        artistList.addArtist(artist2);
        artistList.addArtist(artist3);
        artistList.addArtist(artist1);

        artistList.addArtist(artist1);
        artistList.removeArtist(artist1);
    }

    //Debido al tamaño de cada print de la lista, se hace un test distinto por cada Comparator para mayor visibilidad
    @Test
    void TestBaseList(){

        //Lista base -> Artista2, Artista3, Artista1
        assertEquals("""
                                
                                                                                             
                Nombre: Freddie Mercury		ID: 002
                Release List:
                		Título: Rel1		Artista: Artist1		ID: 300
                		Título: Rel2		Artista: Artist1		ID: 200
                Recording List:
                		Título: Rec1		Artista: Artist1		ID: 001
                		Título: Rec2		Artista: Artist2		ID: 002
                Work List:
                		Título: Work1		Artistas: Artists1		ID: 010
                		Título: Work2		Artistas: Artists2		ID: 020
                Track List:
                		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
                		Posición: 2		Grabación: Rec2		Artista: Artist2		Título: Track2		Duración: 720
                Genre List:
                		Rock
                		New Age
                Rating List:
                		4		5
                                                                                             
                                                                                             
                Nombre: Michael Jackson		ID: 003
                Release List:
                		Título: Rel1		Artista: Artist1		ID: 300
                		Título: Rel2		Artista: Artist1		ID: 200
                		Título: Rel3		Artista: Artist1		ID: 100
                Recording List:
                		Título: Rec1		Artista: Artist1		ID: 001
                		Título: Rec2		Artista: Artist2		ID: 002
                		Título: Rec3		Artista: Artist3		ID: 003
                Work List:
                		Título: Work1		Artistas: Artists1		ID: 010
                		Título: Work2		Artistas: Artists2		ID: 020
                		Título: Work3		Artistas: Artists3		ID: 030
                Track List:
                		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
                		Posición: 2		Grabación: Rec2		Artista: Artist2		Título: Track2		Duración: 720
                		Posición: 3		Grabación: Rec3		Artista: Artist3		Título: Track3		Duración: 1080
                Genre List:
                		Pop
                		Dance
                		Hip Hop
                Rating List:
                		4		5		3
                                                                                             
                                                                                             
                Nombre: Eminem		ID: 001
                Release List:
                		Título: Rel1		Artista: Artist1		ID: 300
                Recording List:
                		Título: Rec1		Artista: Artist1		ID: 001
                Work List:
                		Título: Work1		Artistas: Artists1		ID: 010
                Track List:
                		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
                Genre List:
                		Rap
                Rating List:
                		4
                """, artistList.toString());
    }

    @Test
    void TestNaturalSort(){

        //Natural sort -> Artista1, Artista2, Artista3
        artistList.sort();
        assertEquals("""


            Nombre: Eminem		ID: 001
            Release List:
            		Título: Rel1		Artista: Artist1		ID: 300
            Recording List:
            		Título: Rec1		Artista: Artist1		ID: 001
            Work List:
            		Título: Work1		Artistas: Artists1		ID: 010
            Track List:
            		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
            Genre List:
            		Rap
            Rating List:
            		4
            
            
            Nombre: Freddie Mercury		ID: 002
            Release List:
            		Título: Rel1		Artista: Artist1		ID: 300
            		Título: Rel2		Artista: Artist1		ID: 200
            Recording List:
            		Título: Rec1		Artista: Artist1		ID: 001
            		Título: Rec2		Artista: Artist2		ID: 002
            Work List:
            		Título: Work1		Artistas: Artists1		ID: 010
            		Título: Work2		Artistas: Artists2		ID: 020
            Track List:
            		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
            		Posición: 2		Grabación: Rec2		Artista: Artist2		Título: Track2		Duración: 720
            Genre List:
            		Rock
            		New Age
            Rating List:
            		4		5
            
            
            Nombre: Michael Jackson		ID: 003
            Release List:
            		Título: Rel1		Artista: Artist1		ID: 300
            		Título: Rel2		Artista: Artist1		ID: 200
            		Título: Rel3		Artista: Artist1		ID: 100
            Recording List:
            		Título: Rec1		Artista: Artist1		ID: 001
            		Título: Rec2		Artista: Artist2		ID: 002
            		Título: Rec3		Artista: Artist3		ID: 003
            Work List:
            		Título: Work1		Artistas: Artists1		ID: 010
            		Título: Work2		Artistas: Artists2		ID: 020
            		Título: Work3		Artistas: Artists3		ID: 030
            Track List:
            		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
            		Posición: 2		Grabación: Rec2		Artista: Artist2		Título: Track2		Duración: 720
            		Posición: 3		Grabación: Rec3		Artista: Artist3		Título: Track3		Duración: 1080
            Genre List:
            		Pop
            		Dance
            		Hip Hop
            Rating List:
            		4		5		3
            """, artistList.toString());
    }

    @Test
    void TestNameLengthSort(){

        //NameLengthSort (Inversa)-> Artista2(Freddie), Artista3(Michael), Artista1(Eminem)
        artistList.setComparator(new NameLengthReverseComparator());
        artistList.sort();
        assertEquals("""
            
            
            Nombre: Freddie Mercury		ID: 002
            Release List:
            		Título: Rel1		Artista: Artist1		ID: 300
            		Título: Rel2		Artista: Artist1		ID: 200
            Recording List:
            		Título: Rec1		Artista: Artist1		ID: 001
            		Título: Rec2		Artista: Artist2		ID: 002
            Work List:
            		Título: Work1		Artistas: Artists1		ID: 010
            		Título: Work2		Artistas: Artists2		ID: 020
            Track List:
            		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
            		Posición: 2		Grabación: Rec2		Artista: Artist2		Título: Track2		Duración: 720
            Genre List:
            		Rock
            		New Age
            Rating List:
            		4		5
            
            
            Nombre: Michael Jackson		ID: 003
            Release List:
            		Título: Rel1		Artista: Artist1		ID: 300
            		Título: Rel2		Artista: Artist1		ID: 200
            		Título: Rel3		Artista: Artist1		ID: 100
            Recording List:
            		Título: Rec1		Artista: Artist1		ID: 001
            		Título: Rec2		Artista: Artist2		ID: 002
            		Título: Rec3		Artista: Artist3		ID: 003
            Work List:
            		Título: Work1		Artistas: Artists1		ID: 010
            		Título: Work2		Artistas: Artists2		ID: 020
            		Título: Work3		Artistas: Artists3		ID: 030
            Track List:
            		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
            		Posición: 2		Grabación: Rec2		Artista: Artist2		Título: Track2		Duración: 720
            		Posición: 3		Grabación: Rec3		Artista: Artist3		Título: Track3		Duración: 1080
            Genre List:
            		Pop
            		Dance
            		Hip Hop
            Rating List:
            		4		5		3
            
            
            Nombre: Eminem		ID: 001
            Release List:
            		Título: Rel1		Artista: Artist1		ID: 300
            Recording List:
            		Título: Rec1		Artista: Artist1		ID: 001
            Work List:
            		Título: Work1		Artistas: Artists1		ID: 010
            Track List:
            		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
            Genre List:
            		Rap
            Rating List:
            		4
            """, artistList.toString());
    }

    @Test
    void TestAvgRatingSort(){

        //AvgRating sort -> Artista1(Eminem), Artista3(Michael), Artista2(Freddie)
        artistList.setComparator(new AvgRatingComparator());
        artistList.sort();
        assertEquals("""


            Nombre: Eminem		ID: 001
            Release List:
            		Título: Rel1		Artista: Artist1		ID: 300
            Recording List:
            		Título: Rec1		Artista: Artist1		ID: 001
            Work List:
            		Título: Work1		Artistas: Artists1		ID: 010
            Track List:
            		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
            Genre List:
            		Rap
            Rating List:
            		4
            
            
            Nombre: Michael Jackson		ID: 003
            Release List:
            		Título: Rel1		Artista: Artist1		ID: 300
            		Título: Rel2		Artista: Artist1		ID: 200
            		Título: Rel3		Artista: Artist1		ID: 100
            Recording List:
            		Título: Rec1		Artista: Artist1		ID: 001
            		Título: Rec2		Artista: Artist2		ID: 002
            		Título: Rec3		Artista: Artist3		ID: 003
            Work List:
            		Título: Work1		Artistas: Artists1		ID: 010
            		Título: Work2		Artistas: Artists2		ID: 020
            		Título: Work3		Artistas: Artists3		ID: 030
            Track List:
            		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
            		Posición: 2		Grabación: Rec2		Artista: Artist2		Título: Track2		Duración: 720
            		Posición: 3		Grabación: Rec3		Artista: Artist3		Título: Track3		Duración: 1080
            Genre List:
            		Pop
            		Dance
            		Hip Hop
            Rating List:
            		4		5		3
            
            
            Nombre: Freddie Mercury		ID: 002
            Release List:
            		Título: Rel1		Artista: Artist1		ID: 300
            		Título: Rel2		Artista: Artist1		ID: 200
            Recording List:
            		Título: Rec1		Artista: Artist1		ID: 001
            		Título: Rec2		Artista: Artist2		ID: 002
            Work List:
            		Título: Work1		Artistas: Artists1		ID: 010
            		Título: Work2		Artistas: Artists2		ID: 020
            Track List:
            		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
            		Posición: 2		Grabación: Rec2		Artista: Artist2		Título: Track2		Duración: 720
            Genre List:
            		Rock
            		New Age
            Rating List:
            		4		5
            """, artistList.toString());


    }

    @Test
    void TestEclecticismSort(){

        //Eclecticism Sort -> Artista1(Eminem, 1 género), Artista2(Freddie, 2 géneros), Artista2(Michael, 3 géneros)
        artistList.setComparator(new EclecticismComparator());
        artistList.sort();
        assertEquals("""

                                  
              Nombre: Eminem		ID: 001
              Release List:
              		Título: Rel1		Artista: Artist1		ID: 300
              Recording List:
              		Título: Rec1		Artista: Artist1		ID: 001
              Work List:
              		Título: Work1		Artistas: Artists1		ID: 010
              Track List:
              		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
              Genre List:
              		Rap
              Rating List:
              		4
      
      
              Nombre: Freddie Mercury		ID: 002
              Release List:
              		Título: Rel1		Artista: Artist1		ID: 300
              		Título: Rel2		Artista: Artist1		ID: 200
              Recording List:
              		Título: Rec1		Artista: Artist1		ID: 001
              		Título: Rec2		Artista: Artist2		ID: 002
              Work List:
              		Título: Work1		Artistas: Artists1		ID: 010
              		Título: Work2		Artistas: Artists2		ID: 020
              Track List:
              		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
              		Posición: 2		Grabación: Rec2		Artista: Artist2		Título: Track2		Duración: 720
              Genre List:
              		Rock
              		New Age
              Rating List:
              		4		5
      
      
              Nombre: Michael Jackson		ID: 003
              Release List:
              		Título: Rel1		Artista: Artist1		ID: 300
              		Título: Rel2		Artista: Artist1		ID: 200
              		Título: Rel3		Artista: Artist1		ID: 100
              Recording List:
              		Título: Rec1		Artista: Artist1		ID: 001
              		Título: Rec2		Artista: Artist2		ID: 002
              		Título: Rec3		Artista: Artist3		ID: 003
              Work List:
              		Título: Work1		Artistas: Artists1		ID: 010
              		Título: Work2		Artistas: Artists2		ID: 020
              		Título: Work3		Artistas: Artists3		ID: 030
              Track List:
              		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
              		Posición: 2		Grabación: Rec2		Artista: Artist2		Título: Track2		Duración: 720
              		Posición: 3		Grabación: Rec3		Artista: Artist3		Título: Track3		Duración: 1080
              Genre List:
              		Pop
              		Dance
              		Hip Hop
              Rating List:
              		4		5		3
                """, artistList.toString());




    }

    @Test
    void RecordingListSort(){

        //RecordingList Inverse Sort -> Artista3(Michael, 3 recordings), Artista2(Freddie, 2 recordings), Artista1(Eminem, 1 recording)
        artistList.setComparator(new RecordingListComparator());
        artistList.sort();
        assertEquals("""

                                  
            Nombre: Michael Jackson		ID: 003
            Release List:
            		Título: Rel1		Artista: Artist1		ID: 300
            		Título: Rel2		Artista: Artist1		ID: 200
            		Título: Rel3		Artista: Artist1		ID: 100
            Recording List:
            		Título: Rec1		Artista: Artist1		ID: 001
            		Título: Rec2		Artista: Artist2		ID: 002
            		Título: Rec3		Artista: Artist3		ID: 003
            Work List:
            		Título: Work1		Artistas: Artists1		ID: 010
            		Título: Work2		Artistas: Artists2		ID: 020
            		Título: Work3		Artistas: Artists3		ID: 030
            Track List:
            		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
            		Posición: 2		Grabación: Rec2		Artista: Artist2		Título: Track2		Duración: 720
            		Posición: 3		Grabación: Rec3		Artista: Artist3		Título: Track3		Duración: 1080
            Genre List:
            		Pop
            		Dance
            		Hip Hop
            Rating List:
            		4		5		3
            
            
            Nombre: Freddie Mercury		ID: 002
            Release List:
            		Título: Rel1		Artista: Artist1		ID: 300
            		Título: Rel2		Artista: Artist1		ID: 200
            Recording List:
            		Título: Rec1		Artista: Artist1		ID: 001
            		Título: Rec2		Artista: Artist2		ID: 002
            Work List:
            		Título: Work1		Artistas: Artists1		ID: 010
            		Título: Work2		Artistas: Artists2		ID: 020
            Track List:
            		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
            		Posición: 2		Grabación: Rec2		Artista: Artist2		Título: Track2		Duración: 720
            Genre List:
            		Rock
            		New Age
            Rating List:
            		4		5
            
            
            Nombre: Eminem		ID: 001
            Release List:
            		Título: Rel1		Artista: Artist1		ID: 300
            Recording List:
            		Título: Rec1		Artista: Artist1		ID: 001
            Work List:
            		Título: Work1		Artistas: Artists1		ID: 010
            Track List:
            		Posición: 1		Grabación: Rec1		Artista: Artist1		Título: Track1		Duración: 360
            Genre List:
            		Rap
            Rating List:
            		4
                """, artistList.toString());
    }
}
