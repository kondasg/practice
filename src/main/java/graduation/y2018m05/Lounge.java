package graduation.y2018m05;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Lounge {

    private final static int END_OF_PERIOD_IN_MIN = 15 * 60;
    private final List<Entry> entries = new ArrayList<>();

    public List<Entry> getEntries() {
        return new ArrayList<>(entries);
    }

    public void readLines(BufferedReader reader) {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String[] splittedLine = line.split(" ");
                entries.add(new Entry(
                        Integer.parseInt(splittedLine[0]),
                        Integer.parseInt(splittedLine[1]),
                        Integer.parseInt(splittedLine[2]),
                        splittedLine[3]
                ));
            }
        } catch (IOException ioe) {
            System.out.println("Can't read line!\n" + ioe);
        }
    }

    public int firstEntry() {
        for (Entry entry : entries) {
            if ("be".equals(entry.getDirection())) {
                return entry.getActorId();
            }
        }
        return 0;
    }

    public int lastExit() {
        List<Entry> reverseEntries = new ArrayList<>(entries);
        reverseListByTime(reverseEntries);
        for (Entry entry : reverseEntries) {
            if ("ki".equals(entry.getDirection())) {
                return entry.getActorId();
            }
        }
        return 0;
    }

    private void reverseListByTime(List<Entry> reverseEntries) {
        reverseEntries.sort((o1, o2) -> {
            int hour = o2.getHour() - o1.getHour();
            int min = o2.getMin() - o1.getMin();
            return (hour == 0) ? min : hour;
        });
    }

    public void transitByActor(BufferedWriter writer) {
        try {
            Map<Integer, Integer> actorsTransit = fillTransit();
            for (Map.Entry<Integer, Integer> entry : actorsTransit.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException ioe) {
            System.out.println("Can't write file!" + ioe);
        }
    }

    private Map<Integer, Integer> fillTransit() {
        Map<Integer, Integer> actorsTransit = new TreeMap<>();
        for (Entry entry : entries) {
            if (actorsTransit.containsKey(entry.getActorId())) {
                actorsTransit.put(entry.getActorId(), actorsTransit.get(entry.getActorId()) + 1);
            } else {
                actorsTransit.put(entry.getActorId(), 1);
            }
        }
        return actorsTransit;
    }

    public List<Integer> actorsOfEndTimeInLounge() {
        Map<Integer, String> actors = new HashMap<>();
        for (Entry entry : entries) {
            if ("be".equals(entry.getDirection())) {
                actors.put(entry.getActorId(), "be");
            } else {
                actors.put(entry.getActorId(), "ki");
            }
        }
        return selectActorsInLounge(actors);
    }

    private List<Integer> selectActorsInLounge(Map<Integer, String> actors) {
        List<Integer> res = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : actors.entrySet()) {
            if ("be".equals(entry.getValue())) {
                res.add(entry.getKey());
            }
        }
        return res;
    }

    public String mostActorsInLounge() {
        Map<String, Integer> res = new HashMap<>();
        fillTimeTable(res);
        Map.Entry<String, Integer> maxActors = null;
        for (Map.Entry<String, Integer> entry : res.entrySet()) {
            if (maxActors == null || entry.getValue().compareTo(maxActors.getValue()) > 0) {
                maxActors = entry;
            }
        }
        return maxActors.getKey();
    }

    private void fillTimeTable(Map<String, Integer> res) {
        int actors = 0;
        for (Entry entry : entries) {
            String time = entry.getHour() + ":" + entry.getMin();
            if (("be".equals(entry.getDirection()))) {
                actors++;
            } else {
                actors--;
            }
            res.put(time, actors);
        }
    }

    public List<String> actorInLoungeById(int actorId) {
        List<String> res = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (Entry entry : entries) {
            if (entry.getActorId() == actorId) {
                if (("be".equals(entry.getDirection()))) {
                    line.append(entry.getHour()).append(":").append(entry.getMin()).append("-");
                }
                if (("ki".equals(entry.getDirection()))) {
                    line.append(entry.getHour()).append(":").append(entry.getMin());
                    res.add(line.toString());
                    line = new StringBuilder();
                }
            }
        }
        if (!"".equals(line.toString())) {
            res.add(line.toString());
        }
        return res;
    }

    public int timeInLoungeById(int actorId) {
        int mins = 0;
        for (String line : actorInLoungeById(actorId)) {
            String[] splittedLine = line.split("-");
            String[] inHourMin = splittedLine[0].split(":");
            int inMins = Integer.parseInt(inHourMin[0]) * 60 + Integer.parseInt(inHourMin[1]);
            int outMins;
            if (splittedLine.length == 2) {
                String[] outHourMin = splittedLine[1].split(":");
                outMins = Integer.parseInt(outHourMin[0]) * 60 + Integer.parseInt(outHourMin[1]);
            } else {
                outMins = END_OF_PERIOD_IN_MIN;
            }
            mins += outMins - inMins;
        }
        return mins;
    }

    public String inLoungeById(int actorId) {
        return actorsOfEndTimeInLounge().contains(actorId) ? "a társalgóban volt" : "nem volt a társalgóban";
    }

    public static void main(String[] args) {
        Lounge lounge = new Lounge();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Lounge.class.getResourceAsStream("ajto.txt")))) {
            lounge.readLines(reader);
        } catch (IOException ioe) {
            System.out.println("Can't read file!\n" + ioe);
        }

        System.out.println("1. feladat");
        System.out.println("Beolvasott sorok száma: " + lounge.getEntries().size());
        System.out.println();

        System.out.println("2. feladat");
        System.out.println("Az első belépő: " + lounge.firstEntry());
        System.out.println("Az utolsó kilépő: " + lounge.lastExit());
        System.out.println();

        System.out.println("3. feladat");
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(" athaladas.txt"))) {
            lounge.transitByActor(writer);
            System.out.println("Fileba írás kész!");
        } catch (IOException ioe) {
            System.out.println("Can't write file!\n" + ioe);
        }
        System.out.println();

        System.out.println("4. feladat");
        System.out.print("A végén a társalgóban voltak:");
        for (int i : lounge.actorsOfEndTimeInLounge()) {
            System.out.print(" " + i);
        }
        System.out.println();
        System.out.println();

        System.out.println("5. feladat");
        System.out.println("Például " + lounge.mostActorsInLounge() + "-kor voltak a legtöbben a társalgóban.");
        System.out.println();

        System.out.println("6. feladat");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Adja meg a személy azonosítóját! ");
        int actorId = scanner.nextInt();
        System.out.println();

        System.out.println("7. feladat");
        for (String s : lounge.actorInLoungeById(actorId)) {
            System.out.println(s);
        }
        System.out.println();

        System.out.println("8. feladat");
        System.out.print("A(z) " + actorId + ". személy összesen " + lounge.timeInLoungeById(actorId) + " percet volt bent, ");
        System.out.println("a megfigyelés végén " + lounge.inLoungeById(actorId) + ".");
    }
}
