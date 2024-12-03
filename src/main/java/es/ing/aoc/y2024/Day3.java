package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day3 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    return algorithm(fileContents, false);
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return algorithm(fileContents, true);
  }

  private String algorithm(String fileContents, boolean useStatusRanges){

    String line = String.join("", fileContents.split(System.lineSeparator()));

    Pattern pattern = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)|do\\(\\)|don't\\(\\)");
    Matcher matcher = pattern.matcher(line);

    List<Pair<Integer, Integer>> pairs = new ArrayList<>();
    boolean enabled = true;
    while (matcher.find()) {
      String op = matcher.group();
      if (op.equals("do()")) {
        enabled = true;
      } else if (op.equals("don't()")) {
        enabled = false;
      } else {
        if (enabled || !useStatusRanges) {
          String[] opParts = op.split("[,()]");
          pairs.add(Pair.of(Integer.parseInt(opParts[1]), Integer.parseInt(opParts[2])));
        }
      }
    }

    return String.valueOf(
        pairs.stream()
            .map(p -> p.getLeft() * p.getRight())
            .mapToInt(Integer::intValue)
            .sum());

  }

  public static void main(String[] args) {
    Day.run(Day3::new, "2024/D3_small.txt", "2024/D3_full.txt");
  }
}
