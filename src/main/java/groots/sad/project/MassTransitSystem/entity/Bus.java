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


    public void setRiders(int riders) {
        this.riders = riders;
    }

    /**
     * compute the distance between the current and the next stop
     * @return
     */
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

    /**
     * compute the travel time taken to move from the current stop to the next stop
     * @return
     */
    public int computeBusTravelTime() {

        int travelTime = 1 + (((int) distanceBetweenTwoStops()) * 60 / averageSpeed);
        System.out.println("travel time piyush"+travelTime);
        return travelTime;
    }

    public String displayInfo() {
        return "";
    }

    public double computeBusCost(double kSpeed, double kCapacity) {
        return kSpeed * averageSpeed + kCapacity * passengerCapacity;
    }

    public void changeRoute(Route route, int nextStop) {

        this.route = route;
        busAt = (nextStop + route.getStops().size() - 1) % route.getStops().size();
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    public int getBusTime() {
        return busTime;
    }

    public void moveNextStop() {

        busTime += computeBusTravelTime();
        busAt = (busAt + 1) % route.getStops().size();
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public BusStop getCurrentStop() {

        return route.getCurrentStop(busAt);
    }

    public BusStop getNextStop() {

        return route.getNextStop(busAt);
    }

    public Event createNextMoveEvent() {
        return new Event(busTime, MOVE_BUS, id);
    }

    public Map<String,String> computeDisplayInfo(){

        Map<String, String> displayInfo = new HashMap<>();
        displayInfo.put("bus_id",id);
        displayInfo.put("current_stop",getCurrentStop().getId());
        displayInfo.put("next_stop", route.getNextStop(busAt).getId());
        displayInfo.put("travel_time", Integer.toString(busTime));
        displayInfo.put("riders", Integer.toString(riders));
        displayInfo.put("passengerCapacity",Integer.toString(passengerCapacity));
        return displayInfo;
    }

}
