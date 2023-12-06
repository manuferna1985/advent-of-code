package es.ing.aoc.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Range;
import org.apache.commons.math3.util.CombinatoricsUtils;

public class MathUtils {

    private MathUtils() {
        throw new RuntimeException("Constructor not meant to be called");
    }

    public static <T> List<Pair<List<T>, List<T>>> generateCombinationsInPairs(List<T> elements, int n) {
        return generateCombinationsFor(elements, n)
                .stream()
                .map(left -> new Pair<>(left, createOpposite(elements, left)))
                .collect(Collectors.toList());
    }

    public static <T> List<List<T>> generateCombinationsFor(List<T> elements, int n) {
        List<List<T>> combinations = new ArrayList<>();
        Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(elements.size(), n);
        while (iterator.hasNext()) {
            combinations.add(
                    Arrays.stream(iterator.next()).boxed().map(elements::get).collect(Collectors.toList()));
        }
        return combinations;
    }

    public static <T> List<T> cloneWithoutElement(List<T> currentElements, T elementToRemove) {
        return createOpposite(currentElements, List.of(elementToRemove));
    }

    public static <T> List<T> createOpposite(List<T> all, List<T> partial) {
        return all.stream().filter(e -> !partial.contains(e)).collect(Collectors.toList());
    }

    public static <A, B> Map<B, A> invertMap(Map<A, B> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    public static <T> IntSummaryStatistics getStats(Collection<T> cubes, ToIntFunction<T> fn) {
        return cubes.stream().map(fn::applyAsInt).collect(Collectors.summarizingInt(Integer::intValue));
    }

    public static BinaryOperator<Long> convertToFunction(String op) {
      return switch (op) {
        case "+" -> (a, b) -> a + b;
        case "-" -> (a, b) -> a - b;
        case "*" -> (a, b) -> a * b;
        case "/" -> (a, b) -> a / b;
        default -> throw new RuntimeException("Wrong operation detected!");
      };
    }

    public static <T> Optional<Long> binarySearch(Range<Long> rng, Function<Long, T> searchFunction, Predicate<T> resultValidator) {
        Long middleLimit = rng.getMinimum() + ((rng.getMaximum() - rng.getMinimum()) / 2);
        T minResult = searchFunction.apply(rng.getMinimum());
        if (resultValidator.test(minResult)) {
            return Optional.of(rng.getMinimum());
        }
        T maxResult = searchFunction.apply(rng.getMaximum());
        if (resultValidator.test(maxResult)) {
            return Optional.of(rng.getMaximum());
        }
        if (minResult.equals(maxResult)) {
            return Optional.empty();
        } else {
            T midResult = searchFunction.apply(middleLimit);
            if (resultValidator.test(midResult)) {
                return Optional.of(middleLimit);
            }
            if (minResult.equals(midResult)) {
                return binarySearch(Range.between(middleLimit, rng.getMaximum()), searchFunction, resultValidator);
            } else {
                return binarySearch(Range.between(rng.getMinimum(), middleLimit), searchFunction, resultValidator);
            }
        }
    }

    public static  org.apache.commons.lang3.tuple.Pair<Double, Double> solveEquation2ndGrade(double a, double b, double c) {
        double root = Math.sqrt(Math.pow(b, 2) - 4 * a * c);
        return org.apache.commons.lang3.tuple.Pair.of((-b + root) / 2 * a, (-b - root) / 2 * a);
    }
}
