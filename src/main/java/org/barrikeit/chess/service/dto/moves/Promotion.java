package org.barrikeit.chess.service.dto.moves;

import java.util.HashMap;
import org.barrikeit.chess.service.dto.board.Board;
import org.barrikeit.chess.service.dto.board.Position;
import org.barrikeit.chess.service.dto.pieces.Piece;

public record Promotion(Position position, Piece piece) implements Consequence {
  @Override
  public Board applyOn(Board board) {
    var newPieces = new HashMap<>(board.pieces());
    newPieces.remove(position);
    newPieces.put(position, piece);
    return new Board(newPieces);
  }
}
