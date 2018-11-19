package groots.sad.project.MassTransitSystem.controller;

import groots.sad.project.MassTransitSystem.service.SystemEfficiencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mts/systemefficiency")
public class SystemEfficiencyController {

    @Autowired
    SystemEfficiencyService systemEfficiencyService;

    @GetMapping("/compute")
    public double computeSystemEfficiency() {
        return systemEfficiencyService.computeSystemEfficiency();
    }


}
