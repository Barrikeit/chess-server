package org.barrikeit.chess.service.dto.moves;


public sealed interface PreMove extends BoardEffect permits Capture {}
