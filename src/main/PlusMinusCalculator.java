package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PlusMinusCalculator {


  public static void main(String[] args) throws FileNotFoundException {
    // need to write to list and sort instead of single pass because prompt says to
    List<PlayByPlay> playByPlayLines = loadSortedPlayByPlay();

    Map<String, Set<String>> startOfPeriodPlayers = loadStartOfPeriodMap();

    // stupid ass map (gameid_period_teamid -> set of 5 playerids)
    startOfPeriodPlayers.keySet().forEach(a -> System.out.println(a + "\t" + startOfPeriodPlayers.get(a).toString()));

    // initial way of doing this shit, sure there are a plenty better ways
    // iterate through playByPlayLines - new Game class for each new game id
    //  any time you see new period - (12 0) -> update list of current players using startOfPeriodPlayersMap
    //  anytime people score, update the plus minus (teamA & teamB) of current players (currentPlayersA and currentPlayersB)
    //  anytime there is a sub or new quarter, update list

  }

  private static Map<String, Set<String>> loadStartOfPeriodMap() throws FileNotFoundException {
    HashMap<String, Set<String>> startOfPeriodMap = new HashMap<>();
    String csvFile = "basketball_analytics/lineup_data.txt";
    new BufferedReader(new FileReader(csvFile))
        .lines()
        .skip(1) //Skips the first n lines, in this case 1
        .forEach(line -> {
          String[] fields = line.split("\t");
          String gameIdTeamIdPeriod = fields[0] + "_" + fields[3] + "_" + fields[1];
          if (!startOfPeriodMap.containsKey(gameIdTeamIdPeriod))
            startOfPeriodMap.put(gameIdTeamIdPeriod, new HashSet<>());

          Set<String> playerIds = startOfPeriodMap.get(gameIdTeamIdPeriod);
          playerIds.add(fields[2]);
          startOfPeriodMap.put(
              gameIdTeamIdPeriod,
              playerIds
          );

        }
        );
     return startOfPeriodMap;

  }

  private static List<PlayByPlay> loadSortedPlayByPlay() throws FileNotFoundException {
    String csvFile = "basketball_analytics/play_by_play.txt";

    Comparator <PlayByPlay> pcTimeReverse = Comparator.comparing(PlayByPlay::getPcTime).reversed();

    Comparator<PlayByPlay> gameComparator = Comparator
        .comparing(PlayByPlay::getGameId)
        .thenComparing(PlayByPlay::getPeriod)
        .thenComparing(pcTimeReverse)
        .thenComparing(PlayByPlay::getWcTime)
        .thenComparing(PlayByPlay::getEventNumber)
        ;

    return new BufferedReader(new FileReader(csvFile))
        .lines()
        .skip(1) //Skips the first n lines, in this case 1
        .map(line -> {
          String[] fields = line.split("\t");
          return PlayByPlayBuilder.aPlayByPlayBuilder()
              .withGameId(fields[0])
              .withEventNumber(Integer.parseInt(fields[1]))
              .withEventMessageType(Integer.parseInt(fields[2]))
              .withPeriod(Integer.parseInt(fields[3]))
              .withWcTime(Integer.parseInt(fields[4]))
              .withPcTime(Integer.parseInt(fields[5]))
              .withActionType(Integer.parseInt(fields[6]))
              .withOption1(Integer.parseInt(fields[7]))
              .withOption2(Integer.parseInt(fields[8]))
              .withOption3(Integer.parseInt(fields[9]))
              .withTeamId(fields[10])
              .withPerson1(fields[11])
              .withPerson2(fields[12])
              .withTeamIdType(Integer.parseInt(fields[13]))
              .build();
        })
        .sorted(gameComparator)
        .collect(Collectors.toList());
  }



}
