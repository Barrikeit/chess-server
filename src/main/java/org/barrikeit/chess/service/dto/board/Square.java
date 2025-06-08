package org.barrikeit.chess.service.dto.board;

import org.barrikeit.chess.domain.util.enums.ColorEnum;
import org.barrikeit.chess.service.dto.pieces.Piece;

public record Square(Position position, Piece piece) {

  public Square(Position position) {
    this(position, null);
  }

  public int file() {
    return position.file;
  }

  public int rank() {
    return position.rank;
  }

  public File fileEnum() {
    return position.file();
  }

  public Rank rankEnum() {
    return position.rank();
  }

  public boolean isLight() {
    return position.isLightSquare();
  }

  public boolean isDark() {
    return position.isDarkSquare();
  }

  public boolean isEmpty() {
    return piece == null;
  }

  public boolean isNotEmpty() {
    return !isEmpty();
  }

  public boolean hasPiece(ColorEnum color) {
    return piece != null && piece.color() == color;
  }

  public boolean hasWhitePiece() {
    return piece != null && piece.color() == ColorEnum.WHITE;
  }

  public boolean hasBlackPiece() {
    return piece != null && piece.color() == ColorEnum.BLACK;
  }

  @Override
  public String toString() {
    return fileEnum().notation + rankEnum().notation;
  }
}
