package edu.br.ufpe.cin.sword.cm.propositonal.strategies;

import java.util.Collection;
import java.util.Set;

import edu.br.ufpe.cin.sword.cm.strategies.BlockingStrategy;

public class PropositionalBlockingStrategy implements BlockingStrategy<String, Void, Set<Collection<String>>> {

	@Override
	public boolean isBlocked(String literal, Set<String> path, Void connState, Set<Collection<String>> usedClauses) {
		return false;
	}

}
