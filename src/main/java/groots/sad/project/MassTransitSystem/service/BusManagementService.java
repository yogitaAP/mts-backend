package groots.sad.project.MassTransitSystem.service;

import groots.sad.project.MassTransitSystem.EventSimulator;
import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.entity.BusStop;
import groots.sad.project.MassTransitSystem.entity.Event;
import groots.sad.project.MassTransitSystem.manager.BusManager;
import groots.sad.project.MassTransitSystem.manager.PassengerManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class BusManagementService {

    private EventSimulator eventSimulator;
    private BusManager busManager;
    private LinkedList<Bus> busesToBeProcessed;

    public BusManagementService() {
        this.eventSimulator = EventSimulator.getInstance();
        busManager = eventSimulator.getBusManager();
        busesToBeProcessed = new LinkedList<>();
    }

    public void moveBus() {

        if (busesToBeProcessed.isEmpty()) {
            List<Event> events = eventSimulator.prepareEvents();
            if (events.isEmpty()) {
                System.out.println("No buses to be processed at this logical time");
                eventSimulator.increaseLogicalTime();
                return;
            }
            busesToBeProcessed.addAll(busManager.selectBusesToMove(events));
        }
        Bus bus = busesToBeProcessed.getFirst();
        //processBusStateChangeEvents(bus);
        busManager.moveBus(bus);
        BusStop busStop = bus.getCurrentStop();
        PassengerManager.managePassengersArrivingOnStop(busStop);
        PassengerManager.updatePassengersLeaveBus(bus, busStop);
        PassengerManager.boardPassengersOnBus(bus, busStop);
        PassengerManager.managePassengersDepartStop(busStop);
        eventSimulator.updateEvents(busManager.createNextMoveBusEvent(bus));
    }

    private void processBusStateChangeEvents(Bus bus) {

        // need to discuss with you guys
    }

    public List<Bus> getAllBuses() {
        return busManager.getAllBuses();
    }

    public List<Map<String, String>> getDisplayInfo() {

        List<Bus> buses = busManager.getAllBuses();
        List<Map<String, String>> displayInfoList = new ArrayList<>();
        buses.forEach(bus -> displayInfoList.add(bus.computeDisplayInfo()));
        return displayInfoList;
    }
}
