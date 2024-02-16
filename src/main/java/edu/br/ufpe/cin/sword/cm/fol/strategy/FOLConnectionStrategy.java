package edu.br.ufpe.cin.sword.cm.fol.strategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.br.ufpe.cin.sword.cm.fol.model.FOLLiteral;
import edu.br.ufpe.cin.sword.cm.fol.model.terms.FOLFunction;
import edu.br.ufpe.cin.sword.cm.fol.model.terms.FOLTerm;
import edu.br.ufpe.cin.sword.cm.fol.model.terms.FOLVariable;
import edu.br.ufpe.cin.sword.cm.strategies.ConnectionStrategy;

public class FOLConnectionStrategy implements ConnectionStrategy<FOLLiteral, Map<FOLVariable, FOLTerm>> {

    private Map<FOLVariable, FOLTerm> substitutions;

    public FOLConnectionStrategy() {
        this.substitutions = Collections.emptyMap();
    }

    @Override
    public boolean connect(FOLLiteral literal, FOLLiteral other) {
        if (!Objects.equals(literal.getPredicate(), other.getPredicate()) 
            || literal.isPositive() == other.isPositive()
            || literal.getTerms().size() != other.getTerms().size()) return false;

        int numberOfTerms = literal.getTerms().size();
        Map<FOLVariable, FOLTerm> subs = substitutions;

        for(int i = 0; i < numberOfTerms; i++) {
            FOLTerm substitutedTermLiteral = getSubstitution(literal.getTerms().get(i));
            FOLTerm substitutedTermOther = getSubstitution(other.getTerms().get(i));
            if(!unifiable(substitutedTermLiteral, substitutedTermOther)) {
                setState(subs);
                return false;
            }
        }
        return true;
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
        if (term == other) return true;

        if (term instanceof FOLVariable variable && other instanceof FOLVariable) {
            addSubstitution(variable, other);
        } else if (term instanceof FOLVariable variable && other instanceof FOLFunction function) {
            if (!occursCheck(variable, function)) return false;
            addSubstitution(variable, function);
        } else if (term instanceof FOLFunction function && other instanceof FOLVariable variable) {
            if (!occursCheck(variable, function)) return false;
            addSubstitution(variable, function);
        } else if (term instanceof FOLFunction termFunction && other instanceof FOLFunction otherFunction) {
            if (!Objects.equals(termFunction.getName(), otherFunction.getName())
                ||termFunction.getTerms().size() != otherFunction.getTerms().size()) return false;

            for (int i = 0; i < termFunction.getTerms().size(); i++) {
                FOLTerm termFunctionInnerTerm = getSubstitution(termFunction.getTerms().get(i));
                FOLTerm otherFunctionInnerTerm = getSubstitution(otherFunction.getTerms().get(i));


                if(!unifiable(termFunctionInnerTerm, otherFunctionInnerTerm)) return false;
            }
        } else {
            throw new IllegalArgumentException("term and other should be a FOLVariable or a FOLFunction");
        }

        return true;
    }

    private FOLTerm getSubstitution(FOLTerm term) {
        while (substitutions.containsKey(term)) {
            term = substitutions.get(term);
        }

        return term;
	}

    private void addSubstitution(FOLVariable variable, FOLTerm term) {
        Map<FOLVariable, FOLTerm> newSubstitutionMap = new HashMap<>();
        newSubstitutionMap.putAll(substitutions);
        newSubstitutionMap.put(variable, term);
        substitutions = Collections.unmodifiableMap(newSubstitutionMap);
    }

    private boolean occursCheck(FOLVariable variable, FOLFunction function) {
        return function.getTerms().stream()
            .map(term -> getSubstitution(term))
            .noneMatch(term -> {
                if (Objects.equals(variable, term)) return true;

                if (term instanceof FOLFunction innerFunction) return !occursCheck(variable, innerFunction);

                return false;
            });
    }

}
