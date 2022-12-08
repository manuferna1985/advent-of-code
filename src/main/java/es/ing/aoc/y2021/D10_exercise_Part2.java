package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class D10_exercise_Part2 {

    private static final Long MULTIPLIER = 5L;

    public static class Chunk {
        String left;
        String right;
        Long pointsIfIncomplete;

        public static Chunk of(String left, String right, Long pointsIfIncomplete) {
            Chunk c = new Chunk();
            c.left = left;
            c.right = right;
            c.pointsIfIncomplete = pointsIfIncomplete;
            return c;
        }

        public String toString(){
            return left;
        }
    }

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D10_exercise_Part2.class.getResource("2021/D10_full.txt").toURI()), Charset.defaultCharset());

        List<Chunk> allowedChunks = List.of(
                Chunk.of("(", ")", 1L),
                Chunk.of("[", "]", 2L),
                Chunk.of("{", "}", 3L),
                Chunk.of("<", ">", 4L)
        );

        Map<String, Chunk> chunksMap = new HashMap<>();
        allowedChunks.forEach(c -> {
            chunksMap.put(c.left, c);
            chunksMap.put(c.right, c);
        });


        Stack<Chunk> sequence;

        List<Long> totalPoints = new ArrayList<>();

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
                        System.out.println("Wrong line");
                        sequence.clear();
                        break;
                    }
                }
            }

            if (!sequence.isEmpty()){

                Long currentPoints = 0L;

                while (!sequence.isEmpty()){
                    currentPoints *= MULTIPLIER;
                    currentPoints += sequence.pop().pointsIfIncomplete;
                }

                totalPoints.add(currentPoints);
            }
        }

        Collections.sort(totalPoints);
        System.out.println("Total points: " + totalPoints);
        System.out.println("Middle points: " + totalPoints.get((totalPoints.size() - 1)/2));
    }
}
