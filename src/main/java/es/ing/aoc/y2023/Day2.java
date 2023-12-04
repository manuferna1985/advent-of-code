package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day2 extends Day {

  private static final Integer MAX_RED_CUBES = 12;
  private static final Integer MAX_GREEN_CUBES = 13;
  private static final Integer MAX_BLUE_CUBES = 14;

  @Override
  protected String part1(String fileContents) throws Exception {
    return String.valueOf(buildGamesFromInput(fileContents).entrySet().stream()
        .filter(entry -> entry.getValue().stream().allMatch(this::matchIsWithinLimits))
        .map(Map.Entry::getKey)
        .mapToInt(Integer::valueOf)
        .sum());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return String.valueOf(buildGamesFromInput(fileContents).values().stream()
        .map(this::reduceMatches)
        .map(this::getMatchPower)
        .mapToLong(Long::valueOf)
        .sum());
  }

  private static Map<Integer, List<Triple<Integer, Integer, Integer>>> buildGamesFromInput(String fileContents) {
    String[] lines = fileContents.split(System.lineSeparator());

    Map<Integer, List<Triple<Integer, Integer, Integer>>> games = new HashMap<>();

    for (String game : lines) {
      int gameIndex = Integer.parseInt(game.substring(5, game.indexOf(":")));

      String[] matches = game.substring(game.indexOf(":") + 1).split(";");

      List<Triple<Integer, Integer, Integer>> matchesList = new ArrayList<>();

      for (String m : matches) {
        String[] colors = m.trim().split(",");
        int r = 0, g = 0, b = 0;

        for (String c : colors) {
          String[] parts = c.trim().split(" ");
          switch (parts[1]) {
            case "red" -> r = Integer.parseInt(parts[0]);
            case "green" -> g = Integer.parseInt(parts[0]);
            case "blue" -> b = Integer.parseInt(parts[0]);
          }
        }

        matchesList.add(Triple.of(r, g, b));
      }

      games.put(gameIndex, matchesList);
    }
    return games;
  }

  private boolean matchIsWithinLimits(Triple<Integer, Integer, Integer> match) {
    return match.getLeft() <= MAX_RED_CUBES &&
        match.getMiddle() <= MAX_GREEN_CUBES &&
        match.getRight() <= MAX_BLUE_CUBES;
  }

  private Triple<Integer, Integer, Integer> reduceMatches(List<Triple<Integer, Integer, Integer>> matches) {
    return matches.stream().reduce(Triple.of(0, 0, 0), (m1, m2) -> Triple.of(
        Math.max(m1.getLeft(), m2.getLeft()),
        Math.max(m1.getMiddle(), m2.getMiddle()),
        Math.max(m1.getRight(), m2.getRight())));
  }

  private Long getMatchPower(Triple<Integer, Integer, Integer> match) {
    return match.getLeft().longValue() * match.getMiddle().longValue() * match.getRight().longValue();
  }

  public static void main(String[] args) {
    Day.run(Day2::new, "2023/D2_small.txt", "2023/D2_full.txt");
  }
}
