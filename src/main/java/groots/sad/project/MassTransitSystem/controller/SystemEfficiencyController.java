package groots.sad.project.MassTransitSystem.controller;

import groots.sad.project.MassTransitSystem.service.SystemEfficiencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mts/systemefficiency")
public class SystemEfficiencyController {

    @Autowired
    SystemEfficiencyService systemEfficiencyService;

    @GetMapping("/compute")
    public double computeSystemEfficiency() {
        return systemEfficiencyService.computeSystemEfficiency();
    }


    @PostMapping("/constants")
    public void updateKConstants(@RequestBody Map<String, String> kConstants) {

        double kSpeed = Double.parseDouble(kConstants.getOrDefault("kSpeed", "0.5"));
        double kCapacity = Double.parseDouble(kConstants.getOrDefault("kCapacity", "0.5"));
        double kWaiting = Double.parseDouble(kConstants.getOrDefault("kWaiting", "0.5"));
        double kBuses = Double.parseDouble(kConstants.getOrDefault("kBuses", "0.5"));
        double kCombined = Double.parseDouble(kConstants.getOrDefault("kCombined", "0.5"));
        systemEfficiencyService.setKConstants(kSpeed, kCapacity, kWaiting, kBuses, kCombined);
    }

}
