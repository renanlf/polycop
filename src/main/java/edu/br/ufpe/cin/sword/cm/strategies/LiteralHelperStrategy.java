package edu.br.ufpe.cin.sword.cm.strategies;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface LiteralHelperStrategy<Literal> {
	boolean complementaryOf(Literal literal, Literal other);
	default void clear() { }
	
	default Set<Literal> literalsComplementaryOf(Literal literal, Collection<Literal> set) {
		return set.stream()
				.filter(other -> complementaryOf(literal, other))
				.collect(Collectors.toSet());
	}

    default List<List<Literal>> complementaryOf(Literal literal, List<List<Literal>> matrix) {
		return matrix.stream()
			.filter(clause -> clause.stream().anyMatch(other -> complementaryOf(literal, other)))
			.collect(Collectors.toList());
	}
}
