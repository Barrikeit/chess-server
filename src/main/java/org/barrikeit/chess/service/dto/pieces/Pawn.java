package org.barrikeit.chess.service.dto.pieces;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.barrikeit.chess.domain.util.enums.ColorEnum;
import org.barrikeit.chess.service.dto.board.Board;
import org.barrikeit.chess.service.dto.board.Square;
import org.barrikeit.chess.service.dto.moves.BoardMove;
import org.barrikeit.chess.service.dto.moves.Capture;
import org.barrikeit.chess.service.dto.moves.Move;
import org.barrikeit.chess.service.dto.moves.Promotion;

public class Pawn implements Piece {

  private final ColorEnum color;

  public Pawn(ColorEnum color) {
    this.color = color;
  }

  @Override
  public ColorEnum color() {
    return color;
  }

  @Override
  public int value() {
    return 1;
  }

  @Override
  public String symbol() {
    return switch (color) {
      case WHITE -> Symbols.WHITE_PAWN.fanSymbol;
      case BLACK -> Symbols.BLACK_PAWN.fanSymbol;
    };
  }

  @Override
  public String textSymbol() {
    return switch (color) {
      case WHITE -> Symbols.WHITE_PAWN.fenSymbol;
      case BLACK -> Symbols.BLACK_PAWN.fenSymbol;
    };
  }

  @Override
  public List<BoardMove> pseudoLegalMoves(GameSnapshotState gameSnapshotState, boolean check) {
    Board board = gameSnapshotState.board;
    Square square = board.find(this);
    if (square == null) return List.of();

    List<BoardMove> moves = new ArrayList<>();

    maybeAdd(moves, advanceSingle(board, square));
    maybeAdd(moves, advanceTwoSquares(board, square));
    maybeAdd(moves, captureDiagonal(board, square, -1));
    maybeAdd(moves, captureDiagonal(board, square, 1));
    maybeAdd(moves, enPassantDiagonal(gameSnapshotState, square, -1));
    maybeAdd(moves, enPassantDiagonal(gameSnapshotState, square, 1));

    return checkForPromotion(moves);
  }

  private void maybeAdd(List<BoardMove> moves, BoardMove move) {
    if (move != null) moves.add(move);
  }

  private BoardMove advanceSingle(Board board, Square square) {
    int rankOffset = (color == ColorEnum.WHITE) ? 1 : -1;
    Square target = board.get(square.file(), square.rank() + rankOffset);
    if (target != null && target.isEmpty()) {
      return new BoardMove(new Move(this, square.position(), target.position()));
    }
    return null;
  }

  private BoardMove advanceTwoSquares(Board board, Square square) {
    int startRank = (color == ColorEnum.WHITE) ? 2 : 7;
    if (square.rank() != startRank) return null;

    int r1 = (color == ColorEnum.WHITE) ? 1 : -1;
    int r2 = r1 * 2;

    Square target1 = board.get(square.file(), square.rank() + r1);
    Square target2 = board.get(square.file(), square.rank() + r2);

    if (target1 != null && target1.isEmpty() && target2 != null && target2.isEmpty()) {
      return new BoardMove(new Move(this, square.position(), target2.position()));
    }
    return null;
  }

  private BoardMove captureDiagonal(Board board, Square square, int fileOffset) {
    int rankOffset = (color == ColorEnum.WHITE) ? 1 : -1;
    Square target = board.get(square.file() + fileOffset, square.rank() + rankOffset);
    if (target != null && target.hasPiece(color.opposite())) {
      return new BoardMove(
          new Move(this, square.position(), target.position()),
          new Capture(Objects.requireNonNull(target.piece()), target.position()));
    }
    return null;
  }

  private BoardMove enPassantDiagonal(GameSnapshotState state, Square square, int fileOffset) {
    int enPassantRank = (color == ColorEnum.WHITE) ? 5 : 4;
    if (square.position().rank() != enPassantRank) return null;

    BoardMove lastMove = state.lastMove();
    if (lastMove == null || !(lastMove.piece() instanceof Pawn)) return null;

    boolean fromStart = lastMove.from().rank() == ((color == ColorEnum.WHITE) ? 7 : 2);
    boolean twoStep = lastMove.to().rank() == square.rank();
    boolean correctFile = lastMove.to().file() == square.file() + fileOffset;

    if (fromStart && twoStep && correctFile) {
      int rankOffset = (color == ColorEnum.WHITE) ? 1 : -1;

      Square enPassantTarget =
          state.board().get(square.file() + fileOffset, square.rank() + rankOffset);
      Square capturedSquare = state.board().get(square.file() + fileOffset, square.rank());

      if (enPassantTarget == null || capturedSquare == null) return null;

      return new BoardMove(
          new Move(this, square.position(), enPassantTarget.position()),
          new Capture(Objects.requireNonNull(capturedSquare.piece()), capturedSquare.position()));
    }

    return null;
  }

  private List<BoardMove> checkForPromotion(List<BoardMove> moves) {
    List<BoardMove> result = new ArrayList<>();
    int promotionRank = (color == ColorEnum.WHITE) ? 8 : 1;

    for (BoardMove move : moves) {
      if (move.move().to().rank == promotionRank) {
        result.add(move.consequence(new Promotion(move.move().to(), new Queen(color))).build());
        result.add(move.consequence(new Promotion(move.move().to(), new Rook(color))).build());
        result.add(move.consequence(new Promotion(move.move().to(), new Bishop(color))).build());
        result.add(move.consequence(new Promotion(move.move().to(), new Knight(color))).build());
      } else {
        result.add(move);
      }
    }

    return result;
  }
}
