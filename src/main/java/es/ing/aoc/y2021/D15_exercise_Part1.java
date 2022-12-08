package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class D15_exercise_Part1 {

    public static class Node{

        int i;
        int j;
        boolean covered;

        public Node(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return i == node.i && j == node.j;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }
    }

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D15_exercise_Part1.class.getResource("2021/D15_small.txt").toURI()), Charset.defaultCharset());

        int[][] matrix = new int[allLines.size()][allLines.get(0).length()];

        String line;
        for (int i = 0; i < allLines.size(); i++) {
            line = allLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                matrix[i][j] = Integer.parseInt(String.valueOf(line.charAt(j)));
            }
        }

        long l1 = System.currentTimeMillis();


        Map<Node, Long> minDistances = new HashMap<>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                minDistances.put(new Node(i, j), Long.MAX_VALUE);
            }
        }

        Node last = new Node(matrix.length - 1, matrix[0].length - 1);
        minDistances.put(last, 0L);


        do {
            // Nodo no visitado con la menor distancia minima.
            minDistances.entrySet().stream().filter(entry -> !entry.getKey().covered).min((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                    .ifPresent(nodeLongEntry -> step(matrix, nodeLongEntry.getKey().i, nodeLongEntry.getKey().j, minDistances));

        } while (minDistances.entrySet().stream().anyMatch(entry -> !entry.getKey().covered));

        long l2 = System.currentTimeMillis();

        System.out.println(minDistances.get(new Node(0, 0)) - matrix[0][0] + matrix[matrix.length - 1][matrix[0].length - 1]);

        System.out.printf("Time %dms%n", (l2 - l1));
    }

    private static void step(int[][] matrix, int i, int j, Map<Node, Long> minDistances) {

        Node p = new Node(i, j);
        Long currentMin = minDistances.get(p);

        if (i > 0) {
            // i-1  j
            Node pNeightbour = new Node(i - 1, j);
            minDistances.put(pNeightbour, Math.min(minDistances.get(pNeightbour), currentMin + matrix[i-1][j]));
        }

        if (i < matrix.length - 1) {
            // i+1  j
            Node pNeightbour = new Node(i + 1, j);
            minDistances.put(pNeightbour, Math.min(minDistances.get(pNeightbour), currentMin + matrix[i+1][j]));
        }

        if (j > 0) {
            // i  j-1
            Node pNeightbour = new Node(i, j - 1);
            minDistances.put(pNeightbour, Math.min(minDistances.get(pNeightbour), currentMin + matrix[i][j-1]));
        }

        if (j < matrix.length - 1) {
            // i  j+1
            Node pNeightbour = new Node(i, j + 1);
            minDistances.put(pNeightbour, Math.min(minDistances.get(pNeightbour), currentMin + matrix[i][j+1]));
        }

        p.covered = true;
        minDistances.remove(p);
        minDistances.put(p, currentMin);
    }
}
