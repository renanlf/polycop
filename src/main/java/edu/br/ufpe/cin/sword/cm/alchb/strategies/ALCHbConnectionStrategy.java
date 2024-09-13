package edu.br.ufpe.cin.sword.cm.alchb.strategies;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import edu.br.ufpe.cin.sword.cm.alchb.factories.ALCHbFactory;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbBiOrderedLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbConceptLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbOrderedLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbRoleLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbTerm;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbUnaryIndividual;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbVariable;
import edu.br.ufpe.cin.sword.cm.strategies.ConnectionStrategy;

public class ALCHbConnectionStrategy implements ConnectionStrategy<ALCHbLiteral, Map<ALCHbVariable, ALCHbTerm>> {

	private Map<ALCHbVariable, ALCHbTerm> subs;
	
	private static final ALCHbVariable NULL_VAR = new ALCHbVariable("null");

	public ALCHbConnectionStrategy() {
		this.subs = Collections.emptyMap();
	}

	@Override
	public boolean connect(ALCHbLiteral literal, ALCHbLiteral other) {
		if (literal instanceof ALCHbConceptLiteral && other instanceof ALCHbConceptLiteral) {
			return connect((ALCHbConceptLiteral) literal, (ALCHbConceptLiteral) other);
		}

		if (literal instanceof ALCHbRoleLiteral && other instanceof ALCHbRoleLiteral) {
			return connect((ALCHbRoleLiteral) literal, (ALCHbRoleLiteral) other);
		}

		if (literal instanceof ALCHbOrderedLiteral && other instanceof ALCHbOrderedLiteral) {
			return connect((ALCHbOrderedLiteral) literal, (ALCHbOrderedLiteral) other);
		}

		if (literal instanceof ALCHbBiOrderedLiteral && other instanceof ALCHbBiOrderedLiteral) {
			return connect((ALCHbBiOrderedLiteral) literal, (ALCHbBiOrderedLiteral) other);
		}

		return false;
	}

	private boolean connect(ALCHbConceptLiteral literal, ALCHbConceptLiteral other) {
		Map.Entry<ALCHbVariable, ALCHbTerm> entry = unify(getSubstitution(literal.getTerm()),
				getSubstitution(other.getTerm()));

		if (entry == null)
			return false;
		
		if(entry.getKey() != NULL_VAR)
			putEntries(Collections.singleton(entry));
		
		return true;
	}

	private boolean connect(ALCHbRoleLiteral literal, ALCHbRoleLiteral other) {		
		Map.Entry<ALCHbVariable, ALCHbTerm> entryFirst = unify(getSubstitution(literal.getFirst()),
				getSubstitution(other.getFirst()));

		Map.Entry<ALCHbVariable, ALCHbTerm> entrySecond = unify(getSubstitution(literal.getSecond()),
				getSubstitution(other.getSecond()));

		if (entryFirst == null || entrySecond == null)
			return false;

		Set<Map.Entry<ALCHbVariable, ALCHbTerm>> entries = new HashSet<>();
		
		if(entryFirst.getKey() != NULL_VAR)
			entries.add(entryFirst);
		
		if(entrySecond.getKey() != NULL_VAR)
			entries.add(entrySecond);
		
		putEntries(entries);

		return true;
	}

	private boolean connect(ALCHbOrderedLiteral literal, ALCHbOrderedLiteral other) {
		Map.Entry<ALCHbVariable, ALCHbTerm> entryFirst = unify(getSubstitution(literal.getFirst()),
				getSubstitution(other.getFirst()));

		Map.Entry<ALCHbVariable, ALCHbTerm> entrySecond = unify(getSubstitution(literal.getSecond()),
				getSubstitution(other.getSecond()));

		if (entryFirst == null || entrySecond == null)
			return false;

		Set<Map.Entry<ALCHbVariable, ALCHbTerm>> entries = new HashSet<>();
		
		if(entryFirst.getKey() != NULL_VAR)
			entries.add(entryFirst);
		
		if(entrySecond.getKey() != NULL_VAR)
			entries.add(entrySecond);
		
		putEntries(entries);

		return true;
	}

	private boolean connect(ALCHbBiOrderedLiteral literal, ALCHbBiOrderedLiteral other) {
		Map.Entry<ALCHbVariable, ALCHbTerm> entryFirst = unify(getSubstitution(literal.getFirst()),
				getSubstitution(other.getFirst()));

		Map.Entry<ALCHbVariable, ALCHbTerm> entrySecond = unify(getSubstitution(literal.getSecond()),
				getSubstitution(other.getSecond()));

		Map.Entry<ALCHbVariable, ALCHbTerm> entryThird = unify(getSubstitution(literal.getThird()),
				getSubstitution(other.getThird()));

		Map.Entry<ALCHbVariable, ALCHbTerm> entryFourth = unify(getSubstitution(literal.getFourth()),
				getSubstitution(other.getFourth()));

		if (entryFirst == null || entrySecond == null || entryThird == null || entryFourth == null)
			return false;

		Set<Map.Entry<ALCHbVariable, ALCHbTerm>> entries = new HashSet<>();
		
		if(entryFirst.getKey() != NULL_VAR)
			entries.add(entryFirst);
		
		if(entrySecond.getKey() != NULL_VAR)
			entries.add(entrySecond);
		
		if(entryThird.getKey() != NULL_VAR)		
			entries.add(entryThird);
		
		if(entryFourth.getKey() != NULL_VAR)
			entries.add(entryFourth);
		
		putEntries(entries);

		return true;
	}

	private Map.Entry<ALCHbVariable, ALCHbTerm> unify(ALCHbTerm term, ALCHbTerm other) {
		// entry <null, null> when terms are equal
		// null when not unifiable
		
		if (Objects.equals(term, other)) return Map.entry(NULL_VAR, NULL_VAR);
		
		if (term instanceof ALCHbVariable) {
			if (other instanceof ALCHbUnaryIndividual unaryIndividual && unaryIndividual.getFillerTerm().equals(term)) {
				return null;
			}
			return Map.entry((ALCHbVariable) term, other);
		}

		if (other instanceof ALCHbVariable) {
			if (term instanceof ALCHbUnaryIndividual unaryIndividual && unaryIndividual.getFillerTerm().equals(other)) {
				return null;
			}
			return Map.entry((ALCHbVariable) other, term);
		}
		
		if (term.getName().equals(other.getName()) 
				&& term instanceof ALCHbUnaryIndividual 
				&& other instanceof ALCHbUnaryIndividual) {
			
			return unify(((ALCHbUnaryIndividual) term).getFillerTerm(),
					((ALCHbUnaryIndividual) other).getFillerTerm());
		}

		return null;
	}

	/**
	 * this method changes the subs map. It is the only point where it is updated.
	 * this approach ensures immutable map state when backtracking occurs.
	 * @param entries a set of entries to be added.
	 */
	private void putEntries(Set<Map.Entry<ALCHbVariable, ALCHbTerm>> entries) {	
		if(!entries.isEmpty()) {
			subs = new HashMap<>(subs);
			entries.forEach(e -> subs.put(e.getKey(), e.getValue()));
			subs = Collections.unmodifiableMap(subs);
		}
	}
	
	private ALCHbTerm getSubstitution(ALCHbTerm term) {
		if (term instanceof ALCHbUnaryIndividual unaryIndividual) {
			var fillerTerm = getSubstitution(unaryIndividual.getFillerTerm());
			return new ALCHbFactory().unaryInd(unaryIndividual.getName(), fillerTerm);
		}

		while (subs.containsKey(term)) {
			term = subs.get(term);
		}

		return term;
	}

	@Override
	public void clear() {
		subs = Collections.emptyMap();
	}

	@Override
	public Map<ALCHbVariable, ALCHbTerm> getState() {
		return subs;
	}

	@Override
	public void setState(Map<ALCHbVariable, ALCHbTerm> state) {
		subs = state;
	}

}
