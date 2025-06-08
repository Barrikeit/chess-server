package org.barrikeit.chess.service.dto.moves;

import org.barrikeit.chess.service.dto.board.Board;
import org.barrikeit.chess.service.dto.pieces.Piece;

public sealed interface BoardEffect permits SimpleMove, PreMove, Consequence {
  Piece piece();

  Board applyOn(Board board);
}
