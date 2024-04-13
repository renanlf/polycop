package edu.br.ufpe.cin.sword.cm.strategies;

import java.util.Collection;
import java.util.Optional;

public interface CopyStrategy<Literal, CopyState> {
	Optional<Collection<Literal>> copy(Collection<Literal> clause);

	default void clear() {
	}

	default CopyState getState() {
		return null;
	}

	default void setState(CopyState state) {
	}
}
