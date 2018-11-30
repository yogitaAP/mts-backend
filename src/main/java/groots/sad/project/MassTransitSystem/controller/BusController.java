package groots.sad.project.MassTransitSystem.controller;

import groots.sad.project.MassTransitSystem.entity.Bus;
import groots.sad.project.MassTransitSystem.service.BusManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("mts/bus")
public class BusController {

    @Autowired
    private BusManagementService busManagementService;

    @GetMapping("/move")
    public void moveBus(){

        busManagementService.moveBus();
    }

    @GetMapping("/replay")
    public void replay(){

        busManagementService.replay();
    }

    @GetMapping("/list")
    public List<Bus> getAllBuses(){

        return busManagementService.getAllBuses();
    }

    @GetMapping("/displayinfo")
    public List<Map<String,String>> getBusDisplayInfo(){

        return busManagementService.getDisplayInfo();
    }

    @PostMapping("/updateinfo")
    public void updateBusInfo(@RequestBody Map<String, String> busInfo){

        busManagementService.updateBusInfo(busInfo);
    }
}
