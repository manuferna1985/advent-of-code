package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MathUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day6 extends Day {

  record Race(Long time, Long distance) {
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    return String.valueOf(findSolutions(fileContents, "#"));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return String.valueOf(findSolutions(fileContents, StringUtils.SPACE));
  }

  private Range<Long> getPossibleOptions(Race race) {
    Pair<Double, Double> rootSolutions = MathUtils.solveEquation2ndGrade(-1, race.time, - race.distance);
    double min = Math.nextUp(Math.min(rootSolutions.getLeft(), rootSolutions.getRight()));
    double max = Math.nextDown(Math.max(rootSolutions.getLeft(), rootSolutions.getRight()));
    return Range.between((long) Math.ceil(min), (long) Math.floor(max));
  }

  private Long findSolutions(String fileContents, String tokenToRemove) {
    String[] lines = fileContents.split(System.lineSeparator());
    List<Long> times = getNumbers(lines, 0, tokenToRemove);
    List<Long> distances = getNumbers(lines, 1, tokenToRemove);

    return IntStream
        .range(0, times.size())
        .mapToObj(i -> new Race(times.get(i), distances.get(i)))
        .map(this::getPossibleOptions)
        .mapToLong(rng -> rng.getMaximum() - rng.getMinimum() + 1)
        .reduce(1, Math::multiplyExact);
  }

  private List<Long> getNumbers(String[] lines, int i, String tokenToRemove) {
    return Arrays.stream(lines[i].substring(lines[i].indexOf(":") + 1).trim()
            .replace(tokenToRemove, StringUtils.EMPTY)
            .split(" "))
        .filter(StringUtils::isNotBlank)
        .mapToLong(Long::parseLong)
        .boxed().toList();
  }

  public static void main(String[] args) {
    Day.run(Day6::new, "2023/D6_small.txt", "2023/D6_full.txt");
  }
}
