package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class D15_exercise_Part2 {

    public static class Node{

        int i;
        int j;

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

    public static class NodeDetails {

        boolean covered;
        Long minDistance;

        public NodeDetails(){
            covered = false;
            minDistance = Long.MAX_VALUE;
        }

        public NodeDetails(Long minDistance) {
            this.minDistance = minDistance;
        }
    }

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D15_exercise_Part2.class.getResource("2021/D15_full.txt").toURI()), Charset.defaultCharset());

        int[][] matrix1 = new int[allLines.size()][allLines.get(0).length()];

        String line;
        for (int i = 0; i < allLines.size(); i++) {
            line = allLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                matrix1[i][j] = Integer.parseInt(String.valueOf(line.charAt(j)));
            }
        }

        int[][] matrix = calculateNewMatrix(matrix1);

        long l1 = System.currentTimeMillis();


        Map<Node, NodeDetails> minDistances = new HashMap<>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                minDistances.put(new Node(i, j), new NodeDetails());
            }
        }

        Node last = new Node(matrix.length - 1, matrix[0].length - 1);
        minDistances.put(last, new NodeDetails(0L));

        do {
            // Nodo no visitado con la menor distancia minima.
            minDistances.entrySet().stream().filter(entry -> !entry.getValue().covered).min((o1, o2) -> o1.getValue().minDistance.compareTo(o2.getValue().minDistance))
                    .ifPresent(nodeLongEntry -> step(matrix, nodeLongEntry.getKey().i, nodeLongEntry.getKey().j, minDistances));

        } while (minDistances.entrySet().stream().anyMatch(entry -> !entry.getValue().covered));

        long l2 = System.currentTimeMillis();

        System.out.println(minDistances.get(new Node(0, 0)).minDistance - matrix[0][0] + matrix[matrix.length - 1][matrix[0].length - 1]);

        System.out.printf("Time %dms%n", (l2 - l1));
    }

    private static int[][] calculateNewMatrix(int[][] matrix) {

        int[][] newMatrix = new int[matrix.length * 5][matrix[0].length * 5];

        for (int a=0; a < 5; a++){
            for (int b=0; b < 5; b++){
                int increment = a + b;
                for (int i=0; i < matrix.length; i++){
                    for (int j=0; j < matrix[i].length; j++){
                        newMatrix[i+(a*matrix.length)][j+(b*matrix[i].length)] = addWithMod(matrix[i][j] + increment);
                    }
                }
            }
        }
        return newMatrix;
    }

    private static int addWithMod(int currentValue){
        if (currentValue > 9){
            return currentValue - 9;
        }
        return currentValue;
    }

    private static void step(int[][] matrix, int i, int j, Map<Node, NodeDetails> minDistances) {

        Node p = new Node(i, j);
        NodeDetails currentDetails = minDistances.get(p);

        if (i > 0) {
            // i-1  j
            Node pNeightbour = new Node(i - 1, j);
            NodeDetails details = minDistances.get(pNeightbour);
            details.minDistance = Math.min(details.minDistance, currentDetails.minDistance + matrix[i-1][j]);
        }

        if (i < matrix.length - 1) {
            // i+1  j
            Node pNeightbour = new Node(i + 1, j);
            NodeDetails details = minDistances.get(pNeightbour);
            details.minDistance = Math.min(details.minDistance, currentDetails.minDistance + matrix[i+1][j]);
        }

        if (j > 0) {
            // i  j-1
            Node pNeightbour = new Node(i, j - 1);
            NodeDetails details = minDistances.get(pNeightbour);
            details.minDistance = Math.min(details.minDistance, currentDetails.minDistance + matrix[i][j-1]);
        }

        if (j < matrix.length - 1) {
            // i  j+1
            Node pNeightbour = new Node(i, j + 1);
            NodeDetails details = minDistances.get(pNeightbour);
            details.minDistance = Math.min(details.minDistance, currentDetails.minDistance + matrix[i][j+1]);
        }

        currentDetails.covered = true;
    }
}
