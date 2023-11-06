package edu.br.ufpe.cin.sword.cm.strategies;

import java.util.Optional;
import java.util.Set;

public interface CopyStrategy<Literal, CopyState> {
	Optional<Set<Literal>> copy(Set<Literal> clause, ConnectionStrategy<Literal, ?> connStrategy);
	void clear();
	CopyState getState();
	void setState(CopyState state);
}
