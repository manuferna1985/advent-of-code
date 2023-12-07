package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static es.ing.aoc.y2023.Day7.HandType.*;
import static java.util.stream.Collectors.groupingBy;

public class Day7 extends Day {

  public enum Card {
    A(14), K(13), Q(12), J(11), T(10), R9, R8, R7, R6, R5, R4, R3, R2, X(1);

    private final char letter;
    private final int value;

    Card() {
      this.letter = this.name().charAt(1);
      this.value = Integer.parseInt(String.valueOf(this.letter));
    }

    Card(int value) {
      this.letter = this.name().charAt(0);
      this.value = value;
    }

    public String getFixedLengthValue() {
      return StringUtils.leftPad(String.valueOf(value), 2, "0");
    }

    public static Card of(char letter) {
      for (Card c : Card.values()) {
        if (c.letter==letter) {
          return c;
        }
      }
      throw new RuntimeException("Card letter not found!!!");
    }

    @Override
    public String toString() {
      return String.valueOf(value > 9 ? String.valueOf(letter):value);
    }
  }

  enum HandType {
    FIVE_OF_A_KIND(7), FOUR_OF_A_KIND(6), FULL_HOUSE(5), THREE_OF_A_KIND(4), TWO_PAIR(3), ONE_PAIR(2), HIGH_CARD(1);

    private final int value;

    HandType(int value) {
      this.value = value;
    }
  }

  static class Hand implements Comparable<Hand> {

    public final List<Card> cards;
    public final String handValue;

    Hand(List<Card> cards) {
      this.cards = cards;
      handValue = cards.stream()
          .map(Card::getFixedLengthValue)
          .collect(Collectors.joining(StringUtils.EMPTY));
    }

    @Override
    public int compareTo(Hand that) {

      HandType myType = this.getHandType();
      HandType yourType = that.getHandType();

      if (myType.equals(yourType)) {
        return this.handValue.compareTo(that.handValue);
      } else {
        return Integer.compare(myType.value, yourType.value);
      }
    }

    public HandType getHandType() {
      Map<Card, List<Card>> groups = cards.stream().collect(groupingBy(c -> c));

      if (groups.containsKey(Card.X) && groups.size() > 1) {
        List<Card> jokers = groups.remove(Card.X);
        groups.entrySet().stream()
            .max(Comparator.comparingInt(o -> o.getValue().size())).map(Map.Entry::getKey)
            .ifPresent(card -> groups.get(card).addAll(jokers));
      }

      if (groups.size()==1) {
        return FIVE_OF_A_KIND;
      } else if (groups.size()==2) {
        return groups.values().stream().anyMatch(cs -> cs.size()==4) ? FOUR_OF_A_KIND:FULL_HOUSE;
      } else if (groups.size()==3) {
        return groups.values().stream().anyMatch(cs -> cs.size()==3) ? THREE_OF_A_KIND:TWO_PAIR;
      } else if (groups.size()==4) {
        return ONE_PAIR;
      } else {
        return HIGH_CARD;
      }
    }

    public String toString() {
      return this.cards.toString();
    }
  }

  record Bid(Hand hand, Integer bid) implements Comparable<Bid> {
    @Override
    public int compareTo(Bid o) {
      return this.hand.compareTo(o.hand);
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    return getTotalWinnings(fileContents, false);
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return getTotalWinnings(fileContents, true);
  }

  private String getTotalWinnings(String fileContents, boolean isJokerPlaying) {
    String[] lines = fileContents.split(System.lineSeparator());

    List<Bid> bids = Arrays.stream(lines)
        .map(l -> isJokerPlaying ? l.replace(Card.J.letter, Card.X.letter):l)
        .map(this::buildBid)
        .sorted()
        .toList();

    return String.valueOf(IntStream.rangeClosed(0, bids.size() - 1).map(i -> bids.get(i).bid * (i + 1)).sum());
  }

  private Bid buildBid(String line) {
    String[] parts = line.split(StringUtils.SPACE);
    return new Bid(
        new Hand(parts[0].chars().mapToObj(i -> Card.of((char) i)).toList()),
        Integer.parseInt(parts[1]));
  }

  public static void main(String[] args) {
    Day.run(Day7::new, "2023/D7_small.txt", "2023/D7_full.txt");
  }
}
