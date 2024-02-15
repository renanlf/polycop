package edu.br.ufpe.cin.sword.cm.fol.strategy;

import java.util.Collections;
import java.util.Map;

import edu.br.ufpe.cin.sword.cm.fol.model.FOLLiteral;
import edu.br.ufpe.cin.sword.cm.fol.model.terms.FOLTerm;
import edu.br.ufpe.cin.sword.cm.fol.model.terms.FOLVariable;
import edu.br.ufpe.cin.sword.cm.strategies.ConnectionStrategy;

public class FOLConnectionStrategy implements ConnectionStrategy<FOLLiteral, Map<FOLVariable, FOLTerm>> {

    private Map<FOLVariable, FOLTerm> substitutions;

    @Override
    public boolean connect(FOLLiteral literal, FOLLiteral other) {
        int numberOfTerms = literal.getTerms().size();
        for(int i = 0; i < numberOfTerms; i++) {
            FOLTerm substitutedTermLiteral = getSubstitution(literal.getTerms().get(i));
            FOLTerm substitutedTermOther = getSubstitution(other.getTerms().get(i));
            if(!unifiable(substitutedTermLiteral, substitutedTermOther)) {
                return false;
            }
        }
        return true;
    }

    public FOLConnectionStrategy() {
        this.substitutions = Collections.emptyMap();
    }

    @Override
    public void clear() {
        this.substitutions.clear();
    }

    @Override
    public Map<FOLVariable, FOLTerm> getState() {
        return substitutions;
    }

    @Override
    public void setState(Map<FOLVariable, FOLTerm> state) {
        this.substitutions = state;
    }

    private boolean unifiable(FOLTerm term, FOLTerm other) {
        // TODO implement unification properly
        return true;
    }

    private FOLTerm getSubstitution(FOLTerm term) {
        while (substitutions.containsKey(term)) {
            term = substitutions.get(term);
        }

        return term;
	}

}
