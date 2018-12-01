package groots.sad.project.MassTransitSystem.manager;

import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.entity.BusStop;
import groots.sad.project.MassTransitSystem.validation.PassengerValidator;

/**
 * This is class does not have any instance variable so making all methods static.
 */
public final class PassengerManager {

    public static void managePassengersArrivingOnStop(BusStop busStop){

        busStop.passengersArriveAtStop();
    }

    public static void updatePassengersLeaveBus(Bus bus, BusStop stop){

        int ridersLeaveBus = UniformDistributionCalculator.generateRandomNumber(stop.getrOffLow(),stop.getrOffHigh());
        System.out.println("riders leave bus" + ridersLeaveBus);
        ridersLeaveBus = PassengerValidator.validatePassengerGetOffTheBus(ridersLeaveBus,bus);
        bus.decreaseRidersOnBus(ridersLeaveBus);
        stop.updateTransfersPassenger(ridersLeaveBus);
    }

    public static void boardPassengersOnBus(Bus bus, BusStop stop) {

        int ridersBoardBus = UniformDistributionCalculator.generateRandomNumber(stop.getrOnLow(), stop.getrOnHigh());
        int allowRidersToBoardBus = PassengerValidator.allowPassengerCountBoardingBus(bus, ridersBoardBus,stop);
        bus.increaseRidersOnBus(allowRidersToBoardBus);
        System.out.println("passenger board bus" + allowRidersToBoardBus);
        stop.decreaseWaitingPassenger(allowRidersToBoardBus);
    }

    public static void managePassengersDepartStop(BusStop stop) {

        int ridersDepartStop = UniformDistributionCalculator.generateRandomNumber(stop.getrDepartLow(), stop.getrDepartHigh());
        if (ridersDepartStop <= stop.getTransfersPassenger()) {

            int passengersToBeAddedToWaiting = stop.getTransfersPassenger() - ridersDepartStop;
            stop.setTransfersPassenger(0);
            stop.increaseWaitingPassenger(passengersToBeAddedToWaiting);
        } else {
            int passengerToBeRemovedFromWaiting = PassengerValidator.validatePassengerDepartStop(ridersDepartStop,stop);
            stop.setTransfersPassenger(0);
            stop.decreaseWaitingPassenger(passengerToBeRemovedFromWaiting);
        }
    }
}
