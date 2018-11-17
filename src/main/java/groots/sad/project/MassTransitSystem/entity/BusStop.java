package groots.sad.project.MassTransitSystem.entity;

public class BusStop {

    private String id;
    private String name;
    private int passengerCapacity;
    private Location location;
    private int waitingPassenger;
    private int transfersPassenger;

    public BusStop(String id, String name, int passengerCapacity, Location location, int waitingPassenger, int transfersPassenger) {
        this.id = id;
        this.name = name;
        this.passengerCapacity = passengerCapacity;
        this.location = location;
        this.waitingPassenger = waitingPassenger;
        this.transfersPassenger = transfersPassenger;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public Location getLocation() {
        return location;
    }

    public int getWaitingPassenger() {
        return waitingPassenger;
    }

    public int getTransfersPassenger() {
        return transfersPassenger;
    }

    public void updateWatingPassenger(){

    }

    public void updateTransfersPassenger(){

    }

}
