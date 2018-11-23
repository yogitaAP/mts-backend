package groots.sad.project.MassTransitSystem.manager;

import groots.sad.project.MassTransitSystem.comparator.LowRunningTimeComparator;
import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.entity.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusManager {

    private List<Bus> buses;
    private Map<String, Integer> busStartTime;
    private Map<String, Bus> idToBusMap;
    private static BusManager busManager = new BusManager();

    private BusManager() {

        this.buses = new ArrayList<>();
    }

    public static BusManager getInstance(){
        return busManager;
    }

    public void addBus(Bus bus) {

        buses.add(bus);
    }

    public void moveBus(Bus bus) {

        bus.moveNextStop();
    }

    public List<Bus> selectBusesToMove(List<Event> events) {

        List<Bus> selectedBuses = new ArrayList<>();
        events.forEach(event -> {
            Bus bus = idToBusMap.get(event.getBusId());
            selectedBuses.add(bus);
        });
        LowRunningTimeComparator comparator = new LowRunningTimeComparator();
        selectedBuses.sort(comparator);
        return selectedBuses;
    }

    public void prepareBusStartTimeData(List<Event> events) {

        busStartTime = new HashMap<>();
        events.forEach(event -> busStartTime.put(event.getBusId(), event.getTime()));
        buses.forEach(bus -> {
            int startTime = busStartTime.get(bus.getId());
            bus.setBusTime(startTime);
        });
    }

    public void prepareIdToBusMap() {

        idToBusMap = new HashMap<>();
        buses.forEach(bus -> idToBusMap.put(bus.getId(), bus));
    }

    public List<Bus> getAllBuses(){
        return buses;
    }

    public Event createNextMoveBusEvent(Bus bus){

        return bus.createNextMoveEvent();
    }
}
