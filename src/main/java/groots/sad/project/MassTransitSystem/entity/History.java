package groots.sad.project.MassTransitSystem.entity;

public class History {

    private String busId;
    private int busTime;
    private int busRiders;
    private String busStopId;
    private int stopPassengers;

    public History(String busId,int busTime,int busRiders,String busStopId,int stopPassengers) {
        this.busId = busId;
        this.busTime = busTime;
        this.busRiders = busRiders;
        this.busStopId = busStopId;
        this.stopPassengers = stopPassengers;
    }

    public String getBusId() {
        return busId;
    }
    public int getBusTime() {
        return busTime;
    }
    public int getBusRiders() {
        return busRiders;
    }
    public String getBusStopId() {
        return busStopId;
    }
    public int getStopPassengers() {
        return stopPassengers;
    }

}
