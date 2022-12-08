package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class D15_exercise_Part2_Improved {

    public static class Node {
        int i;
        int j;
        int weight;
        boolean covered;
        int minDistance;

        public Node(int i, int j, int weight) {
            this.i = i;
            this.j = j;
            this.weight = weight;
            this.covered = false;
            this.minDistance = Integer.MAX_VALUE;
        }
    }

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D15_exercise_Part2_Improved.class.getResource("2021/D15_full.txt").toURI()), Charset.defaultCharset());

        int[][] matrix1 = new int[allLines.size()][allLines.get(0).length()];

        String line;
        for (int i = 0; i < allLines.size(); i++) {
            line = allLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                matrix1[i][j] = Integer.parseInt(String.valueOf(line.charAt(j)));
            }
        }

        List<Node> allNodes = new ArrayList<>();
        Node[][] matrix = calculateNewMatrix(matrix1, allNodes);

        long l1 = System.currentTimeMillis();

        matrix[matrix.length - 1][matrix[0].length - 1].minDistance = 0;

        int max = matrix.length * matrix[0].length;
        for (int n = 0; n < max; n++) {
            // Nodo no visitado con la menor distancia minima.
            allNodes.stream().filter(node -> !node.covered).min(Comparator.comparing(o -> o.minDistance))
                    .ifPresent(node -> step(matrix, node));
        }

        long l2 = System.currentTimeMillis();

        System.out.println(matrix[0][0].minDistance - matrix[0][0].weight + matrix[matrix.length - 1][matrix[0].length - 1].weight);
        System.out.printf("Time %dms%n", (l2 - l1));
    }

    private static Node[][] calculateNewMatrix(int[][] matrix, List<Node> allNodes) {

        Node[][] newMatrix = new Node[matrix.length * 5][matrix[0].length * 5];

        for (int a = 0; a < 5; a++) {
            for (int b = 0; b < 5; b++) {
                int increment = a + b;
                for (int i = 0; i < matrix.length; i++) {
                    for (int j = 0; j < matrix[i].length; j++) {
                        int x = i + (a * matrix.length);
                        int y = j + (b * matrix[i].length);
                        newMatrix[x][y] = new Node(x, y, addWithMod(matrix[i][j] + increment));
                        allNodes.add(newMatrix[x][y]);
                    }
                }
            }
        }
        return newMatrix;
    }

    private static int addWithMod(int currentValue) {
        if (currentValue > 9) {
            return currentValue - 9;
        }
        return currentValue;
    }

    private static void step(Node[][] matrix, Node node) {

        if (node.i > 0) {
            recalculateMin(node, matrix[node.i - 1][node.j]);
        }

        if (node.i < matrix.length - 1) {
            recalculateMin(node, matrix[node.i + 1][node.j]);
        }

        if (node.j > 0) {
            recalculateMin(node, matrix[node.i][node.j - 1]);
        }

        if (node.j < matrix.length - 1) {
            recalculateMin(node, matrix[node.i][node.j + 1]);
        }

        node.covered = true;
    }

    private static void recalculateMin(Node current, Node neightbour){
        neightbour.minDistance = Math.min(neightbour.minDistance, current.minDistance + neightbour.weight);
    }
}
