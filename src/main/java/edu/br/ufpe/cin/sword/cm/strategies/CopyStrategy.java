package edu.br.ufpe.cin.sword.cm.strategies;

import java.util.List;
import java.util.Optional;

public interface CopyStrategy<Literal, CopyState> {
	Optional<List<Literal>> copy(List<Literal> clause);

	default void clear() {
	}

	default CopyState getState() {
		return null;
	}

	default void setState(CopyState state) {
	}
}
