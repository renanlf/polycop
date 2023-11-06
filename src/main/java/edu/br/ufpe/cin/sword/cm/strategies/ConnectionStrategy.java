package edu.br.ufpe.cin.sword.cm.strategies;

public interface ConnectionStrategy<T, S> {
	boolean connect(T positive, T negative);
	void clear();
	S getState();
	void setState(S state);
}
