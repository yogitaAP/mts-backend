package groots.sad.project.MassTransitSystem.service;

import groots.sad.project.MassTransitSystem.EventSimulator;
import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.entity.BusStop;
import groots.sad.project.MassTransitSystem.entity.Event;
import groots.sad.project.MassTransitSystem.manager.BusManager;
import groots.sad.project.MassTransitSystem.manager.EventHistoryManager;
import groots.sad.project.MassTransitSystem.manager.PassengerManager;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BusManagementService {


    private EventSimulator eventSimulator;
    private BusManager busManager;
    private LinkedList<Bus> busesToBeProcessed;
    private Map<String, LinkedList<Map<String, String>>> updateBusEvents;


    public BusManagementService() {
        this.eventSimulator = EventSimulator.getInstance();
        busManager = eventSimulator.getBusManager();
        busesToBeProcessed = new LinkedList<>();
        updateBusEvents = new HashMap<>();
    }

    public void moveBus() {

        int logicalTime = eventSimulator.getLogicalTime();
        if (busesToBeProcessed.isEmpty()) {
            Event event = eventSimulator.prepareEvent();
            while (event == null) {
                eventSimulator.increaseLogicalTime();
                event = eventSimulator.prepareEvent();
            }
            busesToBeProcessed.add(busManager.selectBusesToMove(event));
        }
        Bus bus = busesToBeProcessed.removeFirst();
        System.out.println("Bus currently moving is :" + bus.getId());
        addEventToHistory(bus, bus.getNextStop(),logicalTime);
        busManager.moveBus(bus);
        processBusStateChangeEvents(bus);
        BusStop busStop = bus.getCurrentStop();
        PassengerManager.managePassengersArrivingOnStop(busStop);
        PassengerManager.updatePassengersLeaveBus(bus, busStop);
        PassengerManager.boardPassengersOnBus(bus, busStop);
        PassengerManager.managePassengersDepartStop(busStop);
        eventSimulator.updateEventQueue(busManager.createNextMoveBusEvent(bus));
    }

    private void processBusStateChangeEvents(Bus bus) {

        LinkedList<Map<String, String>> busInfoList = updateBusEvents.getOrDefault(bus.getId(),new LinkedList<>());
        busInfoList.forEach(
                busInfo -> busManager.updateBusInfo(busInfo)
        );
    }

    private void addEventToHistory(Bus bus, BusStop stop,int logicalTime) {

        EventHistoryManager eventHistoryManager = EventHistoryManager.getInstance();
        eventHistoryManager.addEventToHistory(bus,stop,logicalTime);
    }

    public void replay(){

        EventHistoryManager eventHistoryManager = EventHistoryManager.getInstance();
        eventHistoryManager.replay();
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
