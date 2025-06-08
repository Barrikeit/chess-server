package org.barrikeit.chess.service.dto.moves;

public sealed interface Consequence extends BoardEffect permits Move, Promotion {}
