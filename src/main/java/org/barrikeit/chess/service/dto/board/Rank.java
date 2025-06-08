package org.barrikeit.chess.service.dto.board;

import java.util.Arrays;

public enum Rank {
  ONE("1"),
  TWO("2"),
  THREE("3"),
  FOUR("4"),
  FIVE("5"),
  SIX("6"),
  SEVEN("7"),
  EIGHT("8");

  public final String notation;

  Rank(String notation) {
    this.notation = notation;
  }

  public static Rank from(String notation) {
    return Arrays.stream(values())
        .filter(r -> r.notation.equals(notation))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid rank notation: " + notation));
  }

  public static Rank from(int value) {
    return values()[value - 1];
  }
}
