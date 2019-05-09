package uk.ac.solent.pointsofinterest;

public class Pointsofinterest {
    private String name;
    private String type;
    private String description;
    private double longitude;
    private double latitude;


    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public static void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public double getLatitude() {

        return latitude;
    }

    public void setLatitude(double latitude) {

        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Pointofinterest{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }


    public static void setlongitude(Double longitude) {
        this.longitude = longitude;
    }
}



