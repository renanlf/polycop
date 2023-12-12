package edu.br.ufpe.cin.sword.cm.strategies;

public interface ConnectionStrategy<Literal, Term, ConnState> {
	boolean connect(Literal literal, Literal other);
	Term getSubstitution(Term term);
	void clear();
	ConnState getState();
	void setState(ConnState state);
}
