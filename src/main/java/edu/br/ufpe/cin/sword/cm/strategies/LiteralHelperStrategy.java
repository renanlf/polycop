package edu.br.ufpe.cin.sword.cm.strategies;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface LiteralHelperStrategy<Literal> {
	boolean complementaryOf(Literal literal, Literal other);
	default void clear() { }
	
	default List<Literal> complementaryOf(Literal literal, Collection<Literal> set) {
		return set.stream()
				.filter(other -> complementaryOf(literal, other))
				.collect(Collectors.toList());
	}

    default List<List<Literal>> complementaryOfInMatrix(Literal literal, List<List<Literal>> matrix) {
		return matrix.stream()
			.filter(clause -> clause.stream().anyMatch(other -> complementaryOf(literal, other)))
			.collect(Collectors.toList());
	}
}
