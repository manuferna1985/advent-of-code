package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day7 extends Day {

  private static final String VARIABLE_TO_FIND = "a";
  private static final String VARIABLE_TO_OVERRIDE = "b";
  private static final Integer NORMALIZE_MAX = 65536;

  enum LogicGate {
    VALUE, AND, OR, LSHIFT, RSHIFT, NOT;

    public static LogicGate of(String op) {
      return Arrays.stream(LogicGate.values()).filter(g -> op.contains(g.name())).findFirst().orElse(VALUE);
    }

    public static List<String> getGateNames() {
      return Arrays.stream(LogicGate.values()).map(Enum::name).toList();
    }
  }

  record Operation(List<String> inputs, LogicGate gate, String result) {
    public boolean canBeSolved(Map<String, Integer> variables) {
      return inputs.stream().allMatch(input -> variables.containsKey(input) || StringUtils.isNumeric(input));
    }

    public Integer exec(Map<String, Integer> variables) {
      List<Integer> inputValues = inputs.stream()
          .map(input -> StringUtils.isNumeric(input) ? Integer.parseInt(input):variables.get(input))
          .toList();

      return normalize(switch (this.gate) {
        case VALUE -> inputValues.get(0);
        case NOT -> ~inputValues.get(0);
        case AND -> inputValues.get(0) & inputValues.get(1);
        case OR -> inputValues.get(0) | inputValues.get(1);
        case RSHIFT -> inputValues.get(0) >> inputValues.get(1);
        case LSHIFT -> inputValues.get(0) << inputValues.get(1);
      });
    }

    private Integer normalize(Integer input) {
      return input < 0 ? input + NORMALIZE_MAX:input;
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    List<Operation> ops = Arrays.stream(fileContents.split(System.lineSeparator())).map(this::buildOperation).toList();
    return String.valueOf(resolveAllVars(new HashMap<>(), ops).get(VARIABLE_TO_FIND));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    List<Operation> ops = Arrays.stream(fileContents.split(System.lineSeparator())).map(this::buildOperation).toList();
    Map<String, Integer> vars = resolveAllVars(new HashMap<>(), ops);
    Integer aValue = vars.get(VARIABLE_TO_FIND);
    vars.clear();
    vars.put(VARIABLE_TO_OVERRIDE, aValue);
    return String.valueOf(resolveAllVars(vars, ops).get(VARIABLE_TO_FIND));
  }

  private Map<String, Integer> resolveAllVars(Map<String, Integer> vars, List<Operation> ops) {
    boolean newResults = true;
    while (newResults) {
      newResults = false;
      for (var op : ops) {
        if (!vars.containsKey(op.result) && op.canBeSolved(vars)) {
          newResults = true;
          vars.put(op.result, op.exec(vars));
        }
      }
    }
    return vars;
  }

  private Operation buildOperation(String line) {
    LogicGate gate = LogicGate.of(line);
    List<String> args = Arrays.stream(line.split("->")[0].trim().split(StringUtils.SPACE))
        .filter(arg -> !LogicGate.getGateNames().contains(arg))
        .toList();
    String result = line.split("->")[1].trim();
    return new Operation(args, gate, result);
  }

  public static void main(String[] args) {
    Day.run(Day7::new, "2015/D7_small.txt", "2015/D7_full.txt");
  }
}
