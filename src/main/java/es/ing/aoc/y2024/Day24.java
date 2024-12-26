package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Tri;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.paukov.combinatorics3.Generator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day24 extends Day {

  private static final Random RANDOM = new Random();
  private static final String XOR = "XOR";
  private static final String AND = "AND";
  private static final String OR = "OR";

  record Connection(String left, String gate, String right, String result) {

    public Connection(String[] c) {
      this(c[0], c[1], c[2], c[4]);
    }

    public void exec(Map<String, Boolean> wires) {
      Boolean leftValue = wires.get(left);
      Boolean rightValue = wires.get(right);

      if (leftValue!=null && rightValue!=null) {
        wires.put(result,
            switch (gate) {
              case XOR -> !leftValue.equals(rightValue);
              case OR -> leftValue || rightValue;
              case AND -> leftValue && rightValue;
              default -> throw new RuntimeException("Invalid gate!");
            });
      }
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator() + System.lineSeparator());
    Map<String, Boolean> wires = buildWires(lines[0]);
    List<Connection> connections = buildConnections(lines);
    int totalZ = getTotalZFromConnections(connections);
    BigInteger zValue = simulateSystemUntilZ(connections, wires, totalZ);
    return String.valueOf(zValue);
  }

  private BigInteger simulateSystemUntilZ(List<Connection> connections, Map<String, Boolean> wires, long totalZ) {
    for (int n = 0; n < totalZ; n++) {
      connections.forEach(con -> con.exec(wires));
      if (getActiveZWires(wires)==totalZ) {
        return getValueFromZWires(wires);
      }
    }
    return null;
  }

  private static List<Connection> buildConnections(String[] lines) {
    return Arrays.stream(lines[1].split(System.lineSeparator()))
        .map(line -> new Connection(line.split(" ")))
        .toList();
  }

  private static Map<String, Boolean> buildWires(String lines) {
    return Arrays.stream(lines.split(System.lineSeparator()))
        .map(line -> line.split(":"))
        .collect(Collectors.toMap(
            value -> value[0],
            value -> value[1].trim().equals("1")));
  }

  private BigInteger getValueFromZWires(Map<String, Boolean> wires) {
    int zIndex = 0;
    StringBuilder zBinaryValue = new StringBuilder();
    while (true) {
      String wireName = String.format("z%s", StringUtils.leftPad(String.valueOf(zIndex), 2, "0"));
      if (wires.containsKey(wireName)) {
        zBinaryValue.append(BooleanUtils.isTrue(wires.get(wireName)) ? "1":"0");
      } else {
        break;
      }
      zIndex++;
    }
    return new BigInteger(StringUtils.reverse(zBinaryValue.toString()), 2);
  }

  private static int getTotalZFromConnections(List<Connection> connections) {
    return (int) connections.stream().filter(c -> c.result.startsWith("z")).count();
  }

  private int getActiveZWires(Map<String, Boolean> wires) {
    return (int) wires.keySet().stream().filter(key -> key.startsWith("z")).count();
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator() + System.lineSeparator());
    List<Connection> connections = buildConnections(lines);

    int totalZ = getTotalZFromConnections(connections);

    List<Tri<Long, Connection, Connection>> swappedData = new Vector<>();
    Generator.combination(connections).simple(2).stream().parallel().forEach(consToSwap -> {

      Connection c1 = consToSwap.get(0);
      Connection c2 = consToSwap.get(1);

      List<Connection> connectionsSwapped = new ArrayList<>(connections);
      connectionsSwapped.remove(c1);
      connectionsSwapped.remove(c2);

      connectionsSwapped.add(new Connection(c1.left, c1.gate, c1.right, c2.result));
      connectionsSwapped.add(new Connection(c2.left, c2.gate, c2.right, c1.result));

      long diff = 0L;
      for (int i = 0; i < 100; i++) {
        BigInteger x = new BigInteger(totalZ - 1, RANDOM);
        BigInteger y = new BigInteger(totalZ - 1, RANDOM);
        BigInteger expectedZ = x.add(y);
        String expectedZBinary = convertToBinaryWithLength(expectedZ, totalZ);
        Map<String, Boolean> wires = buildRandomInputs(x, y, totalZ - 1);

        BigInteger zValue = simulateSystemUntilZ(connectionsSwapped, wires, totalZ);
        if (zValue!=null) {
          String zBinaryValue = convertToBinaryWithLength(zValue, totalZ);
          diff += getSimilarity(expectedZBinary, zBinaryValue);
        } else {
          diff -= 500000;
        }
      }
      swappedData.add(Tri.of(diff, c1, c2));
    });

    swappedData.sort(Comparator.comparing(o -> o.a));
    Collections.reverse(swappedData);

    System.out.printf("[%d] %s <--> %s%n",
        swappedData.getFirst().a,
        swappedData.getFirst().b.result,
        swappedData.getFirst().c.result);

    return "";
  }

  private Map<String, Boolean> buildRandomInputs(BigInteger x, BigInteger y, int fixedLength) {
    Map<String, Boolean> wires = new HashMap<>();

    String xBinary = convertToBinaryWithLength(x, fixedLength);
    String yBinary = convertToBinaryWithLength(y, fixedLength);

    String nPadded;
    for (int n = fixedLength - 1; n >= 0; n--) {
      nPadded = StringUtils.leftPad(String.valueOf((fixedLength - n - 1)), 2, "0");
      wires.put("x%s".formatted(nPadded), xBinary.charAt(n)=='1');
      wires.put("y%s".formatted(nPadded), yBinary.charAt(n)=='1');
    }

    return wires;
  }

  private static String convertToBinaryWithLength(BigInteger x, int fixedLength) {
    return StringUtils.leftPad(x.toString(2), fixedLength, "0");
  }

  private static long getSimilarity(String c1, String c2) {
    return IntStream.range(0, c1.length()).filter(n -> c1.charAt(n)==c2.charAt(n)).count();
  }

  public static void main(String[] args) {
    Day.run(Day24::new, "2024/D24_small.txt", "2024/D24_full.txt");
  }
}
