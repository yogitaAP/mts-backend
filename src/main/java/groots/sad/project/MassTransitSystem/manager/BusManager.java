package groots.sad.project.MassTransitSystem.manager;

import groots.sad.project.MassTransitSystem.EventSimulator;
import groots.sad.project.MassTransitSystem.comparator.LowRunningTimeComparator;
import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.entity.Event;
import groots.sad.project.MassTransitSystem.entity.Route;

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

    public void replay(String busId,int busTime, int busRiders)
    {
        for (int i = 0; i < buses.size(); i++) {
            if(buses.get(i).getId().equals(busId) )
            {
                buses.get(i).setBusTime(busTime);
                buses.get(i).setRiders(busRiders);
            }
        }
    }

    public List<Bus> getAllBuses(){
        return buses;
    }

    public Event createNextMoveBusEvent(Bus bus){

        return bus.createNextMoveEvent();
    }

    public void updateBusInfo(Map<String,String> busInfo){

        String busId = busInfo.get("busId");
        buses.forEach(bus->{
            if(bus.getId().equals(busId)){
                String passenger = busInfo.getOrDefault("passenger",null);
                String speed = busInfo.getOrDefault("speed",null);
                String routeId = busInfo.getOrDefault("routeId",null);
                String nextStop = busInfo.getOrDefault("nextStop",null);
                updateInfo(bus,passenger,speed,routeId,nextStop);
            }
        });

    }

    private void updateInfo(Bus bus, String passenger, String speed, String routeId, String nextStop) {

        EventSimulator eventSimulator = EventSimulator.getInstance();
        if(passenger!=null){
            bus.setPassengerCapacity(Integer.valueOf(passenger));
        }
        if(speed!=null){
            bus.setAverageSpeed(Integer.valueOf(speed));
        }
        if(routeId!=null){
            List<Route> routes = eventSimulator.getRoutes();
            Route route = routes.stream().filter(route1 -> route1.getId().equals(routeId)).findFirst().get();
            bus.changeRoute(route,Integer.valueOf(nextStop));
        }
    }
}
