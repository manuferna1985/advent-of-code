package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.RangeUtils;
import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19 extends Day {

  private static final String SEP = System.lineSeparator();
  private static final String ACCEPTED = "A";
  private static final String REJECTED = "R";
  private static final String INITIAL_WF = "in";
  private static final Character GT = '>';
  private static final Character LT = '<';
  private static final Integer MIN_RANGE = 1;
  private static final Integer MAX_RANGE = 4000;

  record Rule(String varName, Character op, Integer number, String destination) {
    public Rule opposite() {
      if (GT.equals(op)) {
        return new Rule(varName, LT, number + 1, destination);
      } else if (LT.equals(op)) {
        return new Rule(varName, GT, number - 1, destination);
      } else {
        return new Rule(varName, null, number, destination);
      }
    }
  }

  record Workflow(String name, List<Rule> rules) {
  }

  record Part(int x, int m, int a, int s) {
  }

  record Combination(Set<Range<Integer>> range) {
    public Combination() {
      this(Set.of(Range.between(MIN_RANGE, MAX_RANGE)));
    }

    public int size() {
      return range.stream().mapToInt(r -> r.getMaximum() - r.getMinimum() + 1).sum();
    }
  }

  static class Result {
    Combination[] ranges = {new Combination(), new Combination(), new Combination(), new Combination()};

    public long getNumCombinations() {
      return Arrays.stream(ranges).mapToLong(Combination::size).reduce(1L, (a, b) -> a * b);
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    String[] blocks = fileContents.split(SEP + SEP);

    return String.valueOf(
        processParts(
            buildWorkflows(blocks[0].split(SEP)),
            buildParts(blocks[1].split(SEP)))
            .stream()
            .mapToInt(p -> p.x + p.m + p.a + p.s).sum());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] blocks = fileContents.split(SEP + SEP);
    return String.valueOf(
        findMaxAcceptedCombinations(
            buildWorkflows(blocks[0].split(SEP))));
  }

  private long findMaxAcceptedCombinations(Map<String, Workflow> wfs) {
    wfs.put(ACCEPTED, new Workflow(ACCEPTED, Collections.emptyList()));
    wfs.put(REJECTED, new Workflow(REJECTED, Collections.emptyList()));

    return findMaxCombinationsForRule(wfs.get(INITIAL_WF), wfs, new ArrayList<>(), new ArrayList<>())
        .stream()
        .map(this::getPartialResultFor)
        .map(Result::getNumCombinations)
        .mapToLong(Long::valueOf)
        .sum();
  }

  private Result getPartialResultFor(List<Rule> rules) {
    Result partial = new Result();

    for (Rule r : rules) {
      int pos = switch (r.varName.charAt(0)) {
        case 'x' -> 0;
        case 'm' -> 1;
        case 'a' -> 2;
        case 's' -> 3;
        default -> throw new IllegalStateException("Unexpected value: " + r.varName.charAt(0));
      };
      partial.ranges[pos] = merge(partial.ranges[pos], r);
    }

    return partial;
  }

  private Combination merge(Combination current, Rule r) {
    Range<Integer> newRange = GT.equals(r.op) ? Range.between(MIN_RANGE, r.number):Range.between(r.number, MAX_RANGE);
    return new Combination(RangeUtils.subtractRangeFrom(current.range, newRange));
  }

  private List<List<Rule>> findMaxCombinationsForRule(Workflow wf,
                                                      Map<String, Workflow> wfs,
                                                      List<Rule> rules,
                                                      List<List<Rule>> accepted) {
    if (ACCEPTED.equals(wf.name)) {
      accepted.add(rules);
    } else {
      List<Rule> currentRules = new ArrayList<>(rules);
      for (Rule r : wf.rules) {
        findMaxCombinationsForRule(wfs.get(r.destination), wfs, copyWith(currentRules, r), accepted);
        currentRules.add(r.opposite());
      }
    }
    return accepted;
  }

  private List<Rule> copyWith(List<Rule> currentRules, Rule newRule) {
    List<Rule> newRules = new ArrayList<>(currentRules);
    if (newRule.varName!=null) {
      newRules.add(newRule);
    }
    return newRules;
  }

  private List<Part> processParts(Map<String, Workflow> wfs, List<Part> parts) {
    List<Part> acceptedParts = new ArrayList<>();

    for (Part part : parts) {

      String wfName = INITIAL_WF;
      boolean end = false;
      while (!end) {

        Workflow wf = wfs.get(wfName);

        for (Rule rule : wf.rules) {
          if (rule.op!=null) {
            boolean status = switch (rule.varName.charAt(0)) {
              case 'x' -> comp(rule, part.x);
              case 'm' -> comp(rule, part.m);
              case 'a' -> comp(rule, part.a);
              case 's' -> comp(rule, part.s);
              default -> false;
            };

            if (status) {
              wfName = rule.destination;
              break;
            }
          } else {
            wfName = rule.destination;
          }
        }

        if (ACCEPTED.equals(wfName) || REJECTED.equals(wfName)) {
          end = true;
          if (ACCEPTED.equals(wfName)) {
            acceptedParts.add(part);
          }
        }
      }
    }

    return acceptedParts;
  }

  private boolean comp(Rule rule, int value) {
    if (GT.equals(rule.op)) {
      return value > rule.number;
    } else if (LT.equals(rule.op)) {
      return value < rule.number;
    } else {
      throw new IllegalStateException("Unexpected value: " + rule.op);
    }
  }

  private Map<String, Workflow> buildWorkflows(String[] lines) {
    Map<String, Workflow> wfs = new HashMap<>();

    for (String line : lines) {
      String name = line.substring(0, line.indexOf("{"));
      String[] rawRules = line.substring(line.indexOf("{") + 1, line.indexOf("}")).split(",");

      List<Rule> rules = new ArrayList<>();
      for (String r : rawRules) {
        int opIndex = r.indexOf(LT) + r.indexOf(GT) + 1;

        if (opIndex!=-1) {
          rules.add(new Rule(
              r.substring(0, opIndex),
              r.charAt(opIndex),
              Integer.parseInt(r.substring(opIndex + 1, r.indexOf(":"))),
              r.substring(r.indexOf(":") + 1)));

        } else {
          rules.add(new Rule(null, null, null, r));
        }
      }

      wfs.put(name, new Workflow(name, rules));
    }
    return wfs;
  }

  private List<Part> buildParts(String[] lines) {
    List<Part> parts = new ArrayList<>();
    Pattern p = Pattern.compile("x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)");
    Matcher m;
    for (String line : lines) {
      m = p.matcher(line);
      if (m.find()) {
        parts.add(new Part(getAsInt(m, 1), getAsInt(m, 2), getAsInt(m, 3), getAsInt(m, 4)));
      }
    }
    return parts;
  }

  private int getAsInt(Matcher m, int group) {
    return Integer.parseInt(m.group(group));
  }

  public static void main(String[] args) {
    Day.run(Day19::new, "2023/D19_small.txt", "2023/D19_full.txt");
  }
}
