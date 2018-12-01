package groots.sad.project.MassTransitSystem.manager;

import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.entity.BusStop;
import groots.sad.project.MassTransitSystem.entity.History;
import groots.sad.project.MassTransitSystem.EventSimulator;
import org.springframework.util.CollectionUtils;


import java.util.Stack;

/**
 * The responsibility of this is to manage the historical data to be used by replay feature.
 */
public class EventHistoryManager {

    private Stack<History> history;
    static EventHistoryManager eventHistoryManager = new EventHistoryManager();

    private EventHistoryManager() {
        history = new Stack<>();
    }

    public static EventHistoryManager getInstance() {
        return eventHistoryManager;
    }

    public void replay() {

        if (!CollectionUtils.isEmpty(history)) {
            EventSimulator eventSimulator = EventSimulator.getInstance();
            eventSimulator.replay(history.pop());
        }
    }

    public void addEventToHistory(Bus bus, BusStop stop) {

        String busId = bus.getId();
        int busTime = bus.getBusTime();
        int busRiders = bus.getRiders();
        String stopId = stop.getId();
        int stopPassengers = stop.getWaitingPassenger();
        int busAt = bus.getBusAt();
        History newHistory = new History(busId, busTime, busRiders, stopId, stopPassengers, busAt);

        if(history.size() >3)
        {
            Stack<History> copyHistory = new Stack<>();
            copyHistory.push(history.pop());
            copyHistory.push(history.pop());
            history.pop();
            history.push(copyHistory.pop());
            history.push(copyHistory.pop());
            history.push(newHistory);
        }
        else
        {
            history.push(newHistory);
        }
    }

    public void refresh(){
        history = new Stack<>();
    }
}