package edu.br.ufpe.cin.sword.cm.alchb.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbConceptLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbTerm;
import edu.br.ufpe.cin.sword.cm.strategies.ConnectionStrategy;
import edu.br.ufpe.cin.sword.cm.strategies.CopyStrategy;

public class ALCHbCopyStrategy implements CopyStrategy<ALCHbLiteral, ALCHbTerm, Map<ALCHbLiteral, List<ALCHbLiteral>>> {

	private Map<ALCHbLiteral, List<ALCHbLiteral>> copies;

	@Override
	public Optional<Set<ALCHbLiteral>> copy(Set<ALCHbLiteral> clause,
			ConnectionStrategy<ALCHbLiteral, ALCHbTerm, ?> connStrategy, Set<ALCHbLiteral> path) {
		
		if(clause.stream()
				.filter(l -> l instanceof ALCHbConceptLiteral)
				.anyMatch(l -> isBlocked((ALCHbConceptLiteral) l, path, connStrategy)))
			return Optional.empty();
		
		// TODO: copy
		throw new IllegalArgumentException("Not implemented yet!");
	}

	private boolean isBlocked(ALCHbConceptLiteral literal, Set<ALCHbLiteral> path,
			ConnectionStrategy<ALCHbLiteral, ALCHbTerm, ?> connStrategy) {
		ALCHbTerm term = connStrategy.getSubstitution(literal.getTerm());

		Set<String> termConceptSet = conceptsSet(term, path, connStrategy);

		if (termConceptSet.contains(literal.getName()))
			return true;

		// if literal is not a key then it must be in some list
		if (!copies.containsKey(literal)) {
			Optional<Map.Entry<ALCHbLiteral, List<ALCHbLiteral>>> optEntry = copies.entrySet().stream()
					.filter(e -> e.getValue().contains(literal)).findFirst();

			if (optEntry.isEmpty())
				throw new IllegalArgumentException("Should exists in a copy list");

			int indexOfLiteral = optEntry.get().getValue().indexOf(literal);
			ALCHbTerm previousTerm = (indexOfLiteral == 0 ? (ALCHbConceptLiteral) optEntry.get().getKey()
					: (ALCHbConceptLiteral) optEntry.get().getValue().get(indexOfLiteral - 1)).getTerm();

			if (conceptsSet(previousTerm, path, connStrategy).containsAll(termConceptSet))
				return true;
		}

		return false;
	}

	private Set<String> conceptsSet(ALCHbTerm term, Set<ALCHbLiteral> path,
			ConnectionStrategy<ALCHbLiteral, ALCHbTerm, ?> connStrategy) {
		return path.stream().filter(l -> l instanceof ALCHbConceptLiteral).map(l -> (ALCHbConceptLiteral) l)
				.filter(l -> connStrategy.getSubstitution(l.getTerm()) == term).map(l -> l.getName())
				.collect(Collectors.toSet());
	}

	@Override
	public void clear() {
		copies.clear();
	}

	@Override
	public Map<ALCHbLiteral, List<ALCHbLiteral>> getState() {
		Map<ALCHbLiteral, List<ALCHbLiteral>> newMap = new HashMap<>();

		for (Map.Entry<ALCHbLiteral, List<ALCHbLiteral>> entry : copies.entrySet()) {
			newMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
		}

		return Collections.unmodifiableMap(new HashMap<>(copies));
	}

	@Override
	public void setState(Map<ALCHbLiteral, List<ALCHbLiteral>> state) {
		copies.clear();
		copies.putAll(state);
	}

}
