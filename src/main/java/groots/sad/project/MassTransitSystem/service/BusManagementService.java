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

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

@Service
public class BusManagementService {

    EventSimulator eventSimulator;
    BusManager busManager;
    LinkedList<Bus> busesToBeProcessed;
    //EventHistoryManager eventHistoryManager;
    Stack<History> history = new Stack<>();

    public BusManagementService() {
        this.eventSimulator = EventSimulator.getInstance();
        busManager = eventSimulator.getBusManager();
        busesToBeProcessed = new LinkedList<>();
    }

    public void moveBus(){

        if(busesToBeProcessed.isEmpty()){
            List<Event> events = eventSimulator.prepareEvents();
            busesToBeProcessed.addAll(busManager.selectBusesToMove(events));
        }

        Bus bus = busesToBeProcessed.getFirst();
        processBusStateChangeEvents(bus);
        BusStop busStop = bus.getNextStop();
        addEventToHistory(bus, busStop);
        PassengerManager.managePassengersArrivingOnStop(busStop);
        PassengerManager.updatePassengersLeaveBus(bus,busStop);
        PassengerManager.boardPassengersOnBus(bus,busStop);
        PassengerManager.managePassengersDepartStop(busStop);
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
        if(history.size() >0)
        {
            EventHistoryManager.replay(history.pop());
        }
    }

    public List<Bus> getAllBuses() {
        return busManager.getAllBuses();
    }
}
