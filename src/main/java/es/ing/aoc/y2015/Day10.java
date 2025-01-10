package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class Day10 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    final AtomicReference<String> seq = new AtomicReference<>(fileContents.split(System.lineSeparator())[0]);
    IntStream.rangeClosed(1, 40).forEach(i -> seq.set(nextSequence(seq.get())));
    return String.valueOf(seq.get().length());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    final AtomicReference<String> seq = new AtomicReference<>(fileContents.split(System.lineSeparator())[0]);
    IntStream.rangeClosed(1, 50).forEach(i -> seq.set(nextSequence(seq.get())));
    return String.valueOf(seq.get().length());
  }

  private String nextSequence(String seq) {
    StringBuilder newSeq = new StringBuilder();

    Pair<Character, Integer> current = null;
    for (int i=0; i<seq.length(); i++){
      char c = seq.charAt(i);
      if (current == null){
        current = Pair.of(c, 1);
      } else if (current.getKey().equals(c)){
        current = Pair.of(c, current.getValue()+1);
      } else {
        newSeq.append(current.getValue());
        newSeq.append(current.getKey());
        current = Pair.of(c, 1);
      }
    }
    newSeq.append(current.getValue());
    newSeq.append(current.getKey());
    return newSeq.toString();
  }

  public static void main(String[] args) {
    Day.run(Day10::new, "2015/D10_small.txt", "2015/D10_full.txt");
  }
}
