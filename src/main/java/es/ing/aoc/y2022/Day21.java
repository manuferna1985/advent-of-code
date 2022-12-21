package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Range;

public class Day21 extends Day {

    private static final Pattern COMPLEX_MONKEY = Pattern.compile("([a-z]+): ([a-z]+) ([+\\-*/]) ([a-z]+)");
    private static final Pattern SIMPLE_MONKEY = Pattern.compile("([a-z]+): ([0-9]+)");
    private static final String ROOT = "root";
    private static final String HUMAN = "humn";

    static class Operation {
        String firstMonkey;
        BinaryOperator<Long> function;
        String secondMonkey;

        public Operation(String firstMonkey, BinaryOperator<Long> function, String secondMonkey) {
            this.firstMonkey = firstMonkey;
            this.secondMonkey = secondMonkey;
            this.function = function;
        }

        public List<String> getMonkeys() {
            return List.of(firstMonkey, secondMonkey);
        }
    }

    @Override
    protected String part1(String fileContents) throws Exception {
        Map<String, Operation> pendingMonkeys = new HashMap<>();
        Map<String, Long> finishedMonkeys = new HashMap<>();

        readMonkeysData(fileContents, pendingMonkeys, finishedMonkeys);
        guessMonkeyNumbers(pendingMonkeys, finishedMonkeys, false);

        return String.valueOf(finishedMonkeys.get("root"));
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        Map<String, Operation> pendingMonkeys = new HashMap<>();
        Map<String, Long> finishedMonkeys = new HashMap<>();

        readMonkeysData(fileContents, pendingMonkeys, finishedMonkeys);

        Range<Long> limits = Range.between(-100000000000000L, 100000000000000L);

        while (true) {
            // Binary Search            
            finishedMonkeys.put(HUMAN, limits.getMinimum());
            int resultL = guessMonkeyNumbers(new HashMap<>(pendingMonkeys), new HashMap<>(finishedMonkeys), true);
            if (resultL == 0) {
                return String.valueOf(finishedMonkeys.get(HUMAN));
            }

            finishedMonkeys.put(HUMAN, limits.getMaximum());
            int resultH = guessMonkeyNumbers(new HashMap<>(pendingMonkeys), new HashMap<>(finishedMonkeys), true);
            if (resultH == 0) {
                return String.valueOf(finishedMonkeys.get(HUMAN));
            }

            Long middleLimit = limits.getMinimum() + ((limits.getMaximum() - limits.getMinimum()) / 2);
            finishedMonkeys.put(HUMAN, middleLimit);
            int resultM = guessMonkeyNumbers(new HashMap<>(pendingMonkeys), new HashMap<>(finishedMonkeys), true);
            if (resultM == 0) {
                return String.valueOf(finishedMonkeys.get(HUMAN));
            }

            if (resultL == resultM) {
                limits = Range.between(middleLimit, limits.getMaximum());
            } else {
                limits = Range.between(limits.getMinimum(), middleLimit);
            }
        }
    }

    private int guessMonkeyNumbers(Map<String, Operation> pendingMonkeys, Map<String, Long> finishedMonkeys, boolean rootEqualCheck) {
        while (!pendingMonkeys.isEmpty()) {
            List<String> possibleMonkeys = pendingMonkeys.entrySet().stream()
                    .filter(entry -> entry.getValue().getMonkeys().stream().allMatch(finishedMonkeys::containsKey))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            for (String m : possibleMonkeys) {
                Operation op = pendingMonkeys.get(m);

                if (rootEqualCheck && ROOT.equals(m)) {
                    return Long.compare(finishedMonkeys.get(op.firstMonkey), finishedMonkeys.get(op.secondMonkey));
                } else {
                    finishedMonkeys.put(m, op.function.apply(finishedMonkeys.get(op.firstMonkey), finishedMonkeys.get(op.secondMonkey)));
                    pendingMonkeys.remove(m);
                }
            }
        }

        return 0;
    }

    private void readMonkeysData(String fileContents, Map<String, Operation> pendingMonkeys, Map<String, Long> finishedMonkeys) {
        String[] monkeys = fileContents.split(System.lineSeparator()); // when input file is multiline

        Matcher matcher;
        for (String m : monkeys) {
            matcher = COMPLEX_MONKEY.matcher(m);

            if (matcher.find()) {
                // Complex
                pendingMonkeys.put(matcher.group(1), new Operation(matcher.group(2), convertToFunction(matcher.group(3)), matcher.group(4)));
            } else {
                matcher = SIMPLE_MONKEY.matcher(m);

                if (matcher.find()) {
                    // Simple
                    finishedMonkeys.put(matcher.group(1), Long.parseLong(matcher.group(2)));
                } else {
                    throw new RuntimeException("Wrong input detected!");
                }
            }
        }
    }

    private BinaryOperator<Long> convertToFunction(String op) {
        switch (op) {
            case "+":
                return (a, b) -> a + b;
            case "-":
                return (a, b) -> a - b;
            case "*":
                return (a, b) -> a * b;
            case "/":
                return (a, b) -> a / b;
            default:
                throw new RuntimeException("Wrong operation detected!");
        }
    }

    public static void main(String[] args) {
        Day.run(Day21::new, "2022/D21_small.txt", "2022/D21_full.txt");
    }
}
