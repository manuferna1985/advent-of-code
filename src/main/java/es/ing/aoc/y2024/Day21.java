package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.StringUtils;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day21 extends Day {

  private static final String START = "A";
  private static final Point NUMERIC_KEYPAD_HOLE = Point.of(3, 0);
  private static final Point DIRECTIONAL_KEYPAD_HOLE = Point.of(0, 0);

  private static final String[][] NUMERIC_KEYPAD = {
      {"7", "8", "9"},
      {"4", "5", "6"},
      {"1", "2", "3"},
      {"", "0", "A"},
  };

  private static final String[][] DIRECTIONAL_KEYPAD = {
      {"", "^", "A"},
      {"<", "v", ">"}
  };

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
            .map(this::getComplexity)
            .mapToLong(Integer::intValue)
            .sum());
  }

  private Integer getComplexity(Combination comb) {

    int numericPart = Integer.parseInt(StringUtils.join(comb.numbers, "").substring(0, 3));

    System.out.println(comb);
    List<Combination> c1 = getMinLengthCombs(getSeqForNumericKeyPad(comb));
    System.out.printf("[%d] -> %d%n", c1.size(), c1.get(0).numbers.size());
    //System.out.println(c1);

    for (int i=0; i<2; i++) {
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
    return getSeqForKeyPad(comb, START, DIRECTIONAL_KEYPAD, DIRECTIONAL_KEYPAD_HOLE);
  }

  private List<Combination> getSeqForNumericKeyPad(Combination comb) {
    return getSeqForKeyPad(comb, START, NUMERIC_KEYPAD, NUMERIC_KEYPAD_HOLE);
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
          .filter(c -> isAllowed(c, a, b, hole))
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

  private boolean isAllowed(Combination c, Point a, Point b, Point hole) {

    List<Point> path = new ArrayList<>();

    Point current = a;
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

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    return "";
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
