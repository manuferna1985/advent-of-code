package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.dijkstra.GenericNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class Day9 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    return String.valueOf(minTravel(buildCityLinks(fileContents)));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return String.valueOf(maxTravel(buildCityLinks(fileContents)));
  }

  private Map<String, List<GenericNode<String>>> buildCityLinks(String fileContents) {
    Map<String, List<GenericNode<String>>> edges = new HashMap<>();
    String[] lines = fileContents.split(System.lineSeparator());
    for (String travel : lines) {
      String[] travelParts = travel.split("to|=");
      String origin = travelParts[0].trim();
      String dest = travelParts[1].trim();
      int distance = Integer.parseInt(travelParts[2].trim());
      edges.computeIfAbsent(origin, k -> new ArrayList<>()).add(new GenericNode<>(dest, distance));
      edges.computeIfAbsent(dest, k -> new ArrayList<>()).add(new GenericNode<>(origin, distance));
    }
    return edges;
  }

  private int minTravel(Map<String, List<GenericNode<String>>> edges){
    List<String> cities = edges.keySet().stream().toList();
    return cities.stream()
        .mapToInt(city -> travel(edges, new ArrayList<>(cities), city, 0, Integer.MAX_VALUE, Math::min))
        .min().orElse(Integer.MAX_VALUE);
  }

  private int maxTravel(Map<String, List<GenericNode<String>>> edges){
    List<String> cities = edges.keySet().stream().toList();
    return cities.stream()
        .mapToInt(city -> travel(edges, new ArrayList<>(cities), city, 0, Integer.MIN_VALUE, Math::max))
        .max().orElse(Integer.MIN_VALUE);
  }

  private int travel(Map<String, List<GenericNode<String>>> edges, List<String> remainingCitiesToVisit, String currentCity, int cost, Integer initialValue, BinaryOperator<Integer> fn) {
    remainingCitiesToVisit.remove(currentCity);
    if (remainingCitiesToVisit.isEmpty()) {
      return cost;
    }

    int betterCost = initialValue;
    for (String city : remainingCitiesToVisit) {
      Optional<GenericNode<String>> link = edges.get(currentCity).stream().filter(n -> n.id().equals(city)).findFirst();
      if (link.isPresent()) {
        int minLinkCost = travel(edges, new ArrayList<>(remainingCitiesToVisit), link.get().id(), cost + link.get().cost(), initialValue, fn);
        betterCost = fn.apply(betterCost, minLinkCost);
      }
    }
    return betterCost;
  }

  public static void main(String[] args) {
    Day.run(Day9::new, "2015/D9_small.txt", "2015/D9_full.txt");
  }
}
