package graduation.y2020m10;

import java.time.LocalDate;

public class Serie {

    private final LocalDate broadcast;
    private final String title;
    private final int season;
    private final int episode;
    private final int duration;
    private final boolean saw;

    public Serie(LocalDate broadcast, String title, int season, int episode, int duration, boolean saw) {
        this.broadcast = broadcast;
        this.title = title;
        this.season = season;
        this.episode = episode;
        this.duration = duration;
        this.saw = saw;
    }

    public LocalDate getBroadcast() {
        return broadcast;
    }

    public String getTitle() {
        return title;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isSaw() {
        return saw;
    }
}
