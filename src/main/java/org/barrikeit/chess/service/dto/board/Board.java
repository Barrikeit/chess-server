package org.barrikeit.chess.service.dto.board;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.barrikeit.chess.domain.util.enums.ColorEnum;
import org.barrikeit.chess.service.dto.moves.BoardEffect;
import org.barrikeit.chess.service.dto.pieces.Pawn;
import org.barrikeit.chess.service.dto.pieces.Piece;

public record Board(Map<Position, Piece> pieces) {

  public Board() {
    this(initialPieces);
  }

  public Map<Position, Square> squares() {
    return Arrays.stream(Position.values())
        .collect(Collectors.toUnmodifiableMap(p -> p, p -> new Square(p, pieces.get(p))));
  }

  public Square get(Position position) {
    return squares().get(position);
  }

  public Square get(int file, int rank) {
    try {
      return squares().get(Position.from(file, rank));
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  public Square get(File file, Rank rank) {
    return get(Position.from(file, rank));
  }

  public Square find(Piece piece) {
    return squares().values().stream()
        .filter(square -> piece.equals(square.piece()))
        .findFirst()
        .orElse(null);
  }

  public List<Square> find(Class<? extends Piece> type, ColorEnum color) {
    return squares().values().stream()
        .filter(
            square -> {
              Piece p = square.piece();
              return p != null && p.getClass().equals(type) && p.color() == color;
            })
        .toList();
  }

  public Map<Position, Piece> pieces(ColorEnum color) {
    return pieces.entrySet().stream()
        .filter(e -> e.getValue().color() == color)
        .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public Board apply(BoardEffect effect) {
    return effect == null ? this : effect.applyOn(this);
  }

  public static final Map<Position, Piece> initialPieces =
      Map.ofEntries(
          Map.entry(Position.A8, new Rook(ColorEnum.BLACK)),
          Map.entry(Position.B8, new Knight(ColorEnum.BLACK)),
          Map.entry(Position.C8, new Bishop(ColorEnum.BLACK)),
          Map.entry(Position.D8, new Queen(ColorEnum.BLACK)),
          Map.entry(Position.E8, new King(ColorEnum.BLACK)),
          Map.entry(Position.F8, new Bishop(ColorEnum.BLACK)),
          Map.entry(Position.G8, new Knight(ColorEnum.BLACK)),
          Map.entry(Position.H8, new Rook(ColorEnum.BLACK)),
          Map.entry(Position.A7, new Pawn(ColorEnum.BLACK)),
          Map.entry(Position.B7, new Pawn(ColorEnum.BLACK)),
          Map.entry(Position.C7, new Pawn(ColorEnum.BLACK)),
          Map.entry(Position.D7, new Pawn(ColorEnum.BLACK)),
          Map.entry(Position.E7, new Pawn(ColorEnum.BLACK)),
          Map.entry(Position.F7, new Pawn(ColorEnum.BLACK)),
          Map.entry(Position.G7, new Pawn(ColorEnum.BLACK)),
          Map.entry(Position.H7, new Pawn(ColorEnum.BLACK)),
          Map.entry(Position.A2, new Pawn(ColorEnum.WHITE)),
          Map.entry(Position.B2, new Pawn(ColorEnum.WHITE)),
          Map.entry(Position.C2, new Pawn(ColorEnum.WHITE)),
          Map.entry(Position.D2, new Pawn(ColorEnum.WHITE)),
          Map.entry(Position.E2, new Pawn(ColorEnum.WHITE)),
          Map.entry(Position.F2, new Pawn(ColorEnum.WHITE)),
          Map.entry(Position.G2, new Pawn(ColorEnum.WHITE)),
          Map.entry(Position.H2, new Pawn(ColorEnum.WHITE)),
          Map.entry(Position.A1, new Rook(ColorEnum.WHITE)),
          Map.entry(Position.B1, new Knight(ColorEnum.WHITE)),
          Map.entry(Position.C1, new Bishop(ColorEnum.WHITE)),
          Map.entry(Position.D1, new Queen(ColorEnum.WHITE)),
          Map.entry(Position.E1, new King(ColorEnum.WHITE)),
          Map.entry(Position.F1, new Bishop(ColorEnum.WHITE)),
          Map.entry(Position.G1, new Knight(ColorEnum.WHITE)),
          Map.entry(Position.H1, new Rook(ColorEnum.WHITE)));
}
