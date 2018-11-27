package groots.sad.project.MassTransitSystem.controller;

import groots.sad.project.MassTransitSystem.EventSimulator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mts/system")
public class SystemController {

    @GetMapping("/refresh")
    public void refresh(){

        EventSimulator eventSimulator = EventSimulator.getInstance();
        eventSimulator.refresh();
    }
}
