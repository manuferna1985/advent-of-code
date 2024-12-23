package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day23 extends Day {

  private record Party<T extends Comparable<T>>(List<T> friends) {
    public Party(List<T> friends) {
      this.friends = friends;
      Collections.sort(friends);
    }

    public Party(T... friends) {
      this(Arrays.stream(friends).collect(Collectors.toList()));
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    List<Pair<String, String>> network =
        Arrays.stream(fileContents.split(System.lineSeparator()))
            .map(line -> line.split("-"))
            .map(parts -> Pair.of(parts[0], parts[1]))
            .toList();

    Map<String, List<String>> links = new HashMap<>();
    for (Pair<String, String> con : network) {
      links.computeIfAbsent(con.getLeft(), key -> new ArrayList<>()).add(con.getRight());
      links.computeIfAbsent(con.getRight(), key -> new ArrayList<>()).add(con.getLeft());
    }

    Set<Party<String>> parties = new HashSet<>();
    for (Pair<String, String> con : network) {
      List<String> linkedLeft = links.get(con.getLeft());
      List<String> linkedRight = links.get(con.getRight());
      Collection<String> commonLinks = CollectionUtils.intersection(linkedLeft, linkedRight);

      System.out.printf("[%s-%s] -> %s%n", con.getLeft(), con.getRight(), commonLinks);
      System.out.println("");

      for (String mutualFriend : commonLinks) {
        parties.add(new Party<>(con.getLeft(), con.getRight(), mutualFriend));
      }
    }

    return String.valueOf(parties.stream()
        .filter(party ->
            party.friends.stream().anyMatch(name -> name.startsWith("t")))
        //.filter(party -> isNotABigParty(party, links))
        .count());
  }

  private boolean isNotABigParty(Party<String> party, Map<String, List<String>> links) {

    List<String> linkedA = links.get(party.friends.get(0));
    List<String> linkedB = links.get(party.friends.get(1));
    List<String> linkedC = links.get(party.friends.get(2));

    Collection<String> commonLinks = CollectionUtils.intersection(
        CollectionUtils.intersection(linkedA, linkedB), linkedC);

    return commonLinks.isEmpty();
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    return "";
  }

  public static void main(String[] args) {
    Day.run(Day23::new, "2024/D23_small.txt", "2024/D23_full.txt");
  }
}
