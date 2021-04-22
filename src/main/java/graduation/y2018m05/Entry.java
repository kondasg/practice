package graduation.y2018m05;

public class Entry {

    private final int hour;
    private final int min;
    private final int actorId;
    private final String direction;

    public Entry(int hour, int min, int actorId, String direction) {
        this.hour = hour;
        this.min = min;
        this.actorId = actorId;
        this.direction = direction;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public int getActorId() {
        return actorId;
    }

    public String getDirection() {
        return direction;
    }
}
