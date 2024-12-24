package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
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

    Map<String, Boolean> wires = Arrays.stream(lines[0].split(System.lineSeparator()))
        .map(line -> line.split(":"))
        .collect(Collectors.toMap(
            value -> value[0],
            value -> value[1].trim().equals("1")));

    List<Connection> connections = Arrays.stream(lines[1].split(System.lineSeparator()))
        .map(line -> new Connection(line.split(" ")))
        .toList();

    long totalZ = getTotalZFromConnections(connections);
    System.out.println(totalZ);

    while (true) {
      connections.forEach(con -> con.exec(wires));
      long currentZ = getActiveZWires(wires);
      if (currentZ==totalZ) {
        break;
      }
    }

    long zValue = getValueFromZWires(wires);

    return String.valueOf(zValue);
  }

  private long getValueFromZWires(Map<String, Boolean> wires) {

    int zIndex = 0;
    StringBuilder zBinaryValue = new StringBuilder();
    while(true){
      String wireName = String.format("z%s", StringUtils.leftPad(String.valueOf(zIndex), 2, "0"));

      if (wires.containsKey(wireName)){
        zBinaryValue.append(wires.get(wireName) ? "1" : "0");
      } else {
        break;
      }
      zIndex++;
    }

    return Long.parseLong(StringUtils.reverse(zBinaryValue.toString()), 2);
  }

  private static long getTotalZFromConnections(List<Connection> connections) {
    return connections.stream().filter(c -> c.result.startsWith("z")).count();
  }

  private long getActiveZWires(Map<String, Boolean> wires) {
    return wires.keySet().stream().filter(key -> key.startsWith("z")).count();
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    return "";
  }

  public static void main(String[] args) {
    Day.run(Day24::new, "2024/D24_small.txt", "2024/D24_full.txt");
  }
}
