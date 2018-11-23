package groots.sad.project.MassTransitSystem.entity;

public class Event {

    private int time;
    private String type;
    private String busId;

    public Event(int time, String type, String busId) {
        this.time = time;
        this.type = type;
        this.busId = busId;
    }

    public int getTime() {
        return time;
    }

    public String getBusId() {
        return busId;
    }

    public String getType() {
        return type;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
