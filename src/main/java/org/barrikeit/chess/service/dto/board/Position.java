package org.barrikeit.chess.service.dto.board;

public enum Position {
  A1,
  B1,
  C1,
  D1,
  E1,
  F1,
  G1,
  H1,
  A2,
  B2,
  C2,
  D2,
  E2,
  F2,
  G2,
  H2,
  A3,
  B3,
  C3,
  D3,
  E3,
  F3,
  G3,
  H3,
  A4,
  B4,
  C4,
  D4,
  E4,
  F4,
  G4,
  H4,
  A5,
  B5,
  C5,
  D5,
  E5,
  F5,
  G5,
  H5,
  A6,
  B6,
  C6,
  D6,
  E6,
  F6,
  G6,
  H6,
  A7,
  B7,
  C7,
  D7,
  E7,
  F7,
  G7,
  H7,
  A8,
  B8,
  C8,
  D8,
  E8,
  F8,
  G8,
  H8;

  public final int file = this.ordinal() % 8 + 1;
  public final int rank = this.ordinal() / 8 + 1;

  public File file() {
    return File.from(file);
  }

  public Rank rank() {
    return Rank.from(rank);
  }

  public boolean isLightSquare() {
    return (file + rank) % 2 == 1;
  }

  public boolean isDarkSquare() {
    return !isLightSquare();
  }

  public Coordinate toCoordinate(boolean isFlipped) {
    if (isFlipped) {
      return new Coordinate(Coordinate.max.x() - file + 1, (float) rank);
    } else {
      return new Coordinate((float) file, Coordinate.max.y() - rank + 1);
    }
  }

  public static Position from(File file, Rank rank) {
    return values()[file.ordinal() + rank.ordinal() * 8];
  }

  public static Position from(int file, int rank) {
    if (file < 1 || file > 8 || rank < 1 || rank > 8)
      throw new IllegalArgumentException("File and rank must be in 1..8");
    return values()[(file - 1) + (rank - 1) * 8];
  }
}
