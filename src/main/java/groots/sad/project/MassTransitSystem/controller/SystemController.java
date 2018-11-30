package groots.sad.project.MassTransitSystem.controller;

import groots.sad.project.MassTransitSystem.entity.Route;
import groots.sad.project.MassTransitSystem.service.SystemConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("mts/system")
public class SystemController {

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @GetMapping("/refresh")
    public void refresh(){

        systemConfigurationService.refresh();
    }

    @GetMapping("/logicaltime")
    public Map<String,String> getLogicalTime(){

        return systemConfigurationService.getSystemLogicalTime();
    }

    @GetMapping("/routes")
    public List<Route> getAllRoutes(){

        return systemConfigurationService.getAllRoutes();
    }
}
