package groots.sad.project.MassTransitSystem.manager;

import groots.sad.project.MassTransitSystem.entity.History;
import groots.sad.project.MassTransitSystem.EventSimulator;

public class EventHistoryManager {

    public static void replay(History history){
        EventSimulator.replay(history);
    }
}