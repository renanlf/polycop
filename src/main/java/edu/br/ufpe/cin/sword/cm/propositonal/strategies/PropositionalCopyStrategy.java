package edu.br.ufpe.cin.sword.cm.propositonal.strategies;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import edu.br.ufpe.cin.sword.cm.strategies.ConnectionStrategy;
import edu.br.ufpe.cin.sword.cm.strategies.CopyStrategy;

public class PropositionalCopyStrategy implements CopyStrategy<String, Void, Set<Set<String>>> {
	
	private Set<Set<String>> usedClauses;
	
	public PropositionalCopyStrategy() {
		this.usedClauses = new HashSet<>();
	}

	@Override
	public Optional<Set<String>> copy(Set<String> clause, ConnectionStrategy<String, Void, ?> connStrategy, List<String> path) {
		if(usedClauses.contains(clause)) {
			return Optional.empty();
		}
		
		usedClauses.add(clause);
		return Optional.of(clause);
	}

	@Override
	public void clear() {
		usedClauses.clear();
	}

	@Override
	public Set<Set<String>> getState() {
		return new HashSet<>(usedClauses);
	}

	@Override
	public void setState(Set<Set<String>> state) {
		usedClauses.clear();
		usedClauses.addAll(state);
	}
}
