package groots.sad.project.MassTransitSystem.service;

import groots.sad.project.MassTransitSystem.EventSimulator;
import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.entity.BusStop;
import groots.sad.project.MassTransitSystem.entity.Route;
import groots.sad.project.MassTransitSystem.manager.SystemEfficiencyCalculator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemConfigurationService {

    EventSimulator eventSimulator;
    SystemEfficiencyCalculator systemEfficiencyCalculator;

    public SystemConfigurationService() {

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

    public Map<String, String> getSystemLogicalTime() {

        Map<String, String> logicalTime = new HashMap<>();
        logicalTime.put("logicalTime", Integer.toString(eventSimulator.getLogicalTime()));
        return logicalTime;
    }

    public void refresh(){
        eventSimulator.refresh();
    }

    public List<Route> getAllRoutes(){
        return eventSimulator.getRoutes();
    }
}
