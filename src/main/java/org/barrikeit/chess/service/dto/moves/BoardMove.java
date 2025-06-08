package org.barrikeit.chess.service.dto.moves;

import java.util.*;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import org.barrikeit.chess.service.dto.board.Position;
import org.barrikeit.chess.service.dto.pieces.Pawn;
import org.barrikeit.chess.service.dto.pieces.Piece;

public record BoardMove(
    SimpleMove move,
    PreMove preMove,
    Consequence consequence,
    EnumSet<Ambiguity> ambiguity,
    Position from,
    Position to,
    Piece piece) {

  public BoardMove(SimpleMove move) {
    this(move, null, null, EnumSet.noneOf(Ambiguity.class));
  }

  public BoardMove(
      SimpleMove move, PreMove preMove, Consequence consequence, Set<Ambiguity> ambiguity) {

    this(
        Objects.requireNonNull(move),
        preMove,
        consequence,
        ambiguity != null ? EnumSet.copyOf(ambiguity) : EnumSet.noneOf(Ambiguity.class),
        move.from(),
        move.to(),
        move.piece());
  }

  @Override
  public String toString() {
    return toString(true);
  }

  public String toString(boolean useFigurineNotation) {
    if (move instanceof KingSideCastle) return "O-O";
    if (move instanceof QueenSideCastle) return "O-O-O";

    boolean isCapture = preMove instanceof Capture;
    String symbol = "";

    if (!(piece instanceof Pawn)) {
      symbol = useFigurineNotation ? piece.symbol() : piece.textSymbol();
    } else if (isCapture) {
      symbol = from.file().notation;
    }

    String file = ambiguity.contains(Ambiguity.AMBIGUOUS_FILE) ? from.file().toString() : "";
    String rank = ambiguity.contains(Ambiguity.AMBIGUOUS_RANK) ? from.rank().toString() : "";
    String capture = isCapture ? "x" : "";
    String promotion = (consequence instanceof Promotion p) ? "=" + p.piece().textSymbol() : "";

    return symbol + file + rank + capture + to + promotion;
  }

  public BoardMove withConsequence(Consequence newConsequence) {
    return new BoardMove(move, preMove, newConsequence, ambiguity);
  }

  public BoardMove withPreMove(PreMove newPreMove) {
    return new BoardMove(move, newPreMove, consequence, ambiguity);
  }

  public BoardMove withAmbiguity(Set<Ambiguity> newAmbiguity) {
    return new BoardMove(move, preMove, consequence, newAmbiguity);
  }
}
