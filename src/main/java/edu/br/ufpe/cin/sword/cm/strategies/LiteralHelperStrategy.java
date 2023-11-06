package edu.br.ufpe.cin.sword.cm.strategies;

import java.util.Collection;

public interface LiteralHelperStrategy<T, S> {
	Collection<T> complementaryOf(T literal, Collection<T> collection, ConnectionStrategy<T, S> connStrategy);
}
