package groots.sad.project.MassTransitSystem.validation;

import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.entity.BusStop;

public final class PassengerValidator {

    public static int allowPassengerCountBoardingBus(Bus bus, int ridersBoardingBus, BusStop stop) {

        if (bus.getPassengerCapacity() < ridersBoardingBus) {
            ridersBoardingBus = bus.getPassengerCapacity();
        }
        if(ridersBoardingBus>stop.getWaitingPassenger()){
            ridersBoardingBus = stop.getWaitingPassenger();
        }
        return ridersBoardingBus;
    }

    public void validateAvailableBusSeats(){

    }

    public static int validatePassengerGetOffTheBus(int ridersLeaveBus, Bus bus) {

        int ridersAllowedToLeaveBus = ridersLeaveBus;
        if (ridersAllowedToLeaveBus > bus.getPassengerCapacity()) {
            ridersAllowedToLeaveBus = bus.getPassengerCapacity();
        }
        if (ridersAllowedToLeaveBus > bus.getRiders()) {
            ridersAllowedToLeaveBus = bus.getRiders();
        }
        return ridersAllowedToLeaveBus;
    }

    public static int validatePassengerDepartStop(int ridersDepartStop, BusStop stop){
        int passengerToBeRemovedFromWaiting = ridersDepartStop - stop.getTransfersPassenger();
        if(passengerToBeRemovedFromWaiting>stop.getWaitingPassenger()){
            passengerToBeRemovedFromWaiting = stop.getWaitingPassenger();
        }
        return passengerToBeRemovedFromWaiting;
    }
}
