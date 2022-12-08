package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class D12_exercise_Part1 {

    public static class Cave {

        String name;
        List<Cave> connections;

        public static Cave of(String name) {
            Cave c = new Cave();
            c.name = name;
            c.connections = new ArrayList<>();
            return c;
        }

        public void addConnectionWith(Cave other) {
            this.connections.add(other);
        }

        public boolean isSmallCave() {
            return name.length() < 3 && Character.isLowerCase(name.charAt(0));
        }

        public boolean isBigCave() {
            return name.length() < 3 && Character.isUpperCase(name.charAt(0));
        }

        public boolean isStart() {
            return "start".equalsIgnoreCase(name);
        }

        public boolean isEnd() {
            return "end".equalsIgnoreCase(name);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cave cave = (Cave) o;
            return name.equals(cave.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        public String toString() {
            return name;
        }
    }


    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D12_exercise_Part1.class.getResource("2021/D12_full.txt").toURI()), Charset.defaultCharset());

        // Build cave
        Map<String, Cave> map = new HashMap<>();

        for (String conn : allLines) {

            String[] connArray = conn.split("-");
            String init = connArray[0];
            String end = connArray[1];

            Cave initCave = getOrCreate(map, init);
            Cave endCave = getOrCreate(map, end);
            initCave.addConnectionWith(endCave);
            endCave.addConnectionWith(initCave);
        }

        System.out.println(map);

        // Loop cave tunnels

        Cave start = map.values().stream().filter(Cave::isStart).findFirst().get();

        Set<String> differentPaths = new HashSet<>();

        walk(start, new ArrayList<>(), differentPaths);

        System.out.println(differentPaths);
        System.out.println(differentPaths.size());
    }

    private static void walk(Cave currentCave, List<Cave> currentPath, Set<String> differentPaths) {

        currentPath.add(currentCave);

        if (currentCave.isEnd()) {
            differentPaths.add(String.join(",", currentPath.stream().map(c -> c.name).collect(Collectors.toList())));
        } else {
            currentCave.connections.stream().filter(c -> c.isBigCave() || !currentPath.contains(c))
                    .forEach(dest -> walk(dest, currentPath, differentPaths));
        }

        currentPath.remove(currentPath.size() - 1);
    }


    private static Cave getOrCreate(Map<String, Cave> map, String caveName) {
        if (!map.containsKey(caveName)) {
            map.put(caveName, Cave.of(caveName));
        }
        return map.get(caveName);
    }
}
