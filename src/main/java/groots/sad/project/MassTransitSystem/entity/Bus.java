package groots.sad.project.MassTransitSystem.entity;

import java.util.HashMap;
import java.util.Map;

public class Bus {

    private String id;
    Route route;
    private int passengerCapacity;
    private int riders;
    private int averageSpeed;
    private int busTime;
    private int busAt;

    private static final String MOVE_BUS = "move_bus";

    public Bus(String id, Route route, int passengerCapacity, int averageSpeed, int busAt) {
        this.id = id;
        this.route = route;
        this.passengerCapacity = passengerCapacity;
        this.riders = 0;
        this.averageSpeed = averageSpeed;
        this.busTime = 0;
        this.busAt = busAt;
    }

    public String getId() {
        return id;
    }

    public int getRiders() {
        return riders;
    }

    public void increaseRidersOnBus(int riders) {
        this.riders += riders;
    }

    public void decreaseRidersOnBus(int riders) {
        this.riders -= riders;
    }

    public void setAverageSpeed(int averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public void setBusTime(int busTime) {
        this.busTime = busTime;
    }

    public int getBusAt() {
        return busAt;
    }

    public void setBusAt(int busAt) {
        this.busAt = busAt % route.getStops().size();
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

    public String displayInfo() {
        return "";
    }

    public double computeBusCost(double kSpeed, double kCapacity) {
        return kSpeed * averageSpeed + kCapacity * passengerCapacity;
    }

    public void changeRoute() {

    }

    public void moveNextStop() {

        busTime += computeBusTravelTime();
        busAt = (busAt + 1) % route.getStops().size();
    }

    public BusStop getCurrentStop() {

        return route.getCurrentStop(busAt);
    }

    public Event createNextMoveEvent() {
        return new Event(busTime, MOVE_BUS, id);
    }

    public Map<String,String> computeDisplayInfo(){

        Map<String, String> displayInfo = new HashMap<>();
        displayInfo.put("bus_id",id);
        displayInfo.put("current_stop",getCurrentStop().getId());
        displayInfo.put("next_stop", route.getNextStop(busAt).getId());
        displayInfo.put("travel_time", Integer.toString(computeBusTravelTime()));
        displayInfo.put("riders", Integer.toString(riders));
        return displayInfo;
    }

}
