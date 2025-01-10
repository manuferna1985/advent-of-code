package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.tuple.Pair;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day13 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    Map<String, List<Pair<String, Integer>>> rules = buildRules(fileContents);
    List<List<String>> permutations = Generator.permutation(rules.keySet()).simple().stream().toList();
    return String.valueOf(permutations.stream().mapToInt(perm -> calculateHappiness(rules, perm)).max().orElse(0));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    Map<String, List<Pair<String, Integer>>> rules = buildRules(fileContents);
    addMySelf(rules);
    List<List<String>> permutations = Generator.permutation(rules.keySet()).simple().stream().toList();
    return String.valueOf(permutations.stream().mapToInt(perm -> calculateHappiness(rules, perm)).max().orElse(0));
  }

  private void addMySelf(Map<String, List<Pair<String, Integer>>> rules) {
    List<String> people = rules.keySet().stream().toList();
    for (String p : people){
      rules.computeIfAbsent("me", key -> new ArrayList<>()).add(Pair.of(p, 0));
      rules.computeIfAbsent(p, key -> new ArrayList<>()).add(Pair.of("me", 0));
    }
  }

  private Map<String, List<Pair<String, Integer>>> buildRules(String fileContents) {
    String[] lines = fileContents.split(System.lineSeparator());
    Map<String, List<Pair<String, Integer>>> rules = new HashMap<>();
    for (String line : lines) {
      String[] ruleParts = line.split(" ");
      String a = ruleParts[0];
      String b = ruleParts[10].replace(".", "");
      int points = Integer.parseInt(ruleParts[3]);
      points *= ruleParts[2].equals("gain") ? 1:-1;
      rules.computeIfAbsent(a, key -> new ArrayList<>()).add(Pair.of(b, points));
    }
    return rules;
  }

  private int calculateHappiness(Map<String, List<Pair<String, Integer>>> rules, List<String> perm) {
    int happiness = 0;
    for (int i=0; i<perm.size(); i++){
      String me = perm.get(i);
      String left = perm.get((i-1+perm.size()) % perm.size());
      String right = perm.get((i+1) % perm.size());

      List<Pair<String, Integer>> myRules = rules.get(me);
      happiness += myRules.stream().filter(them -> them.getKey().equals(left)).mapToInt(Pair::getValue).findFirst().orElse(0);
      happiness += myRules.stream().filter(them -> them.getKey().equals(right)).mapToInt(Pair::getValue).findFirst().orElse(0);
    }
    return happiness;
  }

  public static void main(String[] args) {
    Day.run(Day13::new, "2015/D13_small.txt", "2015/D13_full.txt");
  }
}
