package groots.sad.project.MassTransitSystem.entity;

public class Location {

    private double longitude;
    private double latitude;

    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    double getLongitude() {
        return longitude;
    }

    double getLatitude() {
        return latitude;
    }
}
