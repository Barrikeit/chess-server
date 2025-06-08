package org.barrikeit.chess.service.dto.board;

public record Coordinate(float x, float y) {

  public static final Coordinate min = new Coordinate(1f, 1f);
  public static final Coordinate max = new Coordinate(8f, 8f);

  public Coordinate plus(Coordinate other) {
    return new Coordinate(this.x + other.x, this.y + other.y);
  }

  public Coordinate minus(Coordinate other) {
    return new Coordinate(this.x - other.x, this.y - other.y);
  }

  public Coordinate times(float factor) {
    return new Coordinate(this.x * factor, this.y * factor);
  }

  public Offset toOffset(float squareSize) {
    float offsetX = (x - 1) * squareSize;
    float offsetY = (y - 1) * squareSize;
    return new Offset(offsetX, offsetY);
  }
}
