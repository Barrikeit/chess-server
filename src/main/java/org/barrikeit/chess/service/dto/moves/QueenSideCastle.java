package org.barrikeit.chess.service.dto.moves;

import java.util.HashMap;
import org.barrikeit.chess.service.dto.board.Board;
import org.barrikeit.chess.service.dto.board.Position;
import org.barrikeit.chess.service.dto.pieces.Piece;

public record QueenSideCastle(Piece piece, Position from, Position to) implements SimpleMove {
  @Override
  public Board applyOn(Board board) {
    var newPieces = new HashMap<>(board.pieces());
    newPieces.remove(from);
    newPieces.put(to, piece);
    return new Board(newPieces);
  }
}
