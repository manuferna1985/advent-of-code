package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day12 extends Day {

  enum Record {
    UNKNOWN('?'),
    OPERATIONAL('.'),
    DAMAGED('#');

    private final char value;

    Record(char value) {
      this.value = value;
    }

    public String getValue() {
      return String.valueOf(value);
    }

    public static Record of(char c) {
      return Arrays.stream(Record.values()).filter(r -> r.value==c).findFirst().orElseThrow(() -> new RuntimeException("Record not found!"));
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {

    String[] lines = fileContents.split(System.lineSeparator());
    List<Pair<List<Record>, List<Integer>>> info = new ArrayList<>();

    for (String line : lines) {
      String[] parts = line.split(" ");
      info.add(Pair.of(
          parts[0].chars().mapToObj(c -> Record.of((char) c)).toList(),
          Arrays.stream(parts[1].split(",")).mapToInt(Integer::parseInt).boxed().toList()
      ));
    }

    int counter = 0, i=1;
    for (var comb : info) {
      Pattern pattern = buildPattern(comb.getRight());
      counter += calculateCombinations(comb, pattern, new HashSet<>());
      System.out.printf("%-5d - %d", i++, counter);
    }

    return String.valueOf(counter);
  }

  @Override
  protected String part2(String fileContents) throws Exception {

    String[] lines = fileContents.split(System.lineSeparator());


    return String.valueOf("");
  }

  private int calculateCombinations(Pair<List<Record>, List<Integer>> info, Pattern numbersPattern, Set<String> valids) {
    if (info.getLeft().contains(Record.UNKNOWN)) {
      List<Record> newRecord = new ArrayList<>();
      for (int i = 0; i < info.getLeft().size(); i++) {
        if (Record.UNKNOWN.equals(info.getLeft().get(i))) {

          List<Record> whenOperational = new ArrayList<>(newRecord);
          whenOperational.add(Record.OPERATIONAL);
          if (i < info.getLeft().size() - 1) {
            whenOperational.addAll(info.getLeft().subList(i + 1, info.getLeft().size()));
          }
          calculateCombinations(Pair.of(whenOperational, info.getRight()), numbersPattern, valids);

          List<Record> whenDamaged = new ArrayList<>(newRecord);
          whenDamaged.add(Record.DAMAGED);
          if (i < info.getLeft().size() - 1) {
            whenDamaged.addAll(info.getLeft().subList(i + 1, info.getLeft().size()));
          }
          calculateCombinations(Pair.of(whenDamaged, info.getRight()), numbersPattern, valids);

        }
        newRecord.add(info.getLeft().get(i));
      }
    } else {
      isCombinationValid(info, numbersPattern, valids);
    }

    return valids.size();
  }

  private boolean isCombinationValid(Pair<List<Record>, List<Integer>> info, Pattern numbersPattern, Set<String> valids) {
    String strToMatch = info.getLeft().stream().map(Record::getValue).collect(Collectors.joining(""));
    boolean result = numbersPattern.matcher(strToMatch).matches();
    //System.out.printf("%-20s%s\n", strToMatch, result);

    if (result) {
      valids.add(strToMatch);
    }
    return result;
  }

  private Pattern buildPattern(List<Integer> numbers) {
    StringBuilder regex = new StringBuilder();
    regex.append("(\\.)*");
    for (int i = 0; i < numbers.size(); i++) {
      regex.append("(\\#){%d}".formatted(numbers.get(i)));
      if (i < numbers.size() - 1) {
        regex.append("(\\.)+");
      }
    }
    regex.append("(\\.)*");
    return Pattern.compile(regex.toString());
  }

  public static void main(String[] args) {
    Day.run(Day12::new, "2023/D12_full.txt"); //, "2023/D12_full.txt");
  }
}
