package groots.sad.project.MassTransitSystem.validation;

import groots.sad.project.MassTransitSystem.entity.Bus;

public class PassengerValidator {

    public static int allowPassengerCountBoardingBus(Bus bus, int ridersBoardingBus) {

        if (bus.getPassengerCapacity() < ridersBoardingBus) {
            ridersBoardingBus = bus.getPassengerCapacity();
        }
        return ridersBoardingBus;
    }

    public void validateAvailableBusSeats(){

    }

    public void validatePassengerGetOffTheBus(){


    }
}
