package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static es.ing.aoc.y2023.Day12.Record.OPERATIONAL;
import static es.ing.aoc.y2023.Day12.Record.UNKNOWN;

public class Day12 extends Day {

  enum Record {
    UNKNOWN('?'),
    OPERATIONAL('.'),
    DAMAGED('#');

    private final char value;

    Record(char value) {
      this.value = value;
    }

    public static Record of(char c) {
      return Arrays.stream(Record.values()).filter(r -> r.value==c).findFirst().orElseThrow(() -> new RuntimeException("Record not found!"));
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    //return algorithm(fileContents, 1);
    return "";
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return algorithm(fileContents, 5);
  }

  private String algorithm(String fileContents, int folding) {
    String[] lines = fileContents.split(System.lineSeparator());
    List<Pair<List<Record>, List<Integer>>> info = new ArrayList<>();

    for (String line : lines) {
      String[] parts = line.split(" ");
      info.add(
          fillRecords(
              Pair.of(
                  parts[0].chars().mapToObj(c -> Record.of((char) c)).toList(),
                  Arrays.stream(parts[1].split(",")).mapToInt(Integer::parseInt).boxed().toList()),
              folding));
    }

    int counter = 0, i = 0;
    for (var comb : info) {
      Pattern pattern = buildPattern(comb.getRight());
      counter += calculateCombinations(comb, pattern, new HashMap<>());
      System.out.printf("%d  - %d\n", i++, counter);
    }

    return String.valueOf(counter);
  }

  private Pair<List<Record>, List<Integer>> fillRecords(Pair<List<Record>, List<Integer>> info, int n) {
    List<Record> newRecords = new ArrayList<>();
    List<Integer> newNumbers = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      newRecords.addAll(info.getLeft());
      newNumbers.addAll(info.getRight());
      if (i < n - 1) {
        newRecords.add(UNKNOWN);
      }
    }

    return Pair.of(newRecords, newNumbers);
  }

  private int calculateCombinations(Pair<List<Record>, List<Integer>> info, Pattern numbersPattern, Map<Triple<Integer, Integer, Record>, Integer> cache) {

    String strToMatch = info.getLeft().stream().map(Record::toString).collect(Collectors.joining(StringUtils.EMPTY));
    Matcher matcher = numbersPattern.matcher(strToMatch);
    System.out.println("before");
    boolean matchResult = matcher.matches();
    System.out.println("after");
    boolean allResolved = !info.getLeft().contains(UNKNOWN);
    int firstUnknown = info.getLeft().indexOf(UNKNOWN);

    if (matchResult) {
      int groups = 0;

      for (int i = 1; i <= matcher.groupCount(); i++) {
        if (!matcher.group(i).contains(UNKNOWN.toString())) {
          groups++;
        }
      }

      if (allResolved || groups==info.getRight().size()) {
        return 1;
      } else {

        Triple<Integer, Integer, Record> cacheKey = Triple.of(groups, firstUnknown, firstUnknown > 0 ? info.getLeft().get(firstUnknown - 1):UNKNOWN);

        if (!cache.containsKey(cacheKey)) {
          List<Record> newRecord = new ArrayList<>();
          for (int i = 0; i < info.getLeft().size(); i++) {
            if (UNKNOWN.equals(info.getLeft().get(i))) {

              List<Record> whenOperational = new ArrayList<>(newRecord);
              whenOperational.add(OPERATIONAL);
              if (i < info.getLeft().size() - 1) {
                whenOperational.addAll(info.getLeft().subList(i + 1, info.getLeft().size()));
              }
              int n1 = calculateCombinations(Pair.of(whenOperational, info.getRight()), numbersPattern, cache);

              List<Record> whenDamaged = new ArrayList<>(newRecord);
              whenDamaged.add(Record.DAMAGED);
              if (i < info.getLeft().size() - 1) {
                whenDamaged.addAll(info.getLeft().subList(i + 1, info.getLeft().size()));
              }
              int n2 = calculateCombinations(Pair.of(whenDamaged, info.getRight()), numbersPattern, cache);

              cache.put(cacheKey, n1 + n2);
              return n1 + n2;
            } else {
              newRecord.add(info.getLeft().get(i));
            }
          }
        } else {
          return cache.get(cacheKey);
        }
      }
    }
    return 0;
  }

  private Pattern buildPattern(List<Integer> numbers) {
    StringBuilder regex = new StringBuilder();
    regex.append("[\\.\\?]*");
    for (int i = 0; i < numbers.size(); i++) {
      regex.append("([\\#\\?]{%d})".formatted(numbers.get(i)));
      if (i < numbers.size() - 1) {
        regex.append("[\\.\\?]+");
      }
    }
    regex.append("[\\.\\?]*");
    return Pattern.compile(regex.toString());
  }

  public static void main(String[] args) {
    Day.run(Day12::new, "2023/D12_small.txt"); //, "2023/D12_full.txt");
  }
}
