package groots.sad.project.MassTransitSystem;

import groots.sad.project.MassTransitSystem.entity.*;
import groots.sad.project.MassTransitSystem.manager.BusManager;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class EventSimulator {


    private static BusManager busManager = BusManager.getInstance();
    private static List<BusStop> busStops = new ArrayList<>();
    private static List<Route> routes = new ArrayList<>();
    private static List<Event> eventQueue = new ArrayList<>();
    private static EventSimulator eventSimulator = new EventSimulator();
    private static int logicalTime;

    private static final String MOVE_BUS = "move_bus";

    private EventSimulator() {

        busManager = BusManager.getInstance();
        busStops = new ArrayList<>();
        routes = new ArrayList<>();
        eventQueue = new ArrayList<>();
        logicalTime = 0;
    }

    public static EventSimulator getInstance(){
        return eventSimulator;
    }

    private static void readData(String fileName) {

        final String DELIMITER = ",";
        try {
            Scanner takeCommand = new Scanner(new File(fileName));
            String[] tokens;
            do {
                String userCommandLine = takeCommand.nextLine();
                tokens = userCommandLine.split(DELIMITER);
                switch (tokens[0]) {
                    case "add_stop":
                        Location location = new Location(Double.valueOf(tokens[4]), Double.valueOf(tokens[5]));
                        busStops.add(new BusStop(tokens[1], tokens[2], Integer.valueOf(tokens[3]), location));
                        break;
                    case "add_route":
                        routes.add(new Route(tokens[1], Integer.valueOf(tokens[2]), tokens[3]));
                        break;
                    case "extend_route":
                        addStopInRoute(tokens[1], tokens[2], routes, busStops);
                        break;
                    case "add_bus":
                        addBus(tokens[1], tokens[2], Integer.valueOf(tokens[3]), Integer.valueOf(tokens[4]), Integer.valueOf(tokens[5]));
                        break;
                    case "add_event":
                        Event event = new Event(Integer.valueOf(tokens[1]), tokens[2], tokens[3]);
                        eventQueue.add(event);
                        break;
                    default:
                        System.out.println(" command not recognized");
                        break;
                }
            } while (takeCommand.hasNextLine());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        busManager.prepareIdToBusMap();
        busManager.prepareBusStartTimeData(eventQueue);
    }

    private static void addBus(String id, String routeId, int location, int passengerCapacity, int speed) {

        Route route = routes.stream()
                .filter(route1 -> route1.getId().equals(routeId))
                .findFirst()
                .get();
        Bus bus = new Bus(id, route, passengerCapacity, speed, location);
        bus.setBusAt(location);
        busManager.addBus(bus);
    }

    static void addStopInRoute(String routeId, String stopId, List<Route> routes, List<BusStop> busStops) {

        Route route = routes.stream()
                .filter(route1 -> route1.getId().equals(routeId))
                .findFirst()
                .get();
        route.extendRoute(busStops, stopId);
    }

    public BusManager getBusManager() {
        return busManager;
    }

    public List<BusStop> getBusStops() {
        return busStops;
    }

    public List<Event> prepareEvents() {

        return eventQueue.stream()
                .filter(event -> event.getTime() == logicalTime)
                .filter(event -> event.getType().equals(MOVE_BUS))
                .collect(Collectors.toList());
    }

    public void increaseLogicalTime(){

        logicalTime++;
    }

    public void updateEvents(Event eventCreated) {

        List<Event> eventsToReAdd = eventQueue.stream().filter(event -> event.getTime() > logicalTime).collect(Collectors.toList());
        eventQueue.clear();
        eventQueue.addAll(eventsToReAdd);
        eventQueue.add(eventCreated);
    }

    public static void replay(History history){

        busStops.forEach(stop -> {

            if (stop.getId().equals(history.getBusStopId())) {
                stop.setWaitingPassenger(history.getStopPassengers());
            }
        });

        for (int i = 0; i < eventQueue.size(); i++) {
            if(eventQueue.get(i).getBusId().equals(history.getBusStopId()) ) {
                eventQueue.get(i).setTime(history.getBusTime());
            }
        }
        eventQueue.forEach(event->{
            if(event.getBusId().equals(history.getBusId())){
                event.setTime(history.getBusTime());
            }
        });
        busManager.replay(history.getBusId(), history.getBusTime(), history.getBusRiders());
    }
}
