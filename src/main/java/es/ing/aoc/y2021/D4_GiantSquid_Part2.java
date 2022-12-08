package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class D4_GiantSquid_Part2 {

    private static final Integer MARK = Integer.MAX_VALUE;

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D4_GiantSquid_Part2.class.getResource("2021/D4_full.txt").toURI()), Charset.defaultCharset());

        List<Integer> numbers = Arrays.stream(allLines.get(0).split(","))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList());

        List<Integer[][]> boards = new ArrayList<>();

        int i = 2;

        while (i < allLines.size() - 1) {

            Integer[][] board = new Integer[5][];
            for (int j = 0; j < 5; j++) {
                board[j] = Arrays.stream(allLines.get(i++)
                                .trim()
                                .split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .boxed()
                        .toArray(Integer[]::new);
            }
            boards.add(board);
            i++;
        }

        // Bingo system
        List<Integer> finishedBoards = new ArrayList<>();

        for (Integer n : numbers) {
            for (int index = 0; index < boards.size(); index++) {
                System.out.printf("Marking board %d with %d%n", index, n);
                markBoard(boards.get(index), n);
                if (!finishedBoards.contains(index) && hasBoardRowOrColumnFinished(boards.get(index))) {
                    System.out.println("The winner is: " + index);
                    System.out.println("Total score is: " + getUnmarkedNumbersSum(boards.get(index)) * n);
                    finishedBoards.add(index);

                    if (finishedBoards.size() == boards.size()){
                        System.exit(0);
                    }
                }
            }
        }

        System.out.println("end");
    }

    private static void markBoard(Integer[][] board, Integer n) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equals(n)) {
                    board[i][j] = MARK;
                }
            }
        }
    }

    private static boolean hasBoardRowOrColumnFinished(Integer[][] board) {
        return Arrays.stream(board).anyMatch(row -> Arrays.stream(row).allMatch(MARK::equals)) ||
                Arrays.stream(transposeMatrix(board)).anyMatch(row -> Arrays.stream(row).allMatch(MARK::equals));
    }

    private static Integer getUnmarkedNumbersSum(Integer[][] board) {
        return Arrays.stream(board)
                .map(row -> Arrays.stream(row)
                        .filter(n -> !MARK.equals(n))
                        .mapToInt(Integer::intValue).sum())
                .mapToInt(Integer::intValue).sum();
    }

    private static Integer[][] transposeMatrix(Integer[][] m) {
        Integer[][] temp = new Integer[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                temp[j][i] = m[i][j];
            }
        }
        return temp;
    }
}
