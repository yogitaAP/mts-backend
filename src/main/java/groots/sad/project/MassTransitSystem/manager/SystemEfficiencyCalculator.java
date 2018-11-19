package groots.sad.project.MassTransitSystem.manager;

import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.entity.BusStop;

import java.util.List;

/**
 * The class is singleton class which will calculate the efficiency of the system.
 * The object instantiation is an eager instantiation to avoid concurrency issues.
 */
public class SystemEfficiencyCalculator {

    private double kSpeed;
    private double kCapacity;
    private double kWaiting;
    private double kBuses;
    private double kCombined;


    private static SystemEfficiencyCalculator systemEfficiencyCalculator = new SystemEfficiencyCalculator();

    private SystemEfficiencyCalculator() {
    }

    public static SystemEfficiencyCalculator getInstance() {
        return systemEfficiencyCalculator;
    }

    public double computeSystemEfficiency(List<Bus> buses, List<BusStop> busStops) {

        int waitingPassengerAtAllStops = calculateWaitingPassengerAtAllStops(busStops);
        double busCostForAllTheBuses = calculateCostForAllBuses(buses);
        double systemEfficiency = kWaiting * waitingPassengerAtAllStops + kBuses * busCostForAllTheBuses
                + kCombined * waitingPassengerAtAllStops * busCostForAllTheBuses;
        return systemEfficiency;
    }

    private double calculateCostForAllBuses(List<Bus> buses) {
        return buses.stream()
                .mapToDouble(bus -> kSpeed * bus.getAverageSpeed() + kCapacity * bus.getPassengerCapacity())
                .sum();
    }

    private int calculateWaitingPassengerAtAllStops(List<BusStop> busStops) {
        return busStops.stream()
                .mapToInt(BusStop::getWaitingPassenger)
                .sum();
    }

    public void setConstants(double kSpeed, double kCapacity, double kWaiting, double kBuses, double kCombined) {
        this.kSpeed = kSpeed;
        this.kCapacity = kCapacity;
        this.kWaiting = kWaiting;
        this.kBuses = kBuses;
        this.kCombined = kCombined;
    }
}
