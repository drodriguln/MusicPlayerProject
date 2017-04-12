package com.developer.drodriguez;

import com.developer.drodriguez.model.Album;
import com.developer.drodriguez.model.Artist;
import com.developer.drodriguez.model.Song;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Daniel on 3/31/17.
 */
public class SongServiceTest {

    @Test
    public void removeFilesTest() {
        String filePath = "/Users/Daniel/Music/library/Foo Fighters/Greatest Hits/Times Like These.mp3";
        File songFile = new File(filePath);
        File albumFolder = songFile.getParentFile();
        File artistFolder = albumFolder.getParentFile();
        if (songFile.isFile())
            if (songFile.exists()) {
                System.out.println("Deleting Song...");
                //songFile.delete();
            }
        if (albumFolder.isDirectory())
            if (albumFolder.list().length == 0)
                System.out.println("Delete Album");
        //albumFolder.delete();
        if (artistFolder.isDirectory())
            if (artistFolder.list().length == 0)
                System.out.println("Delete Artist");
        //artistFolder.delete();
    }

    @Test
    public void testSong() {
        Map<Integer, Song> songMap = new TreeMap<>();
        songMap.put(1, new Song(1, 1, 0, "I Believe", "2007", "/Users/Daniel/Music/library/Dustin Kensrue/Please Come Home/I Believe.mp3") );
        songMap.put(2, new Song(2, 1, 0, "Consider the Ravens", "2007","/Users/Daniel/Music/library/Dustin Kensrue/Please Come Home/Consider the Ravens.mp3") );
        System.out.println(songMap.get(2));
        assertTrue(songMap.containsValue(songMap.get(2)));
    }

    @Test
    public void songSort() {
        Map<Integer, Artist> artistMap = new TreeMap<>();
        List<Artist> newList = new ArrayList<>();

        artistMap.put(1, new Artist(1, "Apples"));
        artistMap.put(2, new Artist(2, "Chocolate"));
        artistMap.put(3, new Artist(3, "Bananas"));

        System.out.println("BEFORE SORT");
        for (Artist artist : artistMap.values())
            newList.add(artist);

        Collections.sort(newList);

        System.out.println("AFTER SORT");
        for (Artist artist : newList)
            System.out.println(artist);
    }

    @Test
    public void removeInvalidPathCharacters() {
        String originalString ="AC/DC";
        char[] originalChars = originalString.toCharArray();
        char[] badChars = {'\\', '/', ':', '*', '?', '<', '>', '|', ']'};
        for (int i = 0; i < originalChars.length; i++)
            for (int j = 0; j < badChars.length; j++)
                if (originalChars[i] == badChars[j])
                    originalChars[i] = '_';
        System.out.println(new String(originalChars));
    }

    @Test
    public void convertTrackToInteger() {
        String tagTrack = "1/12";
        int tTrack = 0;
        if (tagTrack != null)
            if (tagTrack.contains("/"))
                tTrack = Integer.parseInt(tagTrack.substring(0, tagTrack.lastIndexOf("/"))); //Substring removes "out of total tracks" (x"/xx") extension.
            else
                tTrack = Integer.parseInt(tagTrack);
        System.out.println(tTrack);
    }

    @Test
    public void readLibraryFile() throws IOException, FileNotFoundException {

        Scanner scanner = new Scanner(new FileReader(("/Users/Daniel/Music/library/library.mpl")));
        Map<Integer, Artist> artistMap = new HashMap<>();
        Map<Integer, Album> albumMap = new HashMap<>();
        Map<Integer, Song> songMap = new HashMap<>();
        String escDelimiter = "\\|";
        String delimiter = "|";
        String[] indices = null;
        String section = null;
        String line = null;

        //Only read file if it contains the proper header.
        if (!scanner.nextLine().equals("**MPLIBRARY**"))
            return;

        if (scanner.nextLine().equals("--INDEX--"))
            indices = scanner.nextLine().split(escDelimiter);

        int artistIndex = Integer.parseInt(indices[0]);
        int albumIndex = Integer.parseInt(indices[1]);
        int songIndex = Integer.parseInt(indices[2]);

        System.out.println(artistIndex + " " + albumIndex + " " + songIndex);

        System.out.println("PRINT SONGS:");
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.equals("--ARTIST--") || line.equals("--ALBUM--") || line.equals("--SONG--")) {
                section = line;
                line = scanner.nextLine();
            }
            if (line.equals("**MPEND**"))
                break;

            String[] fields = line.split(delimiter);

            if (section.equals("--ARTIST--") && !line.equals("--ALBUM--"))
                artistMap.put(Integer.parseInt(fields[0]), new Artist(Integer.parseInt(fields[0]), fields[1]));
            else if (section.equals("--ALBUM--") && !line.equals("--SONG--"))
                albumMap.put(Integer.parseInt(fields[0]), new Album(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), fields[2]));
            else if (section.equals("--SONG--"))
                songMap.put(Integer.parseInt(fields[0]), new Song(Integer.parseInt(fields[0]),
                        Integer.parseInt(fields[1]), Integer.parseInt(fields[2]), fields[3], fields[4], fields[5]));
        }

        scanner.close();

        for (Artist artist : artistMap.values())
            System.out.println(artist);
        for (Album album : albumMap.values())
            System.out.println(album);
        for (Song song : songMap.values())
            System.out.println(song);

    }

}
