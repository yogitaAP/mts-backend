package groots.sad.project.MassTransitSystem.entity;

import groots.sad.project.MassTransitSystem.manager.UniformDistributionCalculator;

public class BusStop {

    private String id;
    private String name;
    private int passengerCapacity;
    private Location location;
    private int waitingPassenger;
    private int transfersPassenger;

    private int rArriveHigh;
    private int rArriveLow;
    private int rOffHigh;
    private int rOffLow;
    private int rOnHigh;
    private int rOnLow;
    private int rDepartHigh;
    private int rDepartLow;

    public BusStop(String id, String name, int passengerCapacity, Location location) {
        this.id = id;
        this.name = name;
        this.passengerCapacity = passengerCapacity;
        this.location = location;
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

    public void passengersArriveAtStop(){

        this.waitingPassenger += UniformDistributionCalculator.generateRandomNumber(rArriveLow, rArriveHigh);
        System.out.println("passengers arrived at the stop"+ waitingPassenger);
    }

    public void decreaseWaitingPassenger(int passenger){

        this.waitingPassenger -= passenger;
    }

    public void increaseWaitingPassenger(int passenger) {

        this.waitingPassenger += passenger;
    }

    public void updateTransfersPassenger(int transfersPassenger){
        this.transfersPassenger = transfersPassenger;
    }

    public BusStop setrArriveHigh(int rArriveHigh) {
        this.rArriveHigh = rArriveHigh;
        return this;
    }

    public BusStop setrArriveLow(int rArriveLow) {
        this.rArriveLow = rArriveLow;
        return this;
    }

    public BusStop setrOffHigh(int rOffHigh) {
        this.rOffHigh = rOffHigh;
        return this;
    }

    public BusStop setrOffLow(int rOffLow) {
        this.rOffLow = rOffLow;
        return this;
    }

    public BusStop setrOnHigh(int rOnHigh) {
        this.rOnHigh = rOnHigh;
        return this;
    }

    public BusStop setrOnLow(int rOnLow) {
        this.rOnLow = rOnLow;
        return this;
    }

    public BusStop setrDepartHigh(int rDepartHigh) {
        this.rDepartHigh = rDepartHigh;
        return this;
    }

    public BusStop setrDepartLow(int rDepartLow) {
        this.rDepartLow = rDepartLow;
        return this;
    }

    public int getrOffHigh() {
        return rOffHigh;
    }

    public int getrOffLow() {
        return rOffLow;
    }

    public int getrOnHigh() {
        return rOnHigh;
    }

    public int getrOnLow() {
        return rOnLow;
    }

    public int getrDepartHigh() {
        return rDepartHigh;
    }

    public int getrDepartLow() {
        return rDepartLow;
    }

    public void setTransfersPassenger(int transfersPassenger) {
        this.transfersPassenger = transfersPassenger;
    }

    public void setWaitingPassenger(int waitingPassenger) {
        this.waitingPassenger = waitingPassenger;
    }
}
