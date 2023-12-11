package edu.br.ufpe.cin.sword.cm.alchb.strategies;

import java.util.HashMap;
import java.util.Map;

import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbBiOrderedLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbConceptLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbOrderedLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbRoleLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbTerm;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbVariable;
import edu.br.ufpe.cin.sword.cm.alchb.states.ALCHbConnState;
import edu.br.ufpe.cin.sword.cm.strategies.ConnectionStrategy;

public class ALCHbConnectionStrategy implements ConnectionStrategy<ALCHbLiteral, ALCHbConnState> {

	private final Map<ALCHbVariable, ALCHbTerm> subs;

	public ALCHbConnectionStrategy() {
		this.subs = new HashMap<>();
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
		if (!literal.getName().equals(other.getName()))
			return false;

		if (literal.getTerm() == other.getTerm())
			return true;

		Map.Entry<ALCHbVariable, ALCHbTerm> entry = unify(getSubstitution(literal.getTerm()),
				getSubstitution(other.getTerm()));

		if (entry == null)
			return false;

		subs.put(entry.getKey(), entry.getValue());
		return true;

	}

	private boolean connect(ALCHbRoleLiteral literal, ALCHbRoleLiteral other) {
		if (!literal.getName().equals(other.getName()))
			return false;

		if (literal.getFirst() == other.getFirst() && literal.getSecond() == other.getSecond())
			return true;

		Map.Entry<ALCHbVariable, ALCHbTerm> entryFirst = unify(getSubstitution(literal.getFirst()),
				getSubstitution(other.getFirst()));

		Map.Entry<ALCHbVariable, ALCHbTerm> entrySecond = unify(getSubstitution(literal.getSecond()),
				getSubstitution(other.getSecond()));

		if (entryFirst == null || entrySecond == null)
			return false;

		subs.put(entryFirst.getKey(), entryFirst.getValue());
		subs.put(entrySecond.getKey(), entrySecond.getValue());

		return true;
	}

	private boolean connect(ALCHbOrderedLiteral literal, ALCHbOrderedLiteral other) {
		if (literal.getFirst() == other.getFirst() && literal.getSecond() == other.getSecond())
			return true;

		Map.Entry<ALCHbVariable, ALCHbTerm> entryFirst = unify(getSubstitution(literal.getFirst()),
				getSubstitution(other.getFirst()));

		Map.Entry<ALCHbVariable, ALCHbTerm> entrySecond = unify(getSubstitution(literal.getSecond()),
				getSubstitution(other.getSecond()));

		if (entryFirst == null || entrySecond == null)
			return false;

		subs.put(entryFirst.getKey(), entryFirst.getValue());
		subs.put(entrySecond.getKey(), entrySecond.getValue());

		return true;
	}

	private boolean connect(ALCHbBiOrderedLiteral literal, ALCHbBiOrderedLiteral other) {
		if (literal.getFirst() == other.getFirst() && literal.getSecond() == other.getSecond()
				&& literal.getThird() == other.getThird() && literal.getFourth() == other.getFourth())
			return true;

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

		subs.put(entryFirst.getKey(), entryFirst.getValue());
		subs.put(entrySecond.getKey(), entrySecond.getValue());
		subs.put(entryThird.getKey(), entryThird.getValue());
		subs.put(entryFourth.getKey(), entryFourth.getValue());

		return true;
	}

	private Map.Entry<ALCHbVariable, ALCHbTerm> unify(ALCHbTerm term, ALCHbTerm other) {
		if (term instanceof ALCHbVariable) {
			return Map.entry((ALCHbVariable) term, other);
		}

		if (other instanceof ALCHbVariable) {
			return Map.entry((ALCHbVariable) other, term);
		}

		return null;
	}

	private ALCHbTerm getSubstitution(ALCHbTerm term) {
		if (term instanceof ALCHbVariable) {
			return getSubstitution((ALCHbVariable) term);
		}

		return term;
	}

	private ALCHbTerm getSubstitution(ALCHbVariable variable) {
		ALCHbTerm toReturn = variable;

		while (subs.containsKey(toReturn)) {
			toReturn = subs.get(toReturn);
		}

		return toReturn;
	}

	@Override
	public void clear() {
		subs.clear();
	}

	@Override
	public ALCHbConnState getState() {
		return new ALCHbConnState(subs);
	}

	@Override
	public void setState(ALCHbConnState state) {
		subs.clear();
		subs.putAll(state.getSubs());
	}

}
