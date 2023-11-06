package edu.br.ufpe.cin.sword.cm.strategies;

import java.util.Set;

public interface LiteralHelperStrategy<Literal> {
	Set<Literal> complementaryOf(Literal literal, Set<Literal> set, ConnectionStrategy<Literal, ?> connStrategy);
}
