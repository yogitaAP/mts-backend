package groots.sad.project.MassTransitSystem.service;

import groots.sad.project.MassTransitSystem.EventSimulator;
import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.entity.BusStop;
import groots.sad.project.MassTransitSystem.entity.Event;
import groots.sad.project.MassTransitSystem.entity.History;
import groots.sad.project.MassTransitSystem.manager.BusManager;
import groots.sad.project.MassTransitSystem.manager.PassengerManager;
import groots.sad.project.MassTransitSystem.manager.EventHistoryManager;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;


@Service
public class BusManagementService {


    private EventSimulator eventSimulator;
    private BusManager busManager;
    private LinkedList<Bus> busesToBeProcessed;
    Stack<History> history = new Stack<>();


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
        processBusStateChangeEvents(bus);
        addEventToHistory(bus, busStop);
        PassengerManager.managePassengersArrivingOnStop(busStop);
        PassengerManager.updatePassengersLeaveBus(bus, busStop);
        PassengerManager.boardPassengersOnBus(bus, busStop);
        PassengerManager.managePassengersDepartStop(busStop);
        eventSimulator.updateEvents(busManager.createNextMoveBusEvent(bus));
    }

    private void processBusStateChangeEvents(Bus bus) {

        // need to discuss with you guys
    }

    private void addEventToHistory(Bus bus, BusStop stop) {

        String busId = bus.getId();
        int busTime = bus.getBusTime();
        int busRiders = bus.getRiders();
        String stopId = stop.getId();
        int stopPassengers = stop.getWaitingPassenger();
        History newHistory = new History(busId, busTime, busRiders, stopId, stopPassengers);
        history.push(newHistory);
    }

    public void replay(){
        if(!CollectionUtils.isEmpty(history)) {
            EventHistoryManager.replay(history.pop());
        }
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
