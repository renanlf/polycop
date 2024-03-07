package edu.br.ufpe.cin.sword.cm.fol.model;

import java.util.List;
import java.util.stream.Stream;

import edu.br.ufpe.cin.sword.cm.fol.model.terms.FOLTerm;

public class FOLLiteral {

    private final String predicate;
    private final boolean positive;
    private final List<FOLTerm> terms;

    private FOLLiteral(String predicate, boolean positive, FOLTerm... terms) {
        this.predicate = predicate;
        this.positive = positive;
        this.terms = Stream.of(terms).toList();
    }

    public static FOLLiteral of(String predicate, boolean positive, FOLTerm... terms) {
        return new FOLLiteral(predicate, positive, terms);
    }

    public String getPredicate() {
        return predicate;
    }

    public boolean isPositive() {
        return positive;
    }

    public List<FOLTerm> getTerms() {
        return terms;
    }

}
