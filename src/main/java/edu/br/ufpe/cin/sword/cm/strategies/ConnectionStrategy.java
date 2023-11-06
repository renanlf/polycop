package edu.br.ufpe.cin.sword.cm.strategies;

public interface ConnectionStrategy<Literal, ConnState> {
	boolean connect(Literal literal, Literal other);
	void clear();
	ConnState getState();
	void setState(ConnState state);
}
