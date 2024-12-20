package es.ing.aoc.common;

import org.apache.commons.lang3.NotImplementedException;

import java.util.function.IntPredicate;

public class SearchUtils {

  private SearchUtils() {
    throw new NotImplementedException("Constructor not meant to be called!");
  }

  /**
   * Binary search function.
   *
   * @param min              Min index to start the search process (predicate MUST be FALSE here)
   * @param max              Max index to start the search process (predicate MSUT be TRUE here)
   * @param predicateToMatch Predicate used to validate the search process. Will return true/false.
   * @param fetchResult      True when we need the first TRUE index, false when we need the last FALSE one in the received range.
   *                         false false ... false     true ... true true true
   * @return Index of the element where the predicate changes from false to true.
   */
  public static int binarySearch(int min, int max, IntPredicate predicateToMatch, boolean fetchResult) {

    while (true) {
      int limit = (min + max) / 2;
      if (predicateToMatch.test(limit)) {
        max = limit;
      } else {
        min = limit;
      }

      if (min + 1==max) {
        return fetchResult ? max:min;
      }
    }
  }
}
