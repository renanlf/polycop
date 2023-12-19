package edu.br.ufpe.cin.sword.cm.strategies;

import java.util.Set;

public interface BlockingStrategy<Literal, ConnState, CopyState> {

	boolean isBlocked(Literal literal, Set<Literal> path, ConnState connState, CopyState copyState);
}
