package edu.br.ufpe.cin.sword.cm.propositonal.strategies;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import edu.br.ufpe.cin.sword.cm.strategies.CopyStrategy;

public class PropositionalCopyStrategy implements CopyStrategy<String, Set<Collection<String>>> {
	
	private Set<Collection<String>> usedClauses;
	
	public PropositionalCopyStrategy() {
		this.usedClauses = new HashSet<>();
	}

	@Override
	public Optional<List<String>> copy(List<String> clause) {
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
	public Set<Collection<String>> getState() {
		return new HashSet<>(usedClauses);
	}

	@Override
	public void setState(Set<Collection<String>> state) {
		usedClauses.clear();
		usedClauses.addAll(state);
	}
}
