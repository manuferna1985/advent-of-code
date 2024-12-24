package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day24 extends Day {

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
    long zValue = simulateSystemUntilZ(connections, wires, totalZ);
    return String.valueOf(zValue);
  }

  private long simulateSystemUntilZ(List<Connection> connections, Map<String, Boolean> wires, long totalZ) {
    while (true) {
      connections.forEach(con -> con.exec(wires));
      if (getActiveZWires(wires)==totalZ) {
        break;
      }
    }
    return getValueFromZWires(wires);
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

  private long getValueFromZWires(Map<String, Boolean> wires) {
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
    return Long.parseLong(StringUtils.reverse(zBinaryValue.toString()), 2);
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

    BigInteger x = BigInteger.ZERO, y = BigInteger.ONE;
    for (int z=0; z<totalZ; z++){
      Map<String, Boolean> wires = buildWires(x, y, totalZ);
      BigInteger zValue = BigInteger.valueOf(simulateSystemUntilZ(connections, wires, totalZ));

      //if (!x.add(y).equals(zValue)){
        System.out.printf("[z%s] %d + %d = %d%n", StringUtils.leftPad(String.valueOf(z), 2, "0"), x, y, zValue);
      //}

      //x = x.multiply(BigInteger.TWO);
      y = y.multiply(BigInteger.TWO);
    }

    return "";
  }

  private Map<String, Boolean> buildWires(BigInteger x, BigInteger y, int maxZ) {
    Map<String, Boolean> wires = new HashMap<>();

    String xBinary = StringUtils.leftPad(x.toString(2), maxZ - 1, "0");
    String yBinary = StringUtils.leftPad(y.toString(2), maxZ - 1, "0");

    String nPadded;
    for (int n = maxZ - 2; n >= 0; n--) {
      nPadded = StringUtils.leftPad(String.valueOf((maxZ - n - 2)), 2, "0");
      wires.put("x%s".formatted(nPadded), xBinary.charAt(n)=='1');
      wires.put("y%s".formatted(nPadded), yBinary.charAt(n)=='1');
    }

    return wires;
  }

  public static void main(String[] args) {
    Day.run(Day24::new, "2024/D24_full.txt");
  }
}
