package nl.xannic.minor.rondjerotterdam;

/**
 * Created by Xander on 12/3/2014.
 */
public class Category {
    private int id;
    private String name;

    Category(int id, String name){
        this.id = id;
        this.name =  name;
    }

    public int getID() {
        return id;
    }

    public void setID(int iD) {
        id = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
