package groots.sad.project.MassTransitSystem.controller;

import groots.sad.project.MassTransitSystem.EventSimulator;
import groots.sad.project.MassTransitSystem.entity.BusStop;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("mts/bus")
public class BusStopController {


    @GetMapping("/stops")
    public List<BusStop> getAllBusStops(){

        EventSimulator eventSimulator = EventSimulator.getInstance();
        return eventSimulator.getBusStops();
    }
}
