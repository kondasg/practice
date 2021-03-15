package graduation.y2020m10;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

public class Series {

    private static final String DATE_SEPARATOR = "\\.";
    private static final String SEASON_SEPARATOR = "x";
    private static final LocalDate NI_DATE =  LocalDate.of(2999, 1, 1);
    private final List<Serie> filmSeries = new ArrayList<>();

    public List<Serie> getFilmSeries() {
        return filmSeries;
    }

    public void readLines(BufferedReader reader) {
        String line;
        int row = 1;
        LocalDate broadcast = null;
        String title = "";
        int season = 0, episode = 0, duration = 0;
        boolean saw = false;
        try {
            while ((line = reader.readLine()) != null) {

                switch (row % 5) {
                    case 0:
                        saw = !"0".equals(line);
                        break;
                    case 1:
                        if (!"NI".equals(line)) {
                            String[] splittedLine1 = line.split(DATE_SEPARATOR);
                            broadcast = LocalDate.of(
                                    Integer.parseInt(splittedLine1[0]),
                                    Integer.parseInt(splittedLine1[1]),
                                    Integer.parseInt(splittedLine1[2]));
                        } else {
                            broadcast = NI_DATE;
                        }
                        break;
                    case 2:
                        title = line;
                        break;
                    case 3:
                        String[] splittedLine2 = line.split(SEASON_SEPARATOR);
                        season = Integer.parseInt(splittedLine2[0]);
                        episode = Integer.parseInt(splittedLine2[1]);
                        break;
                    case 4:
                        duration = Integer.parseInt(line);
                        break;
                }
                if (row % 5 == 0) {
                    filmSeries.add(new Serie(broadcast, title, season, episode, duration, saw));
                }
                row++;
            }
        } catch (IOException ioe) {
            System.out.println("Can't read line!\n" + ioe);
        }
    }

    public int broadcastTimeIsKnown() {
        int count = 0;
        for (Serie serie : filmSeries) {
            if (!serie.getBroadcast().equals(NI_DATE)) {
                count++;
            }
        }
        return count;
    }

    public double watchedEpisodes() {
        double saw = 0;
        for (Serie serie : filmSeries) {
            if (serie.isSaw()) {
                saw++;
            }
        }
        return new BigDecimal(saw / filmSeries.size() * 100)
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public int watchedEpisodesTime() {
        int duration = 0;
        for (Serie serie : filmSeries) {
            if (serie.isSaw()) {
                duration += serie.getDuration();
            }
        }
        return duration;
    }

    public boolean isValidDate(String date) {
        Pattern pattern = Pattern.compile("[0-9]{4}\\.[0-1][0-9]\\.[0-3][0-9]");
        return pattern.matcher(date).matches();
    }

    public List<Serie> nonWatchedEpisodesToDate(String date) {
        LocalDate formatDate = LocalDate.parse(date.replace(".", "-"));
        List<Serie> result = new ArrayList<>();
        for (Serie serie : filmSeries) {
            if (serie.getBroadcast().compareTo(formatDate) <= 0 && !serie.isSaw()) {
                result.add(serie);
            }
        }
        return result;
    }

    public String dayOfWeek(int year, int month, int day) {
        String[] days = {"v", "h", "k", "sze", "cs", "p", "szo"};
        int[] months = {0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4};
        if (month < 3) {
            year--;
        }
        return days[(year + year / 4 - year / 100 + year / 400 + months[month - 1] + day) % 7];
    }

    public boolean isValidDay(String day) {
        List<String> days = List.of("v", "h", "k", "sze", "cs", "p", "szo");
        return days.contains(day);
    }

    public Set<String> givenDailySeries(String day) {
        Set<String> result = new LinkedHashSet<>();
        for (Serie serie : filmSeries) {
            if (day.equals(
                    dayOfWeek(serie.getBroadcast().getYear(),
                            serie.getBroadcast().getMonthValue(),
                            serie.getBroadcast().getDayOfMonth()))) {
                result.add(serie.getTitle());
            }
        }
        return result;
    }

    public void writeFile(BufferedWriter writer) {
        try {
            for (AggregateSerie aggregateSerie : aggregateToSeries().values()) {
                writer.write(aggregateSerie.printAggregateSerie());
                writer.newLine();
            }
        } catch (IOException ioe) {
            System.out.println("Can't write file!" + ioe);
        }
    }

    public Map<String, AggregateSerie> aggregateToSeries() {
        Map<String, AggregateSerie> aggregateSeries = new LinkedHashMap<>();
        for (Serie serie : filmSeries) {
            aggregateSeries.put(serie.getTitle(), new AggregateSerie(serie.getTitle(), 0, 0));
        }
        for (Serie serie : filmSeries) {
            AggregateSerie agt = aggregateSeries.get(serie.getTitle());
            aggregateSeries.put(agt.getTitle(), new AggregateSerie(agt.getTitle(),
                    agt.getDuration() + serie.getDuration(),
                    agt.getEpisode() + 1));

        }
        return aggregateSeries;
    }

    public static void main(String[] args) {
        Series series = new Series();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Series.class.getResourceAsStream("lista.txt")))) {
            series.readLines(reader);
        } catch (IOException ioe) {
            System.out.println("Can't read file!\n" + ioe);
        }

        System.out.println("1. feladat");
        System.out.println("Beolvasott sorok száma: " + series.getFilmSeries().size());
        System.out.println();

        System.out.println("2. feladat");
        System.out.println("A listában " + series.broadcastTimeIsKnown() + " db vetítési dátummal rendelkező epizód van.");
        System.out.println();

        System.out.println("3. feladat");
        System.out.println("A listában lévő epizódok " + series.watchedEpisodes() + "%-át látta.");
        System.out.println();

        System.out.println("4. feladat");
        DayHourMin dhm = new DayHourMin(series.watchedEpisodesTime());
        System.out.println(
                "Sorozatnézéssel " + dhm.getDay()
                        + " napot " + dhm.getHour()
                        + " órát és " + dhm.getMin() + " percet töltött.");
        System.out.println();

        System.out.println("5. feladat");
        Scanner scanner = new Scanner(System.in);
        String inputDate;
        do {
            System.out.print("Adjon meg egy dátumot (formátum: éééé.hh.nn)! Dátum= ");
            inputDate = scanner.nextLine();
        } while (!series.isValidDate(inputDate));
        for (Serie serie : series.nonWatchedEpisodesToDate(inputDate)) {
            System.out.printf("%dx%02d\t%s\n", serie.getSeason(), serie.getEpisode(), serie.getTitle());
        }
        System.out.println();

        System.out.println("7. feladat");
        String inputDay;
        do {
            System.out.print("Adja meg a hét egy napját (például cs) Nap= ");
            inputDay = scanner.nextLine();
        } while (!series.isValidDay(inputDay));
        if (series.givenDailySeries(inputDay).size() == 0) {
            System.out.println("Az adott napon nem kerül adásba sorozat.");
        } else {
            for (String serie : series.givenDailySeries(inputDay)) {
                System.out.println(serie);
            }
        }
        System.out.println();

        System.out.println("8. feladat");
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("summa.txt"))) {
            series.writeFile(writer);
            System.out.println("Fileba írás kész!");
        } catch (IOException ioe) {
            System.out.println("Can't write file!\n" + ioe);
        }
    }

}
