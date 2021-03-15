package graduation.y2020m10;

public class DayHourMin {

    private final int day;
    private final int hour;
    private final int min;

    public DayHourMin(int minutes) {
        this.day = minutes / (24 * 60);
        this.hour = (minutes / 60) - day * 24;
        this.min = minutes - (day * 24 * 60) - (hour * 60);
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }
}
