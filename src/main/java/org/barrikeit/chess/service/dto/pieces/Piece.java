package org.barrikeit.chess.service.dto.pieces;

import java.util.List;
import org.barrikeit.chess.domain.util.enums.ColorEnum;
import org.barrikeit.chess.service.dto.moves.BoardMove;

public interface Piece {

  ColorEnum color();

  int value();

  String symbol();

  String textSymbol();

  /** List of all possible moves for this piece without applying pin/check constraints. */
  default List<BoardMove> pseudoLegalMoves(GameSnapshotState gameSnapshotState, boolean check) {
    return List.of();
  }
}
