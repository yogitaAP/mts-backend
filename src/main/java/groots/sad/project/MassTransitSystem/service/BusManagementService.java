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

import java.util.*;


@Service
public class BusManagementService {


    private EventSimulator eventSimulator;
    private BusManager busManager;
    private LinkedList<Bus> busesToBeProcessed;
    private Map<String, LinkedList<Map<String, String>>> updateBusEvents;
    Stack<History> history = new Stack<>();


    public BusManagementService() {
        this.eventSimulator = EventSimulator.getInstance();
        busManager = eventSimulator.getBusManager();
        busesToBeProcessed = new LinkedList<>();
        updateBusEvents = new HashMap<>();
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
        busManager.moveBus(bus);
        processBusStateChangeEvents(bus);
        BusStop busStop = bus.getCurrentStop();
        addEventToHistory(bus, busStop);
        PassengerManager.managePassengersArrivingOnStop(busStop);
        PassengerManager.updatePassengersLeaveBus(bus, busStop);
        PassengerManager.boardPassengersOnBus(bus, busStop);
        PassengerManager.managePassengersDepartStop(busStop);
        eventSimulator.updateEvents(busManager.createNextMoveBusEvent(bus));
    }

    private void processBusStateChangeEvents(Bus bus) {

        LinkedList<Map<String, String>> busInfoList = updateBusEvents.getOrDefault(bus.getId(),new LinkedList<>());
        busInfoList.forEach(
                busInfo -> busManager.updateBusInfo(busInfo)
        );
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

    public void updateBusInfo(Map<String, String> busInfo) {

        String busId = busInfo.get("busId");
        if (updateBusEvents.containsKey(busId)) {
            LinkedList<Map<String, String>> busInfoList = updateBusEvents.get(busId);
            busInfoList.add(busInfo);
        } else {
            LinkedList<Map<String, String>> busInfoList = new LinkedList<>();
            busInfoList.add(busInfo);
            updateBusEvents.put(busId, busInfoList);
        }
    }
}
