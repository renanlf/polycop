package edu.br.ufpe.cin.sword.cm.strategies;

public interface ConnectionStrategy<Literal, Term, ConnState> {
	boolean connect(Literal literal, Literal other);
	void clear();
	ConnState getState();
	void setState(ConnState state);
}
