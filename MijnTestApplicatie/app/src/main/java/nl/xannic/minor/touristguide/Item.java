package nl.xannic.minor.touristguide;

/**
 * Created by Martijn on 26-11-2014.
 */
public class Item {

    private int ID;
    private int category;
    private double longitude;
    private double latitude;
    private double distance;
    private String name;
    private String imageSource;

    public Item()
    {

    }

    public Item (int ID, int category, double longitude, double latitude, double distance, String name, String imageSource)
    {
        this.ID = ID;
        this.category = category;
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance= distance;
        this.name = name;
        this.imageSource = imageSource;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }
}
