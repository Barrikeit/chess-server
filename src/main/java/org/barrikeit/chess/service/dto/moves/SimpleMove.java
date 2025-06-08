package org.barrikeit.chess.service.dto.moves;

import org.barrikeit.chess.service.dto.board.Position;

public sealed interface SimpleMove extends BoardEffect
    permits Move, KingSideCastle, QueenSideCastle {
  Position from();

  Position to();
}
