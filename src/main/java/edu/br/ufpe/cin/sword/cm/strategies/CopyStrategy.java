package edu.br.ufpe.cin.sword.cm.strategies;

import java.util.Collection;
import java.util.Optional;

public interface CopyStrategy<Literal, Term, CopyState> {
	Optional<Collection<Literal>> copy(Collection<Literal> clause);
	void clear();
	CopyState getState();
	void setState(CopyState state);
}
