package hayatsoftwares.widget.location.Model;

public class Data {
    private String name;
    private String latitude;
    private String longitude;

    public String getName() {
        return name;
    }
    public Data(String name , String latitude , String longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String  longitude) {
        this.longitude = longitude;
    }
}
