package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class D24_exercise_2 {

    private static final Integer[] A = {12, 10, 10, -6, 11, -12, 11, 12, 12, -2, -5, -4, -4, -12};
    private static final Integer[] B = {1, 1, 1, 26, 1, 26, 1, 1, 1, 26, 26, 26, 26, 26};
    private static final Integer[] C = {6, 2, 13, 8, 13, 8, 3, 11, 10, 8, 14, 6, 8, 2};
    private static final long[] MAX_Z = {8031810176L, 8031810176L, 8031810176L, 8031810176L, 308915776, 308915776, 11881376, 11881376, 11881376, 11881376, 456976, 17576, 676, 26};

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D19_exercise.class.getResource("2021/D24_full.txt").toURI()), Charset.defaultCharset());

        // Start coding here. ;-)
        long before = System.nanoTime();

        String[] solution = new String[14];
        search(0, 0, solution);
        boolean valid = processInput(allLines, String.join("", solution));

        System.out.println(String.join("", solution));
        System.out.println(valid);

        long after = System.nanoTime();

        System.out.printf("Elapsed time: %.0f ms%n%n", ((double) (after - before)) / 1000000);
    }

    private static long stage(int n, int w, long z){

        if (z % 26 + A[n] == w){
            return z / B[n];
        } else {
            return 26 * (z / B[n]) + w + C[n];
        }
    }

    private static void search(Integer depth, long z, String[] solution){

        if (depth == 14){
            if (z == 0){
                System.out.println(String.join("", solution));
            }
            return;
        } else if (z >= MAX_Z[depth]){
            return;
        }

        for (int i=9; i >= 1; i--){
            solution[depth] = "" + i;
            search(depth + 1, stage(depth, i, z), solution);
        }
    }

    private static boolean processInput(List<String> aluCommands, String modelNumber) {

        AtomicInteger modelNumberReadIndex = new AtomicInteger(0);

        Map<Character, Long> variables = new HashMap<>();
        variables.put('w', 0L);
        variables.put('x', 0L);
        variables.put('y', 0L);
        variables.put('z', 0L);

        aluCommands.stream().map(cmd -> cmd.split(" ")).forEach(lineParts -> {

            char firstPos = lineParts[1].charAt(0);
            long rightValue = lineParts.length > 2 ? getVarOrFixedValue(lineParts[2], variables) : 0L;

            switch (lineParts[0]) {
                case "inp":
                    long current = Long.parseLong(modelNumber.substring(modelNumberReadIndex.get(), modelNumberReadIndex.incrementAndGet()));
                    variables.put(firstPos, current);
                    break;
                case "add":
                    variables.put(firstPos, variables.get(firstPos) + rightValue);
                    break;
                case "mul":
                    variables.put(firstPos, variables.get(firstPos) * rightValue);
                    break;
                case "div":
                    variables.put(firstPos, variables.get(firstPos) / rightValue);
                    break;
                case "mod":
                    variables.put(firstPos, variables.get(firstPos) % rightValue);
                    break;
                case "eql":
                    variables.put(firstPos, variables.get(firstPos).equals(rightValue) ? 1L : 0L);
                    break;
                default:
                    throw new IllegalArgumentException("wrong alu command");
            }
        });

        return variables.get('z').equals(0L);
    }

    private static Long getVarOrFixedValue(String value, Map<Character, Long> variables) {
        if (variables.containsKey(value.charAt(0))) {
            return variables.get(value.charAt(0));
        } else {
            return Long.parseLong(value);
        }
    }
}
