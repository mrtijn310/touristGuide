package nl.xannic.minor.touristguideapplicatie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Martijn on 8-12-2014.
 */
public class Data {

    public static ArrayList<Item> itemList = new ArrayList<Item>();
    public static String cityName = "City";
    public static double lat, lon;

    public ArrayList getSortedList(ArrayList<Item> list) {
        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                Item item1 = (Item) o1;
                Item item2 = (Item) o2;
                return Double.compare(item1.getDistance(), item2.getDistance());
            }
        });

        return list;
    }
}
