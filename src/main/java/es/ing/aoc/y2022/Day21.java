package es.ing.aoc.y2022;

import static es.ing.aoc.common.MathUtils.binarySearch;
import static es.ing.aoc.common.MathUtils.convertToFunction;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Range;

public class Day21 extends Day {

    private static final Long MAX_RANGE = 100000000000000L;
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
        guessMonkeyNumbers(pendingMonkeys, finishedMonkeys);
        return String.valueOf(finishedMonkeys.get(ROOT));
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        Map<String, Operation> pendingMonkeys = new HashMap<>();
        Map<String, Long> finishedMonkeys = new HashMap<>();
        readMonkeysData(fileContents, pendingMonkeys, finishedMonkeys);

        return String.valueOf(
                binarySearch(
                        Range.between(-MAX_RANGE, MAX_RANGE),
                        hum -> {
                            finishedMonkeys.put(HUMAN, hum);
                            return guessMonkeyNumbers(new HashMap<>(pendingMonkeys), new HashMap<>(finishedMonkeys));
                        },
                        result -> result == 0)
                        .orElse(-1L));
    }

    private int guessMonkeyNumbers(Map<String, Operation> pendingMonkeys, Map<String, Long> finishedMonkeys) {
        Operation op = null;
        while (!pendingMonkeys.isEmpty()) {
            List<String> possibleMonkeys = pendingMonkeys.entrySet().stream()
                    .filter(entry -> entry.getValue().getMonkeys().stream().allMatch(finishedMonkeys::containsKey))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            for (String m : possibleMonkeys) {
                op = pendingMonkeys.get(m);
                finishedMonkeys.put(m, op.function.apply(finishedMonkeys.get(op.firstMonkey), finishedMonkeys.get(op.secondMonkey)));
                pendingMonkeys.remove(m);
            }
        }
        return op != null ? Long.compare(finishedMonkeys.get(op.firstMonkey), finishedMonkeys.get(op.secondMonkey)) : 0;
    }

    private void readMonkeysData(String fileContents, Map<String, Operation> pendingMonkeys, Map<String, Long> finishedMonkeys) {
        String[] monkeys = fileContents.split(System.lineSeparator()); // when input file is multiline

        List<Pair<Pattern, Consumer<Matcher>>> patterns = List.of(
                Pair.of(COMPLEX_MONKEY, matcher -> pendingMonkeys.put(matcher.group(1), new Operation(matcher.group(2), convertToFunction(matcher.group(3)), matcher.group(4)))),
                Pair.of(SIMPLE_MONKEY, matcher -> finishedMonkeys.put(matcher.group(1), Long.parseLong(matcher.group(2)))));

        Matcher matcher;
        for (String m : monkeys) {
            for (Pair<Pattern, Consumer<Matcher>> patternAndConsumer : patterns) {
                matcher = patternAndConsumer.a.matcher(m);
                if (matcher.find()) {
                    patternAndConsumer.b.accept(matcher);
                }
            }
        }
    }

    public static void main(String[] args) {
        Day.run(Day21::new, "2022/D21_small.txt", "2022/D21_full.txt");
    }
}
