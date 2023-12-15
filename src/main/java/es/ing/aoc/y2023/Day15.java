package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Day15 extends Day {

  private static final char DASH = '-';
  private static final char EQUAL = '=';

  @Override
  protected String part1(String fileContents) throws Exception {
    String[] words = fileContents.split(",");
    return String.valueOf(Arrays.stream(words).map(this::getWordHash).mapToInt(Integer::valueOf).sum());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] words = fileContents.split(",");

    Map<Integer, Map<String, Integer>> boxes = new HashMap<>();

    for (String w : words){
      int op = w.indexOf(DASH) + w.indexOf(EQUAL) + 1;
      String label = w.substring(0, op);
      Integer boxNumber = getWordHash(label);

      if (!boxes.containsKey(boxNumber)){
        boxes.put(boxNumber, new LinkedHashMap<>());
      }
      Map<String, Integer> currentBox = boxes.get(boxNumber);

      switch(w.charAt(op)){
        case DASH -> currentBox.remove(label);
        case EQUAL -> currentBox.put(label, Integer.parseInt(w.substring(op+1, op+2)));
        default -> throw new IllegalStateException("Unexpected value: " + w.charAt(op));
      }
    }

    int focusingPower = 0;
    for (Map.Entry<Integer, Map<String, Integer>> box : boxes.entrySet()){
      int i=1;
      for (Integer power : box.getValue().values()){
        focusingPower += (box.getKey() + 1) * i * power;
        i++;
      }
    }
    return String.valueOf(focusingPower);
  }

  private int getWordHash(String word){
    final AtomicInteger hash = new AtomicInteger();
    word.chars().boxed().forEach(c -> hash.set(((hash.get() + c ) * 17) % 256));
    return hash.get();
  }

  public static void main(String[] args) {
    Day.run(Day15::new, "2023/D15_small.txt", "2023/D15_full.txt");
  }
}
