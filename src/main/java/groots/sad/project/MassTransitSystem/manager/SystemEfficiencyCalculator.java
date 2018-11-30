package groots.sad.project.MassTransitSystem.manager;

import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.entity.BusStop;

import java.util.List;

/**
 * The class is singleton class which will calculate the efficiency of the system.
 * The object instantiation is an eager instantiation to avoid concurrency issues.
 * The class is responsible to calculate the efficiency of the system
 */
public class SystemEfficiencyCalculator {

    private double kSpeed;
    private double kCapacity;
    private double kWaiting;
    private double kBuses;
    private double kCombined;


    private static SystemEfficiencyCalculator systemEfficiencyCalculator = new SystemEfficiencyCalculator();

    private SystemEfficiencyCalculator() {
        kSpeed = 1.0;
        kCapacity= 1.0;
        kWaiting = 1.0;
        kBuses = 1.0;
        kCombined = 1.0;
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
                .mapToDouble(bus -> bus.computeBusCost(kSpeed, kCapacity))
                .sum();
    }

    private int calculateWaitingPassengerAtAllStops(List<BusStop> busStops) {
        return busStops.stream()
                .mapToInt(BusStop::getWaitingPassenger)
                .sum();
    }


    // Using builder pattern so that we can set all the values at a time.

    public SystemEfficiencyCalculator setkSpeed(double kSpeed) {
        this.kSpeed = kSpeed;
        return this;
    }

    public SystemEfficiencyCalculator setkCapacity(double kCapacity) {
        this.kCapacity = kCapacity;
        return this;
    }

    public SystemEfficiencyCalculator setkWaiting(double kWaiting) {
        this.kWaiting = kWaiting;
        return this;
    }

    public SystemEfficiencyCalculator setkBuses(double kBuses) {
        this.kBuses = kBuses;
        return this;
    }

    public SystemEfficiencyCalculator setkCombined(double kCombined) {
        this.kCombined = kCombined;
        return this;
    }
}
