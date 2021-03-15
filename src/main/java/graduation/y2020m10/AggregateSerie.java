package graduation.y2020m10;

public class AggregateSerie {

    private final String title;
    private final int duration;
    private final int episode;

    public AggregateSerie(String title, int duration, int episode) {
        this.title = title;
        this.duration = duration;
        this.episode = episode;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public int getEpisode() {
        return episode;
    }

    public String printAggregateSerie() {
        return title + " " + duration + " " + episode;
    }

}
