package edu.br.ufpe.cin.sword.cm.strategies;

import java.util.Optional;
import java.util.Set;

public interface CopyStrategy<T, S> {
	Optional<Set<T>> copy(Set<T> clause);
	void clear();
	S getState();
	void setState(S state);
}
