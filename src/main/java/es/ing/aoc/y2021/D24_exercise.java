package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

public class D24_exercise {

    private static final Long MAX_MODEL_NUMBER = 99999999999999L;

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D19_exercise.class.getResource("2021/D24_full.txt").toURI()), Charset.defaultCharset());

        // Start coding here. ;-)
        long before = System.nanoTime();

        AtomicLong largestResult = new AtomicLong(0L);

        LongStream.range(11111111111111L, 99999999999999L).parallel()
                .forEach(value -> {
                    String modelNumberStr = String.format("%1$14s", value).replace(' ', '0');

                    if (!modelNumberStr.contains("0")) {
                        if (processInput(allLines, modelNumberStr)) {
                            if (value > largestResult.get()) {
                                synchronized (largestResult) {
                                    System.out.println("LARGEST VALID!: " + modelNumberStr);
                                    largestResult.set(value);
                                }
                            }
                        }
                    }
                });

        System.out.println(largestResult.get());

        long after = System.nanoTime();

        System.out.printf("Elapsed time: %.0f ms%n%n", ((double) (after - before)) / 1000000);
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
