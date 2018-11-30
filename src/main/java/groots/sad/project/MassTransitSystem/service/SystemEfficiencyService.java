package groots.sad.project.MassTransitSystem.service;

import groots.sad.project.MassTransitSystem.EventSimulator;
import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.entity.BusStop;
import groots.sad.project.MassTransitSystem.manager.SystemEfficiencyCalculator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemEfficiencyService {

    EventSimulator eventSimulator;
    SystemEfficiencyCalculator systemEfficiencyCalculator;

    public SystemEfficiencyService() {

        eventSimulator = EventSimulator.getInstance();
        systemEfficiencyCalculator = SystemEfficiencyCalculator.getInstance();
    }

    public double computeSystemEfficiency(){

        List<Bus> buses = eventSimulator.getBusManager().getAllBuses();
        List<BusStop> busStops = eventSimulator.getBusStops();
        double systemEfficiency = systemEfficiencyCalculator.computeSystemEfficiency(buses,busStops);
        return systemEfficiency;
    }

    public void setKConstants(double kSpeed,double kCapacity,double kWaiting, double kBuses, double kCombined){

        SystemEfficiencyCalculator systemEfficiencyCalculator = SystemEfficiencyCalculator.getInstance();
        systemEfficiencyCalculator.setkSpeed(kSpeed)
                .setkCapacity(kCapacity)
                .setkWaiting(kWaiting)
                .setkBuses(kBuses)
                .setkCombined(kCombined);
    }
}
