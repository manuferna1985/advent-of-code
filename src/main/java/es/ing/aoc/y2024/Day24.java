package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    BigInteger zValue = simulateSystemUntilZ(connections, wires, totalZ);
    return String.valueOf(zValue);
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator() + System.lineSeparator());
    List<Connection> connections = buildConnections(lines);
    int totalZ = getTotalZFromConnections(connections);

    // Initial exception:   x00 XOR y00 => z00 --- S(0)
    //                      x00 AND y00 => bdj --- Cout(0)
    // for i=01 to i=44
    //   x(i)    XOR  y(i)        => sum(i)      Ex: y01 XOR x01 -> twd
    //   x(i)    AND  y(i)        => carry(i)    Ex: y01 AND x01 -> gwd
    //   sum(i)  XOR  Cout(i-1)   => S(i)        Ex: twd XOR bdj -> z01
    //   sum(i)  AND  Cout(i-1)   => Aux(i)      Ex: twd AND bdj -> cbq
    //   Aux(i)  OR   carry(i)    => Cout(i)     Ex: cbq OR gwd  -> rhr
    //
    // End exception:       vdn OR qtn  => z45 --- Cout(44)

    Set<Connection> swappedConns = new HashSet<>();

    // Loop to get all simple Sum's and Carry's for each bit
    Optional<Connection> rOpt;
    Connection r;
    for (int i = 0; i < totalZ - 1; i++) {
      // Search Sum's
      rOpt = searchFirstConnectionWith(connections, getX(i), getY(i), XOR, "*");
      if (rOpt.isPresent() && i > 0) {
        r = rOpt.get();
        // sum[i] CANNOT be a var, and should has XOR and AND gates with Cout(-1)
        Optional<Connection> c1 = searchFirstConnectionWith(connections, r.result, "*", XOR, "*");
        Optional<Connection> c2 = searchFirstConnectionWith(connections, r.result, "*", AND, "*");

        if (c1.isEmpty() || c2.isEmpty() || connectionHasAllVars(c1.get()) || connectionHasAllVars(c2.get())) {
          swappedConns.add(r);
        }
      }

      // Search Carry's
      rOpt = searchFirstConnectionWith(connections, getX(i), getY(i), AND, "*");
      if (rOpt.isPresent() && i > 0) {
        r = rOpt.get();
        // carry[i] CANNOT be a var, and should has OR with another wire also not var
        Optional<Connection> c1 = searchFirstConnectionWith(connections, r.result, "*", OR, "*");

        if (c1.isEmpty() || connectionHasAllVars(c1.get())) {
          swappedConns.add(r);
        }
      }

      if (i > 0) {
        // Compute and search S's
        Optional<Connection> c1 = searchFirstConnectionWith(connections, "*", "*", XOR, getZ(i));
        if (c1.isEmpty()) {
          Optional<Connection> c2 = searchFirstConnectionWith(connections, "*", "*", "*", getZ(i));
          if (c2.isPresent()) {
            swappedConns.add(c2.get());
          }
        }
      }
    }

    List<Connection> c2 = searchAllConnectionsWith(connections, "*", "*", XOR, "*",
        c -> !wireIsVariable(c.left) && !wireIsVariable(c.right) && !c.result.startsWith("z"));

    if (!c2.isEmpty()) {
      swappedConns.addAll(c2);
    }

    return swappedConns.stream().map(Connection::result).sorted().collect(Collectors.joining(","));
  }

  private boolean connectionHasAllVars(Connection c) {
    return Stream.of(c.left, c.right, c.result).allMatch(this::wireIsVariable);
  }

  private boolean wireIsVariable(String wire) {
    return wire.startsWith("x") || wire.startsWith("y") || wire.startsWith("z");
  }

  private String getX(int i) {
    return "x%s".formatted(StringUtils.leftPad(String.valueOf(i), 2, "0"));
  }

  private String getY(int i) {
    return "y%s".formatted(StringUtils.leftPad(String.valueOf(i), 2, "0"));
  }

  private String getZ(int i) {
    return "z%s".formatted(StringUtils.leftPad(String.valueOf(i), 2, "0"));
  }

  private Optional<Connection> searchFirstConnectionWith(List<Connection> connectionList,
                                                         String a,
                                                         String b,
                                                         String gate,
                                                         String result) {
    return searchAllConnectionsWith(connectionList, a, b, gate, result).stream().findFirst();
  }

  private List<Connection> searchAllConnectionsWith(List<Connection> connectionList,
                                                    String a,
                                                    String b,
                                                    String gate,
                                                    String result) {
    return searchAllConnectionsWith(connectionList, a, b, gate, result, c -> true);
  }

  private List<Connection> searchAllConnectionsWith(List<Connection> connectionList,
                                                    String a,
                                                    String b,
                                                    String gate,
                                                    String result,
                                                    Predicate<Connection> extraCondition) {
    return connectionList.stream()
        .filter(c -> equalWires(c.gate, gate))
        .filter(c -> equalWires(c.result, result))
        .filter(c -> ((equalWires(c.left, a) && equalWires(c.right, b)) || (equalWires(c.left, b) && equalWires(c.right, a))))
        .filter(extraCondition)
        .toList();
  }

  private boolean equalWires(String w1, String w2) {
    return w1.equals("*") || w2.equals("*") || w1.equals(w2);
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

  public static void main(String[] args) {
    Day.run(Day24::new, "2024/D24_small.txt", "2024/D24_full.txt");
  }
}
