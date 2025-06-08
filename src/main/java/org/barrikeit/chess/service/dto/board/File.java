package org.barrikeit.chess.service.dto.board;

import java.util.Arrays;

public enum File {
  A("A"),
  B("B"),
  C("C"),
  D("D"),
  E("E"),
  F("F"),
  G("G"),
  H("H");

  public final String notation;

  File(String notation) {
    this.notation = notation;
  }

  public static File from(String notation) {
    return Arrays.stream(values())
        .filter(f -> f.notation.equalsIgnoreCase(notation))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid file notation: " + notation));
  }

  public static File from(int value) {
    return values()[value - 1];
  }
}
