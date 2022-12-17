package es.ing.aoc.y2022;

import static es.ing.aoc.common.MathUtils.cloneWithoutElement;
import static es.ing.aoc.common.MathUtils.generateCombinationsInPairs;
import static es.ing.aoc.common.MathUtils.invertMap;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import es.ing.aoc.common.dijkstra.Graph;
import es.ing.aoc.common.dijkstra.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day16 extends Day {

    private static final Pattern FILE_PATTERN = Pattern.compile("Valve ([A-Z]+) has flow rate=([0-9]+); tunnels? leads? to valves? ([A-Z, ]+)$");
    private static final String INITIAL_VALVE = "AA";
    private static final int MAX_TIME_PART1 = 30;
    private static final int MAX_TIME_PART2 = 26;

    @Override
    protected String part1(String fileContents) throws Exception {
        return String.valueOf(executeValvesAlgorithm(fileContents, false));
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        return String.valueOf(executeValvesAlgorithm(fileContents, true));
    }

    private int executeValvesAlgorithm(String fileContents, boolean elephantHelp) {

        String[] lines = fileContents.split(System.lineSeparator());

        Map<String, List<String>> neighbours = new HashMap<>();
        Map<String, Integer> pressures = new HashMap<>();
        Map<String, Integer> dict = new HashMap<>();
        int count = 0;

        for (String valve : lines) {
            Matcher matcher = FILE_PATTERN.matcher(valve);
            if (matcher.find()) {
                // Basic fields
                String name = matcher.group(1);

                // Add current valve data to dictionaries
                dict.put(name, count++);
                pressures.put(name, Integer.parseInt(matcher.group(2)));

                // Calculate neighbours
                neighbours.put(name, new ArrayList<>());
                Arrays.stream(matcher.group(3).split(","))
                        .map(String::trim)
                        .forEach(neighbour -> neighbours.get(name).add(neighbour));
            }
        }

        // Tunnels to loop could be only the ones which add real pressure to the system.
        List<String> tunnels = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : pressures.entrySet()) {
            if (entry.getValue() != 0) {
                tunnels.add(entry.getKey());
            }
        }

        // Cache Dijkstra distances between all nodes
        Map<String, Map<String, Integer>> matrix = calculateNeighbourhoodMatrix(neighbours, dict);

        int maxPressure = 0;

        if (!elephantHelp) {
            maxPressure = getMaxPressureFor(matrix, pressures, tunnels, MAX_TIME_PART1);
        } else {
            int k = tunnels.size() / 2;
            for (Pair<List<String>, List<String>> comb : generateCombinationsInPairs(tunnels, k)) {
                int total = Stream.of(comb.a, comb.b)
                        .map(c -> getMaxPressureFor(matrix, pressures, c, MAX_TIME_PART2))
                        .mapToInt(Integer::intValue).sum();

                maxPressure = Math.max(maxPressure, total);
            }
        }

        return maxPressure;
    }

    private int getMaxPressureFor(Map<String, Map<String, Integer>> matrix,
                                  Map<String, Integer> pressures,
                                  List<String> remaining,
                                  int maxTime) {
        return bruteForceSearch(matrix, pressures, 0, 0, 0, INITIAL_VALVE, remaining, maxTime);
    }

    private Map<String, Map<String, Integer>> calculateNeighbourhoodMatrix(Map<String, List<String>> neighbours, Map<String, Integer> dict) {

        Map<Integer, List<Node>> edges = new HashMap<>();
        neighbours.keySet().forEach(k1 ->
                edges.put(dict.get(k1), neighbours.get(k1).stream().map(dict::get).map(Node::new).collect(Collectors.toList())));

        Map<Integer, String> inverseDict = invertMap(dict);

        Graph graph = new Graph(edges);
        Map<String, Map<String, Integer>> matrix = new HashMap<>();

        for (Map.Entry<String, Integer> entry : dict.entrySet()) {
            Map<String, Integer> currentDistances = new HashMap<>();
            // Get min distances using Dijkstra from the entry start point
            int[] shortestPaths = graph.algorithm(entry.getValue()).getDistances();

            for (int i = 0; i < shortestPaths.length; i++) {
                currentDistances.put(inverseDict.get(i), shortestPaths[i]);
            }
            matrix.put(entry.getKey(), currentDistances);
        }

        return matrix;
    }

    private int bruteForceSearch(Map<String, Map<String, Integer>> matrix,
                                 Map<String, Integer> pressures,
                                 int currentTime,
                                 int currentPressure,
                                 int currentFlow,
                                 String currentTunnel,
                                 List<String> remaining,
                                 int timeLimit) {

        int max = currentPressure + (timeLimit - currentTime) * currentFlow;

        for (String valve : remaining) {
            int distanceOpeningValve = matrix.get(currentTunnel).get(valve) + 1;
            if (currentTime + distanceOpeningValve < timeLimit) {
                int newTime = currentTime + distanceOpeningValve;
                int newPressure = currentPressure + distanceOpeningValve * currentFlow;
                int newFlow = currentFlow + pressures.get(valve);
                int possibleScore = bruteForceSearch(
                        matrix, pressures, newTime, newPressure, newFlow, valve, cloneWithoutElement(remaining, valve), timeLimit);
                max = Math.max(max, possibleScore);
            }
        }

        return max;
    }

    public static void main(String[] args) {
        Day.run(Day16::new, "2022/D16_small.txt", "2022/D16_full.txt");
    }
}
