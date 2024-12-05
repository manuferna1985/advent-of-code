package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day5 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator() + System.lineSeparator());
    List<Pair<Integer, Integer>> rules = readRules(lines);
    List<List<Integer>> pages = readPages(lines);

    return String.valueOf(pages.stream()
        .filter(ps -> isCorrectlyPrinted(ps, rules))
        .map(this::getMiddlePage)
        .mapToInt(Integer::intValue)
        .sum());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator() + System.lineSeparator());
    List<Pair<Integer, Integer>> rules = readRules(lines);
    List<List<Integer>> pages = readPages(lines);

    return String.valueOf(pages.stream()
        .filter(ps -> !isCorrectlyPrinted(ps, rules))
        .map(ps -> fixPageOrder(ps, rules))
        .map(this::getMiddlePage)
        .mapToInt(Integer::intValue)
        .sum());
  }

  private Integer getMiddlePage(List<Integer> ps) {
    return ps.get(ps.size() / 2);
  }

  private List<List<Integer>> readPages(String[] lines) {
    return Arrays.stream(lines[1].split(System.lineSeparator()))
        .map(ps -> Arrays.stream(ps.split(",")).map(Integer::parseInt).toList())
        .toList();
  }

  private List<Pair<Integer, Integer>> readRules(String[] lines) {
    return Arrays.stream(lines[0].split(System.lineSeparator()))
        .map(rule -> {
          String[] ruleParts = rule.split("\\|");
          return new Pair<>(
              Integer.parseInt(ruleParts[0]), Integer.parseInt(ruleParts[1]));
        }).toList();
  }

  private boolean isCorrectlyPrinted(List<Integer> pages, List<Pair<Integer, Integer>> rules) {
    if (pages.size()==1) {
      return true;
    }
    List<Integer> nextPages = pages.subList(1, pages.size());
    return isCorrectlyPrinted(pages.get(0), nextPages, rules)
        && isCorrectlyPrinted(nextPages, rules);
  }

  private boolean isCorrectlyPrinted(Integer page, List<Integer> nextPages, List<Pair<Integer, Integer>> rules) {
    return nextPages.stream().allMatch(next -> isPageOrdered(page, next, rules));
  }

  private boolean isPageOrdered(Integer pageOne, Integer pageTwo, List<Pair<Integer, Integer>> rules) {
    return rules.stream().noneMatch(rule -> matchRule(rule, pageTwo, pageOne));
  }

  private List<Integer> fixPageOrder(List<Integer> unorderedPages, List<Pair<Integer, Integer>> rules) {
    List<Integer> ordered = new ArrayList<>(unorderedPages);
    ordered.sort((o1, o2) -> {
      if (rules.stream().anyMatch(r -> matchRule(r, o1, o2))) {
        return -1;
      } else if (rules.stream().anyMatch(r -> matchRule(r, o2, o1))) {
        return 1;
      }
      return 0;
    });

    return ordered;
  }

  private boolean matchRule(Pair<Integer, Integer> rule, Integer p1, Integer p2) {
    return rule.getFirst().equals(p1) && rule.getSecond().equals(p2);
  }

  public static void main(String[] args) {
    Day.run(Day5::new, "2024/D5_small.txt", "2024/D5_full.txt");
  }
}
