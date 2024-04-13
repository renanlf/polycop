package edu.br.ufpe.cin.sword.cm.strategies;

public interface ConnectionStrategy<Literal, ConnState> {
	boolean connect(Literal literal, Literal other);

	default void clear() {
	}

	default ConnState getState() {
		return null;
	}

	default void setState(ConnState state) {
	}
}
