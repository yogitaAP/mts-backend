package groots.sad.project.MassTransitSystem.entity;

public class Bus {

    private String id;
    Route route;
    private int passengerCapacity;
    private int riders;
    private int fuelCapacity;
    private int averageSpeed;
    private int busTime;
    private int busAt;

    public Bus(String id, Route route, int passengerCapacity, int riders, int fuelCapacity, int averageSpeed, int busTime, int busAt) {
        this.id = id;
        this.route = route;
        this.passengerCapacity = passengerCapacity;
        this.riders = riders;
        this.fuelCapacity = fuelCapacity;
        this.averageSpeed = averageSpeed;
        this.busTime = busTime;
        this.busAt = busAt;
    }

    public String getId() {
        return id;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    public int getRiders() {
        return riders;
    }

    public void setRiders(int riders) {
        this.riders = riders;
    }

    public int getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(int averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public int getBusTime() {
        return busTime;
    }

    public void setBusTime(int busTime) {
        this.busTime = busTime;
    }

    public int getBusAt() {
        return busAt;
    }

    private double distanceBetweenTwoStops() {

        BusStop currentStop = route.getCurrentStop(busAt);
        BusStop nextStop = route.getNextStop(busAt);
        double csLatitude = currentStop.getLocation().getLatitude();
        double nsLatitude = nextStop.getLocation().getLatitude();
        double csLongitude = currentStop.getLocation().getLongitude();
        double nsLongitude = nextStop.getLocation().getLongitude();
        double distance = 70.0 * Math.sqrt(Math.pow(csLatitude - nsLatitude, 2) + Math.pow(csLongitude - nsLongitude, 2));
        return distance;
    }

    public int computeBusTravelTime() {

        int travelTime = 1 + (((int) distanceBetweenTwoStops()) * 60 / averageSpeed);
        return travelTime;
    }

    public String displayInfo(){
        return "";
    }

    public int computeBusCost(){
        return 0;
    }

    public void changeRoute(){

    }
}
