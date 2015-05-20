package nl.tudelft.ti2806.riverrush;

/**
 * Created by Martijn on 20-5-2015.
 */
public class Assets {

    public static String getFileName(String name) {
        return "data/" + name;
    }

    public static String boat = getFileName("boat.jpg");
    public static String shipv2 = getFileName("shipv2.png");
    public static String ship = getFileName("ship.png");
    public static String raccoon = getFileName("raccoon.png");
    public static String pirateship = getFileName("pirateship.png");
    public static String left = getFileName("left.jpg");
    public static String grass = getFileName("grass.jpg");
    public static String river = getFileName("river.jpg");
    public static String cannonball = getFileName("cannonball.png");
    public static String win = getFileName("win.png");
    public static String lose = getFileName("lose.png");
    public static String loading = getFileName("loading.jpeg");


}
