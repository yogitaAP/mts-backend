package groots.sad.project.MassTransitSystem.controller;

import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.service.BusManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("mts/bus")
public class BusController {

    @Autowired
    BusManagementService busManagementService;

    @PostMapping("/move")
    public void moveBus(){

        busManagementService.moveBus();
    }

    @GetMapping("/list")
    public List<Bus> getAllBuses(){

        return busManagementService.getAllBuses();
    }
}
