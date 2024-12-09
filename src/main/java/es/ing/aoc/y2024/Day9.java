package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Day9 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    String line = fileContents.split(System.lineSeparator())[0];
    List<String> disk = getDiskContent(line);
    List<Integer> optimizedDisk = optimizeDisk(disk);
    Long checksum = calculateChecksum(optimizedDisk);
    return String.valueOf(checksum);
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String line = fileContents.split(System.lineSeparator())[0];
    List<Pair<String, Integer>> disk = getDiskContentGrouped(line);
    List<Integer> optimizedDisk = optimizeGroupedDisk(disk);
    Long checksum = calculateChecksum(optimizedDisk);
    return String.valueOf(checksum);
  }

  private Long calculateChecksum(List<Integer> optimizedDisk) {
    long checksum = 0;
    for (int i = 0; i < optimizedDisk.size(); i++) {
      checksum += i * optimizedDisk.get(i);
    }
    return checksum;
  }

  private List<Integer> optimizeDisk(List<String> disk) {

    Stack<Pair<Integer, Integer>> blockStack = new Stack<>();
    LinkedList<Integer> emptyQueue = new LinkedList<>();

    for (int i = 0; i < disk.size(); i++) {
      if (!disk.get(i).equals(".")) {
        Pair<Integer, Integer> block = Pair.of(i, Integer.parseInt(disk.get(i)));
        blockStack.push(block);
      } else {
        emptyQueue.add(i);
      }
    }

    List<Pair<Integer, Integer>> rearrangedBlocks = new ArrayList<>();
    boolean end = false;
    while (!emptyQueue.isEmpty() && !end) {
      int firstEmpty = emptyQueue.pollFirst();
      if (firstEmpty < blockStack.peek().getKey()) {
        Pair<Integer, Integer> lastBlock = blockStack.pop();
        rearrangedBlocks.add(Pair.of(firstEmpty, lastBlock.getValue()));
      } else {
        end = true;
      }
    }

    rearrangedBlocks.addAll(blockStack);
    rearrangedBlocks.sort(Comparator.comparingInt(Pair::getKey));
    return rearrangedBlocks.stream().map(Pair::getValue).toList();
  }

  private static List<String> getDiskContent(String line) {
    List<String> disk = new ArrayList<>();
    int id = 0;
    for (int i = 0; i < line.length(); i++) {
      int number = Integer.parseInt(line.charAt(i) + "");
      for (int j = 0; j < number; j++) {
        if (i % 2==0) {
          disk.add(String.valueOf(id));
        } else {
          disk.add(".");
        }
      }
      if (i % 2==0) {
        id++;
      }
    }
    return disk;
  }

  private List<Integer> optimizeGroupedDisk(List<Pair<String, Integer>> disk) {
    LinkedList<Pair<String, Integer>> linkedDisk = new LinkedList<>(disk);
    Stack<Pair<String, Integer>> processed = new Stack<>();

    while (!linkedDisk.isEmpty()) {
      Pair<String, Integer> lastElement = linkedDisk.pollLast();

      if (lastElement.getKey().equals(".")) {
        processed.push(lastElement);
      } else {

        boolean moved = false;
        int index = 0;
        for (Pair<String, Integer> elem : linkedDisk) {
          if (elem.getKey().equals(".") && elem.getValue() >= lastElement.getValue()) {

            Pair<String, Integer> hole = linkedDisk.remove(index);
            linkedDisk.add(index, lastElement);
            processed.push(Pair.of(".", lastElement.getValue()));

            if (hole.getValue() > lastElement.getValue()) {
              linkedDisk.add(index + 1, Pair.of(hole.getKey(), hole.getValue() - lastElement.getValue()));
            }
            moved = true;
            break;
          }
          index++;
        }

        if (!moved) {
          processed.push(lastElement);
        }
      }
    }

    List<Integer> result = new ArrayList<>();

    while (!processed.empty()) {
      var elem = processed.pop();
      for (int i = 0; i < elem.getValue(); i++) {
        if (!elem.getKey().equals(".")) {
          result.add(Integer.parseInt(elem.getKey()));
        } else {
          result.add(0);
        }
      }
    }

    return result;
  }

  private static List<Pair<String, Integer>> getDiskContentGrouped(String line) {
    List<Pair<String, Integer>> disk = new ArrayList<>();
    int id = 0;
    for (int i = 0; i < line.length(); i++) {
      int number = Integer.parseInt(line.charAt(i) + "");
      if (number > 0) {
        if (i % 2==0) {
          disk.add(Pair.of(String.valueOf(id), number));
        } else {
          disk.add(Pair.of(".", number));
        }
      }
      if (i % 2==0) {
        id++;
      }
    }
    return disk;
  }

  public static void main(String[] args) {
    Day.run(Day9::new, "2024/D9_small.txt", "2024/D9_full.txt");
  }
}
