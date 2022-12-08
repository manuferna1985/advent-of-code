package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class D10_exercise_Part1 {

    public static class Chunk {
        String left;
        String right;
        Long pointsIfIllegal;

        public static Chunk of(String left, String right, Long pointsIfIllegal) {
            Chunk c = new Chunk();
            c.left = left;
            c.right = right;
            c.pointsIfIllegal = pointsIfIllegal;
            return c;
        }

        public String toString(){
            return left;
        }
    }

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D10_exercise_Part1.class.getResource("2021/D10_full.txt").toURI()), Charset.defaultCharset());

        List<Chunk> allowedChunks = List.of(
                Chunk.of("(", ")", 3L),
                Chunk.of("[", "]", 57L),
                Chunk.of("{", "}", 1197L),
                Chunk.of("<", ">", 25137L)
        );

        Map<String, Chunk> chunksMap = new HashMap<>();
        allowedChunks.forEach(c -> {
            chunksMap.put(c.left, c);
            chunksMap.put(c.right, c);
        });


        Stack<Chunk> sequence;

        Long totalPoints = 0L;

        for (String line : allLines) {
            sequence = new Stack<>();

            for (String letter : line.split("")) {
                Chunk c = chunksMap.get(letter);
                if (letter.equals(c.left)) {
                    // Open
                    sequence.push(c);
                } else {
                    // Close
                    if (sequence.peek().equals(c)) {
                        sequence.pop();
                    } else {
                        totalPoints += c.pointsIfIllegal;
                        break;
                    }
                }
            }
        }

        System.out.println("Total points: " + totalPoints);
    }
}
