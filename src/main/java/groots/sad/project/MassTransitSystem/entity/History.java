package groots.sad.project.MassTransitSystem.entity;

/**
 * Stores the information of the move bus events which are executed
 */
public class History {

    private String busId;
    private int busTime;
    private int busRiders;
    private String busStopId;
    private int stopPassengers;
    private int busAt;
    private int logicalTime;

    public History(String busId,int busTime,int busRiders,String busStopId,int stopPassengers, int busAt,int logicalTime) {
        this.busId = busId;
        this.busTime = busTime;
        this.busRiders = busRiders;
        this.busStopId = busStopId;
        this.stopPassengers = stopPassengers;
        this.busAt = busAt;
        this.logicalTime = logicalTime;
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
    public int getBusAt() {
        return busAt;
    }

    public int getLogicalTime() {
        return logicalTime;
    }
}
