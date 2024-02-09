package edu.br.ufpe.cin.sword.cm.fol.model;

import java.util.List;
import java.util.stream.Stream;

public class FOLLiteral {

    private List<FOLTerm> terms;

    public FOLLiteral(FOLTerm... terms) {
        this.terms = Stream.of(terms).toList();
    }

    public List<FOLTerm> getTerms() {
        return terms;
    }

}
