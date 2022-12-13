package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.IntUnaryOperator;

public class Day8 extends Day {

    private static final IntUnaryOperator EQUAL = a -> a;
    private static final IntUnaryOperator MINUS = a -> a - 1;
    private static final IntUnaryOperator PLUS = a -> a + 1;

    private List<Integer> getTrees(int[][] matrix, int x, int y, IntUnaryOperator xModifier, IntUnaryOperator yModifier, boolean stopWhenTaller) {
        List<Integer> trees = new ArrayList<>();
        for (int i = xModifier.applyAsInt(x), j = yModifier.applyAsInt(y);
             i >= 0 && j >= 0 && i <= matrix.length - 1 && j <= matrix[0].length - 1;
             i = xModifier.applyAsInt(i), j = yModifier.applyAsInt(j)) {
            int otherTree = matrix[i][j];
            trees.add(otherTree);
            if (stopWhenTaller && otherTree >= matrix[x][y]) {
                break;
            }
        }
        return trees;
    }

    private List<Integer> getUpTrees(int[][] matrix, int x, int y, boolean stopWhenTaller) {
        return getTrees(matrix, x, y, MINUS, EQUAL, stopWhenTaller);
    }

    private List<Integer> getBottomTrees(int[][] matrix, int x, int y, boolean stopWhenTaller) {
        return getTrees(matrix, x, y, PLUS, EQUAL, stopWhenTaller);
    }

    private List<Integer> getLeftTrees(int[][] matrix, int x, int y, boolean stopWhenTaller) {
        return getTrees(matrix, x, y, EQUAL, MINUS, stopWhenTaller);
    }

    private List<Integer> getRightTrees(int[][] matrix, int x, int y, boolean stopWhenTaller) {
        return getTrees(matrix, x, y, EQUAL, PLUS, stopWhenTaller);
    }

    private boolean isTreeVisible(int[][] matrix, int x, int y) {
        boolean visible = false;
        if (x == 0 || y == 0 || x == matrix.length - 1 || y == matrix[0].length - 1) {
            visible = true;
        } else {
            int me = matrix[x][y];

            visible = Collections.max(getUpTrees(matrix, x, y, false)) < me
                    || Collections.max(getBottomTrees(matrix, x, y, false)) < me
                    || Collections.max(getLeftTrees(matrix, x, y, false)) < me
                    || Collections.max(getRightTrees(matrix, x, y, false)) < me;
        }
        return visible;
    }

    private int getScenicScore(int[][] matrix, int x, int y) {
        return getUpTrees(matrix, x, y, true).size()
                * getBottomTrees(matrix, x, y, true).size()
                * getLeftTrees(matrix, x, y, true).size()
                * getRightTrees(matrix, x, y, true).size();
    }

    private int[][] readMatrixFromFile(String fileContents) {
        List<String> allLines = Arrays.asList(fileContents.split(System.lineSeparator())); // when input file is multiline
        int[][] matrix = new int[allLines.size()][allLines.get(0).length()];

        String line;
        for (int i = 0; i < allLines.size(); i++) {
            line = allLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                matrix[i][j] = Integer.parseInt(String.valueOf(line.charAt(j)));
            }
        }
        return matrix;
    }

    @Override
    protected String part1(String fileContents) throws Exception {
        int[][] matrix = readMatrixFromFile(fileContents);
        int visibleTrees = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (isTreeVisible(matrix, i, j)) {
                    visibleTrees++;
                }
            }
        }
        return String.valueOf(visibleTrees);
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        int[][] matrix = readMatrixFromFile(fileContents);
        int maxScenicScore = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                maxScenicScore = Math.max(maxScenicScore, getScenicScore(matrix, i, j));
            }
        }
        return String.valueOf(maxScenicScore);
    }

    public static void main(String[] args) {
        Day.run(Day8::new, "2022/D8_small.txt", "2022/D8_full.txt");
    }
}
