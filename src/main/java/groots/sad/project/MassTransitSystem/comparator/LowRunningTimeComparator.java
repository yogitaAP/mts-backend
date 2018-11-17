package groots.sad.project.MassTransitSystem.comparator;

import groots.sad.project.MassTransitSystem.entity.Bus;

import java.util.Comparator;

public class LowRunningTimeComparator implements Comparator<Bus> {


    @Override
    public int compare(Bus bus1, Bus bus2) {
        return bus1.computeBusTravelTime() - bus2.computeBusTravelTime();
    }
}
