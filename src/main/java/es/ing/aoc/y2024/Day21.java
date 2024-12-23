package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day21 extends Day {

  private static final Map<Pair<String, String>, Combination> CACHE = new HashMap<>();

  private static final String HOLE = "@";
  private static final String START = "A";

  private static final String[][] NUMERIC_KEYPAD = {
      {"7", "8", "9"},
      {"4", "5", "6"},
      {"1", "2", "3"},
      {"@", "0", "A"},
  };

  private static final Map<String, Point> NUMERICAL_POINTS = buildPoints(NUMERIC_KEYPAD);

  private static final String[][] DIRECTIONAL_KEYPAD = {
      {"@", "^", "A"},
      {"<", "v", ">"}
  };

  private static final Map<String, Point> DIRECTIONAL_POINTS = buildPoints(DIRECTIONAL_KEYPAD);

  private static Map<String, Point> buildPoints(String[][] keyPad) {
    Map<String, Point> pointsMap = new HashMap<>();
    for (int x = 0; x < keyPad.length; x++) {
      for (int y = 0; y < keyPad[x].length; y++) {
        pointsMap.put(keyPad[x][y], Point.of(x, y));
      }
    }
    return pointsMap;
  }

  record Combination(List<String> numbers) {

    public Combination(String line) {
      this(Arrays.stream(line.split("")).collect(Collectors.toList()));
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    return String.valueOf(algorithm(fileContents, 2));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return String.valueOf(algorithm(fileContents, 25));
  }

  private long algorithm(String fileContents, int directionalRobotsNumber) {

    return Arrays.stream(fileContents.split(System.lineSeparator()))
        .map(Combination::new)
        .map(c0 -> {
          int numericPart = Integer.parseInt(StringUtils.join(c0.numbers, "").substring(0, 3));
          Combination c1 = this.getComplexity(c0, NUMERICAL_POINTS, true);

          // The key here is to split the next iteration keys combination into sequences, splitting the chain by "A's".
          // For instance: "<v<A.>>^A.vA.^A.<v<A" --> [("<v<A":2),(">>^A":1),("vA":1),("^A":1)]
          // With this, we don't really need to keep the whole combination, which will grow exponentially, but
          // just these sequences and the number of times each one is repeated, cause they're independent between them,
          // as they always end in the "A" button.
          Map<String, Long> sequences = buildSequencesFromComb(c1);

          for (int i = 0; i < directionalRobotsNumber; i++) {
            Map<String, Long> nextSequences = new HashMap<>();

            for (var entry : sequences.entrySet()) {
              c1 = this.getComplexity(new Combination(entry.getKey()), DIRECTIONAL_POINTS, true);
              for (var entry2 : buildSequencesFromComb(c1).entrySet()) {
                if (!nextSequences.containsKey(entry2.getKey())) {
                  nextSequences.put(entry2.getKey(), entry.getValue() * entry2.getValue());
                } else {
                  nextSequences.put(entry2.getKey(), nextSequences.get(entry2.getKey()) + (entry.getValue() * entry2.getValue()));
                }
              }
            }
            sequences = nextSequences;
          }

          long totalSeqLength = sequences.entrySet()
              .stream().mapToLong(entry -> entry.getKey().length() * entry.getValue()).sum();

          System.out.printf("[%s] = %d%n", c0, totalSeqLength);

          return totalSeqLength * numericPart;
        })
        .mapToLong(Long::longValue)
        .sum();
  }

  private Map<String, Long> buildSequencesFromComb(Combination c) {
    Map<String, Long> seqsMap = new HashMap<>();
    String[] tokens = StringUtils.join(c.numbers, "").replace("A", "A|").split("\\|");
    for (String t : tokens) {
      seqsMap.put(t, seqsMap.computeIfAbsent(t, k -> 0L) + 1L);
    }
    return seqsMap;
  }

  private Combination getComplexity(Combination comb, Map<String, Point> pointsMap, boolean goDeep) {
    Combination next = new Combination(new ArrayList<>());
    String current = START;
    for (int i = 0; i < comb.numbers.size(); i++) {
      next.numbers.addAll(getCachedOptimumPathFor(current, comb.numbers.get(i), pointsMap, goDeep).numbers);
      current = comb.numbers.get(i);
    }
    return next;
  }

  private Combination getCachedOptimumPathFor(String aChar, String bChar, Map<String, Point> pointsMap, boolean goDeep) {
    if (goDeep) {
      return CACHE.computeIfAbsent(Pair.of(aChar, bChar),
          key -> getOptimumPathFor(aChar, bChar, pointsMap, true));
    } else {
      return getOptimumPathFor(aChar, bChar, pointsMap, false);
    }
  }

  private Combination getOptimumPathFor(String aChar, String bChar, Map<String, Point> pointsMap, boolean goDeep) {

    Point a = pointsMap.get(aChar);
    Point b = pointsMap.get(bChar);
    Point hole = pointsMap.get(HOLE);

    List<Combination> combs;
    if (Objects.equals(a, b)) {
      combs = List.of(new Combination(new ArrayList<>()));
    } else {
      List<String> movs = new ArrayList<>();
      IntStream.range(b.x, a.x).forEach(n -> movs.add("^"));
      IntStream.range(a.x, b.x).forEach(n -> movs.add("v"));
      IntStream.range(b.y, a.y).forEach(n -> movs.add("<"));
      IntStream.range(a.y, b.y).forEach(n -> movs.add(">"));

      combs = Generator.permutation(movs).simple().stream()
          .map(Combination::new)
          .filter(c -> isAllowed(c, a, hole))
          .filter(this::hasOneTurnAtMost)
          .toList();
    }

    combs.forEach(this::addCombinationEnd);

    if (combs.size() > 1 && goDeep) {
      Combination nextA = combs.get(0), nextB = combs.get(1);
      while (nextA.numbers.size()==nextB.numbers.size()) {
        nextA = getComplexity(nextA, DIRECTIONAL_POINTS, false);
        nextB = getComplexity(nextB, DIRECTIONAL_POINTS, false);
      }
      return nextA.numbers.size() > nextB.numbers.size() ? combs.get(1):combs.get(0);
    }
    return combs.getFirst();
  }

  private Combination addCombinationEnd(Combination combination) {
    combination.numbers.add(START);
    return combination;
  }

  private boolean hasOneTurnAtMost(Combination c) {
    int turns = 0;
    for (int i = 0; i < c.numbers.size() - 1; i++) {
      turns += c.numbers.get(i).equals(c.numbers.get(i + 1)) ? 0:1;
    }
    return turns <= 1;
  }

  private boolean isAllowed(Combination c, Point start, Point hole) {

    List<Point> path = new ArrayList<>();

    Point current = start;
    for (String mov : c.numbers) {
      current = switch (mov) {
        case ">" -> Point.of(current.x, current.y + 1);
        case "<" -> Point.of(current.x, current.y - 1);
        case "^" -> Point.of(current.x - 1, current.y);
        case "v" -> Point.of(current.x + 1, current.y);
        default -> throw new RuntimeException("Invalid direction in path!");
      };

      path.add(current);
    }

    return !path.contains(hole);
  }

  public static void main(String[] args) {
    Day.run(Day21::new, "2024/D21_small.txt", "2024/D21_full.txt");
  }
}
