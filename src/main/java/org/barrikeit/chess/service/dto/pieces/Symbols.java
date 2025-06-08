package org.barrikeit.chess.service.dto.pieces;

public enum Symbols {
  WHITE_PAWN("♙", "P"),
  WHITE_KNIGHT("♘", "N"),
  WHITE_BISHOP("♗", "B"),
  WHITE_ROOK("♖", "R"),
  WHITE_QUEEN("♕", "Q"),
  WHITE_KING("♔", "K"),
  BLACK_PAWN("♟", "p"),
  BLACK_KNIGHT("♞", "n"),
  BLACK_BISHOP("♝", "b"),
  BLACK_ROOK("♜", "r"),
  BLACK_QUEEN("♛", "q"),
  BLACK_KING("♚", "k");

  public final String fanSymbol;
  public final String fenSymbol;

  Symbols(String fanSymbol, String fenSymbol) {
    this.fanSymbol = fanSymbol;
    this.fenSymbol = fenSymbol;
  }
}
