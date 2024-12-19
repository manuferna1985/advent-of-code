package es.ing.aoc.common;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class MemoizationUtils {

  public static void main(String[] args) {
    long n = 15;
    long fact;

    BiFunction<Long, BiFunction, Long> fn = MemoizationUtils::factorial;
    fact = executeTimed(() -> fn.apply(n, fn));
    System.out.println("The result obtained was: " + fact);
    fact = executeTimed(() -> fn.apply(n, fn));
    System.out.println("The result obtained was: " + fact);

    BiFunction<Long, BiFunction, Long> memFn = memoize(fn);
    fact = executeTimed(() -> memFn.apply(n, memFn));
    System.out.println("The result obtained was: " + fact);
    fact = executeTimed(() -> memFn.apply(n, memFn));
    System.out.println("The result obtained was: " + fact);
  }

  public static Long factorial(Long n, BiFunction<Long, BiFunction, Long> fn) {
    if (n <= 1) {
      return 1L;
    } else {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return n * fn.apply(n - 1, fn);
    }
  }

  public static <T, R> BiFunction<T, BiFunction, R> memoize(BiFunction<T, BiFunction, R> function) {
    Map<T, R> cache = new HashMap<>();

    return (input, bfn) -> {
      if (cache.containsKey(input)) {
        return cache.get(input);
      }
      R result = function.apply(input, bfn);
      cache.put(input, result);
      return result;
    };
  }

  private static <R> R executeTimed(Supplier<R> method) {
    long startTime = System.currentTimeMillis();
    R result = method.get();
    long endTime = System.currentTimeMillis();
    System.out.println("That took " + (endTime - startTime) + " milliseconds");
    return result;
  }
}
