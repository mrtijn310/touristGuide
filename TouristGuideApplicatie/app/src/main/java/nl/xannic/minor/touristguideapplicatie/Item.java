package nl.xannic.minor.touristguideapplicatie;

/**
 * Created by Xander on 11/25/2014.
 */
public class Item {
    private int ID, CityID, CategoryID;
    private double lat, lon, distance;
    private String Name, Information, Image, Category;


    public Item (int ID, String Name, String Information, double lat, double lon, int CityID, int CategoryID, String Image){
        this.ID = ID;
        this.Name = Name;
        this.Information = Information;
        this.lat = lat;
        this.lon = lon;
        this.CityID = CityID;
        this.CategoryID = CategoryID;
        this.Image = Image;

//        categories.add("Monumenten");
//        categories.add("Eetcafes");
//        categories.add("Evenementen");
//        categories.add("Musea");

        switch(CategoryID) {
            case 0:
                this.Category = "Monumenten";
                break;
            case 1:
                this.Category = "Eetcafes";
                break;
            case 2:
                this.Category = "Evenementen";
                break;
            case 3:
                this.Category = "Musea";
                break;
            default:
                this.Category = "Onbekend";
        }
    }

    public Item (int ID, int CategoryID, double lat, double lon, double distance, String Name , String Image){
        this.ID = ID;
        this.CategoryID = CategoryID;
        this.lon = lon;
        this.lat = lat;
        this.distance = distance;
        this.Name = Name;
        this.Image = Image;

        switch(CategoryID) {
            case 0:
                this.Category = "Monumenten";
                break;
            case 1:
                this.Category = "Eetcafes";
                break;
            case 2:
                this.Category = "Evenementen";
                break;
            case 3:
                this.Category = "Musea";
                break;
            default:
                this.Category = "Onbekend";
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getDistance() {
        return lat;
    }

    public void setDistance(double lat) {
        this.lat = lat;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getInformation() {
        return Information;
    }

    public void setInformation(String information) {
        Information = information;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
