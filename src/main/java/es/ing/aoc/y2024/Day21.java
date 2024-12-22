package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.StringUtils;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day21 extends Day {

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
    return String.valueOf(
        Arrays.stream(fileContents.split(System.lineSeparator()))
            .map(Combination::new)
            .map(c1 -> {
              int numericPart = Integer.parseInt(StringUtils.join(c1.numbers, "").substring(0, 3));
              List<Combination> c2List = this.getComplexity2(c1, NUMERICAL_POINTS, 0);
              for (Combination c2 : c2List) {
                List<Combination> c3List = this.getComplexity2(c2, DIRECTIONAL_POINTS, 0);
                for (Combination c3 : c3List) {
                  List<Combination> c4List = this.getComplexity2(c3, DIRECTIONAL_POINTS, 0);
                  for (Combination c4 : c4List) {
                    System.out.printf("[%s] = %d%n", c4.numbers, c4.numbers.size());
                  }
                }
              }

              return 0;
            })
            .mapToInt(Integer::intValue)
            .sum());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    return "";
  }

  private List<Combination> getComplexity2(Combination comb, Map<String, Point> pointsMap, int tabs) {

    //System.out.println(StringUtils.repeat("-", 150));

    System.out.printf("%s%n", comb.numbers);

    List<Combination> aliveCombinations = new ArrayList<>();
    aliveCombinations.add(new Combination(new ArrayList<>()));

    String current = START;
    for (int i = 0; i < comb.numbers.size(); i++) {
      List<Combination> combs = getOptimumPathFor(current, comb.numbers.get(i), pointsMap, tabs);

      List<Combination> nextCombs = new ArrayList<>();
      for (Combination alive : aliveCombinations) {
        for (Combination next : combs) {
          Combination newC = new Combination(new ArrayList<>());
          newC.numbers.addAll(alive.numbers);
          newC.numbers.addAll(next.numbers);
          nextCombs.add(newC);
        }
      }
      aliveCombinations = nextCombs;
      current = comb.numbers.get(i);
    }

    //System.out.println(StringUtils.repeat("-", 150));

    return aliveCombinations;
  }

  private List<Combination> getOptimumPathFor(String aChar, String bChar, Map<String, Point> pointsMap, int tabs) {

    //System.out.printf("%sCalculating optimum path from [%s] to [%s]%n", StringUtils.repeat(" ", tabs * 2), aChar, bChar);

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

    combs.forEach(this::addCombEnd);

    //System.out.printf("%s%s%n", StringUtils.repeat(" ", tabs * 2), combs);

    return combs;
  }

  private Combination addCombEnd(Combination combination) {
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

  private Integer getComplexity(Combination comb) {

    int numericPart = Integer.parseInt(StringUtils.join(comb.numbers, "").substring(0, 3));

    System.out.println(comb);
    List<Combination> c1 = getMinLengthCombs(getSeqForNumericKeyPad(comb));
    System.out.printf("[%d] -> %d%n", c1.size(), c1.get(0).numbers.size());
    //System.out.println(c1);

    for (int i = 0; i < 2; i++) {
      List<Combination> c2 = getMinLengthCombs(c1.stream().flatMap(c -> getSeqForDirectionalKeyPad(c).stream()).toList());
      System.out.printf("[%d] -> %d%n", c2.size(), c2.get(0).numbers.size());
      //System.out.println(c2);
      c1 = c2;
    }

    return c1.get(0).numbers.size() * numericPart;
  }

  private List<Combination> getMinLengthCombs(List<Combination> list) {
    int minLength = list.stream().map(c -> c.numbers.size()).mapToInt(Integer::intValue).min().orElse(0);
    return list.stream().filter(c -> c.numbers.size()==minLength).toList();
  }

  private List<Combination> getSeqForDirectionalKeyPad(Combination comb) {
    return getSeqForKeyPad(comb, START, DIRECTIONAL_KEYPAD, find(DIRECTIONAL_KEYPAD, HOLE));
  }

  private List<Combination> getSeqForNumericKeyPad(Combination comb) {
    return getSeqForKeyPad(comb, START, NUMERIC_KEYPAD, find(NUMERIC_KEYPAD, HOLE));
  }

  private List<Combination> getSeqForKeyPad(Combination comb, String current, String[][] keyPad, Point hole) {

    if (comb.numbers.isEmpty()) {
      return List.of(new Combination(Collections.emptyList()));
    }

    Point a = find(keyPad, current);
    Point b = find(keyPad, comb.numbers.get(0));

    List<Combination> combs1;
    if (Objects.equals(a, b)) {
      combs1 = List.of(new Combination(Collections.emptyList()));
    } else {
      List<String> movs = new ArrayList<>();
      IntStream.range(b.x, a.x).forEach(n -> movs.add("^"));
      IntStream.range(a.x, b.x).forEach(n -> movs.add("v"));
      IntStream.range(b.y, a.y).forEach(n -> movs.add("<"));
      IntStream.range(a.y, b.y).forEach(n -> movs.add(">"));

      combs1 = Generator.permutation(movs).simple().stream()
          .map(Combination::new)
          .filter(c -> isAllowed(c, a, hole))
          .toList();
    }
    List<Combination> combs2 = getSeqForKeyPad(comb, comb.numbers.remove(0), keyPad, hole);

    List<Combination> seqs = new ArrayList<>();
    for (Combination c1 : combs1) {
      for (Combination c2 : combs2) {
        List<String> c3 = new ArrayList<>(c1.numbers);
        c3.add(START);
        c3.addAll(c2.numbers);
        seqs.add(new Combination(c3));
      }
    }

    return seqs;
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

  private Point find(String[][] keyPad, String character) {
    for (int x = 0; x < keyPad.length; x++) {
      for (int y = 0; y < keyPad[x].length; y++) {
        if (character.equals(keyPad[x][y])) {
          return Point.of(x, y);
        }
      }
    }
    return null;
  }

  public static void main(String[] args) {
    Day.run(Day21::new, "2024/D21_small.txt");//, "2024/D21_full.txt");
  }
}
