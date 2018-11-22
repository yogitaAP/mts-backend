package groots.sad.project.MassTransitSystem.service;

import groots.sad.project.MassTransitSystem.EventSimulator;
import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.entity.BusStop;
import groots.sad.project.MassTransitSystem.entity.Event;
import groots.sad.project.MassTransitSystem.manager.BusManager;
import groots.sad.project.MassTransitSystem.manager.PassengerManager;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class BusManagementService {

    EventSimulator eventSimulator;
    BusManager busManager;
    LinkedList<Bus> busesToBeProcessed;

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
        PassengerManager.managePassengersArrivingOnStop(busStop);
        PassengerManager.updatePassengersLeaveBus(bus,busStop);
        PassengerManager.boardPassengersOnBus(bus,busStop);
        PassengerManager.managePassengersDepartStop(busStop);
    }

    private void processBusStateChangeEvents(Bus bus) {

        // need to discuss with you guys
    }

    public List<Bus> getAllBuses() {
        return busManager.getAllBuses();
    }
}
