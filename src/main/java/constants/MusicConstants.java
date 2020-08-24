package constants;

import java.util.HashMap;

public class MusicConstants {
    public final static String CAT = "https://www.youtube.com/watch?v=8kDeQCr14gI";
    public final static String FAR = "https://www.youtube.com/watch?v=CuqeaZZyL48";
    public final static String BLOCKS = "https://www.youtube.com/watch?v=GOX-cRbXlgU";
    public final static String CHIRP = "https://www.youtube.com/watch?v=qDUESGp-2nM";
    public final static String MALL = "https://www.youtube.com/watch?v=6xZ6zmCoQk4";
    public final static String MELLOHI = "https://www.youtube.com/watch?v=seNu0rueWtQ";
    public final static String STALL = "https://www.youtube.com/watch?v=YRlIl6K6S88";
    public final static String STRAD = "https://www.youtube.com/watch?v=3jg0_QPa4xI";
    public final static String WARD = "https://www.youtube.com/watch?v=5VuDwU4bW8Q";
    public final static String WAIT = "https://www.youtube.com/watch?v=rRgzlfMYnzQ";
    public final static String PIGSTEP = "https://www.youtube.com/watch?v=_5vLDyfb4m8";
    public final static String DISC_11 = "https://www.youtube.com/watch?v=aLf9lfbI5Kg";
    public static final String DISC_13 = "https://www.youtube.com/watch?v=ExhKqGIaAyI";

    public static HashMap<String, String> mapSongs() {
        HashMap<String, String> songs = new HashMap<>();
        songs.put("cat", CAT);
        songs.put("far", FAR);
        songs.put("blocks", BLOCKS);
        songs.put("chirp", CHIRP);
        songs.put("mall", MALL);
        songs.put("mellohi", MELLOHI);
        songs.put("stall", STALL);
        songs.put("strad", STRAD);
        songs.put("ward", WARD);
        songs.put("wait", WAIT);
        songs.put("pigstep", PIGSTEP);
        songs.put("13", DISC_13);
        songs.put("11", DISC_11);
        return songs;
    }
}
