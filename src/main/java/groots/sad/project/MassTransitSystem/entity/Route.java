package groots.sad.project.MassTransitSystem.entity;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private String id;
    private int number;
    private String name;
    private List<BusStop> stops=new ArrayList<BusStop>();

    public Route(String id, int number, String name) {
        this.id = id;
        this.number = number;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    private void addBusStop(BusStop busStop){
        stops.add(busStop);
    }

    public List<BusStop> getStops(){
        return stops;
    }

    BusStop getCurrentStop(int busAt){
        return stops.get(busAt);
    }

    BusStop getNextStop(int busAt) {
        if (busAt + 1 == stops.size()) {
            return getStops().get(0);
        } else {
            return stops.get(busAt + 1);
        }
    }

    public void modifyStopSequence(){

    }

    public void extendRoute(List<BusStop> busStops,String stopId){

        BusStop busStop = busStops.stream().filter(busStop1 -> busStop1.getId().equals(stopId)).findFirst().get();
        addBusStop(busStop);
    }
}
