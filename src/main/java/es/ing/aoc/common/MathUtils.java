package es.ing.aoc.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
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

    public static <T> IntSummaryStatistics getStats(List<T> cubes, ToIntFunction<T> fn) {
        return cubes.stream().map(fn::applyAsInt).collect(Collectors.summarizingInt(Integer::intValue));
    }
}
