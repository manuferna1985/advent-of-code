package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day4 extends Day {


  @Override
  protected String part1(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());
    List<Pair<List<Integer>, List<Integer>>> cards = readScratchCards(lines);
    int sum = cards.stream().map(this::getCardPoints).mapToInt(Integer::intValue).sum();
    return String.valueOf(sum);
  }


  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    List<Pair<List<Integer>, List<Integer>>> cards = readScratchCards(lines);
    Map<Integer, Integer> copies = new HashMap<>();

    for (int i = 0; i < cards.size(); i++) {
      copies.put(i, 1);
    }

    for (int i = 0; i < cards.size(); i++) {
      for (int j = 1; j <= getMatchingNumbers(cards.get(i)); j++) {
        copies.put(i + j, copies.get(i + j) + copies.get(i));
      }
    }
    return String.valueOf(copies.values().stream().mapToInt(Integer::intValue).sum());
  }

  private List<Pair<List<Integer>, List<Integer>>> readScratchCards(String[] lines) {

    List<Pair<List<Integer>, List<Integer>>> cards = new ArrayList<>();

    for (String line : lines) {
      String winningNumbers = line.substring(line.indexOf(":") + 1, line.indexOf("|"));
      String cardNumbers = line.substring(line.indexOf("|") + 1);
      cards.add(Pair.of(getNumbersList(winningNumbers), getNumbersList(cardNumbers)));
    }

    return cards;
  }

  private List<Integer> getNumbersList(String numbers) {
    return Arrays.stream(numbers.trim().split(" ")).filter(StringUtils::isNotBlank).mapToInt(Integer::parseInt)
        .boxed()
        .collect(Collectors.toList());
  }

  private Integer getCardPoints(Pair<List<Integer>, List<Integer>> card) {
    int points = 0;
    for (Integer n : card.getRight()) {
      if (card.getLeft().contains(n)) {
        points = (points==0) ? 1:points * 2;
      }
    }
    return points;
  }

  private Integer getMatchingNumbers(Pair<List<Integer>, List<Integer>> card) {
    return (int) card.getRight().stream().filter(n -> card.getLeft().contains(n)).count();
  }

  public static void main(String[] args) {
    Day.run(Day4::new, "2023/D4_small.txt", "2023/D4_full.txt");
  }
}
