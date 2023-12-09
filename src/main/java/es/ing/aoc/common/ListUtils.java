package es.ing.aoc.common;

import java.util.List;

public class ListUtils {

  private ListUtils() {
    throw new RuntimeException("Constructor not meant to be called");
  }

  public static <T> T first(List<T> list) {
    return list.get(0);
  }

  public static <T> T last(List<T> list) {
    return list.get(list.size() - 1);
  }
}
