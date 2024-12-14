package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.gaussian.GaussianEliminationSystemImpl1;
import org.apache.commons.math3.util.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day13 extends Day {

  private static final BigDecimal MAX = BigDecimal.valueOf(100);
  private static final BigDecimal[] COSTS = {BigDecimal.valueOf(3), BigDecimal.valueOf(1)};
  private static final BigDecimal FACTOR = new BigDecimal("10000000000000");

  record Machine(Pair<BigDecimal, BigDecimal> a, Pair<BigDecimal, BigDecimal> b, Pair<BigDecimal, BigDecimal> price) {
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    return getNumberOfTokens(fileContents, false);
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return getNumberOfTokens(fileContents, true);
  }

  private String getNumberOfTokens(String fileContents, boolean applyPriceTransformation) {
    String[] machineData = fileContents.split(System.lineSeparator() + System.lineSeparator());

    List<Machine> machines = new ArrayList<>();
    for (String rawMachine : machineData) {
      String[] lines = rawMachine.split(System.lineSeparator());
      machines.add(new Machine(
          readButtonData(lines[0]),
          readButtonData(lines[1]),
          readPriceData(lines[2], applyPriceTransformation ? FACTOR : BigDecimal.ZERO)));
    }

    BigInteger total = BigInteger.ZERO;
    for (Machine m : machines) {
      BigDecimal[][] matrix = {{m.a.getFirst(), m.b.getFirst(), m.price.getFirst()}, {m.a.getSecond(), m.b.getSecond(), m.price.getSecond()}};
      BigDecimal[] solution = new GaussianEliminationSystemImpl1().applyAlgorithm(matrix);

      if (isValidSolution(solution, applyPriceTransformation)) {
        total = total.add(getSolutionCost(solution));
      }
    }

    return String.valueOf(total);
  }

  private Pair<BigDecimal, BigDecimal> readButtonData(String line) {
    return new Pair<>(
        new BigDecimal(line.substring(line.indexOf("X") + 2, line.indexOf(","))),
        new BigDecimal(line.substring(line.indexOf("Y") + 2)));
  }

  private Pair<BigDecimal, BigDecimal> readPriceData(String line, BigDecimal priceFactor) {
    return new Pair<>(
        priceFactor.add(new BigDecimal(line.substring(line.indexOf("X") + 2, line.indexOf(",")))),
        priceFactor.add(new BigDecimal(line.substring(line.indexOf("Y") + 2))));
  }

  private boolean isValidSolution(BigDecimal[] solution, boolean skipMaxTokensLimit) {
    return Arrays.stream(solution)
        .allMatch(d -> (skipMaxTokensLimit || d.compareTo(MAX) < 0) && new BigDecimal(d.toBigInteger()).compareTo(d)==0);
  }

  private BigInteger getSolutionCost(BigDecimal[] solution) {
    return solution[0].multiply(COSTS[0]).add(solution[1].multiply(COSTS[1])).toBigInteger();
  }

  public static void main(String[] args) {
    Day.run(Day13::new, "2024/D13_small.txt", "2024/D13_full.txt");
  }
}
