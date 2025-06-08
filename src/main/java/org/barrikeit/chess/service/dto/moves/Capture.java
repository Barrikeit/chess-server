package org.barrikeit.chess.service.dto.moves;

import java.util.HashMap;
import org.barrikeit.chess.service.dto.board.Board;
import org.barrikeit.chess.service.dto.board.Position;
import org.barrikeit.chess.service.dto.pieces.Piece;

public record Capture(Piece piece, Position position) implements PreMove {
  @Override
  public Board applyOn(Board board) {
    var newPieces = new HashMap<>(board.pieces());
    newPieces.remove(position);
    return new Board(newPieces);
  }
}
