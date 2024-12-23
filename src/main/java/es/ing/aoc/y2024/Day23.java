package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day23 extends Day {

  private record Party(List<String> friends) {
    public Party(List<String> friends) {
      this.friends = friends;
      Collections.sort(this.friends);
    }

    public Party(String... friends) {
      this(Arrays.stream(friends).collect(Collectors.toList()));
    }

    public boolean isMatchingT() {
      return this.friends.stream().anyMatch(name -> name.startsWith("t"));
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    return String.valueOf(buildNetworks(fileContents, false).stream()
        .filter(Party::isMatchingT)
        .count());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return String.valueOf(buildNetworks(fileContents, true).stream()
        .filter(Party::isMatchingT)
        .max(Comparator.comparingInt(p -> p.friends.size()))
            .map(party -> StringUtils.join(party.friends, ","))
        .orElse(null));
  }

  private Set<Party> buildNetworks(String fileContents, boolean findMaxParties) {
    List<Pair<String, String>> network =
        Arrays.stream(fileContents.split(System.lineSeparator()))
            .map(line -> line.split("-"))
            .map(parts -> Pair.of(parts[0], parts[1]))
            .toList();

    Map<String, Collection<String>> links = new HashMap<>();
    for (Pair<String, String> con : network) {
      links.computeIfAbsent(con.getLeft(), key -> new ArrayList<>()).add(con.getRight());
      links.computeIfAbsent(con.getRight(), key -> new ArrayList<>()).add(con.getLeft());
    }

    Set<Party> parties = new HashSet<>();
    if (!findMaxParties){
      for (Pair<String, String> con : network) {
        Collection<String> commonLinks = CollectionUtils.intersection(
            links.get(con.getLeft()),
            links.get(con.getRight()));
        // We just need all the combinations for the current connection (L,R) with each of their common computers
        for (String mutualFriend : commonLinks) {
          parties.add(new Party(con.getLeft(), con.getRight(), mutualFriend));
        }
      }
    } else {
      // We need to grow each party until its maximum neighbours
      for (Pair<String, String> con : network) {
        parties.add(growParty(new Party(con.getLeft(), con.getRight()), links));
      }
    }
    return parties;
  }

  private Party growParty(Party party, Map<String, Collection<String>> links) {
    Collection<String> partyIntersect = intersection(party.friends.stream().map(links::get).toList());
    if (!partyIntersect.isEmpty()) {
      List<String> bigParty = new ArrayList<>(party.friends);
      bigParty.add(((ArrayList<String>) partyIntersect).getFirst());
      return growParty(new Party(bigParty), links);
    }
    return party;
  }

  private <T> Collection<T> intersection(List<Collection<T>> cols) {
    return cols.stream().reduce(CollectionUtils::intersection).orElse(Collections.emptyList());
  }

  public static void main(String[] args) {
    Day.run(Day23::new, "2024/D23_small.txt", "2024/D23_full.txt");
  }
}
