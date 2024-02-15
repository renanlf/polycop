package edu.br.ufpe.cin.sword.cm.fol.model.terms;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class FOLFunction extends FOLTerm {

    private List<FOLTerm> terms;

    public FOLFunction(String name, FOLTerm... terms) {
        super(name);
        this.terms = Stream.of(terms).toList();
    }

    public FOLFunction(String name) {
        super(name);
        this.terms = Collections.emptyList();
    }

    public List<FOLTerm> getTerms() {
        return terms;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        FOLFunction other = (FOLFunction) obj;
        return Objects.equals(this.getName(), other.getName())
            && Objects.equals(this.terms, other.terms);
    }

    @Override
    public int hashCode() {
        final int prime = 3;
        return prime * 1 + ((this.getName() == null ? 0 : this.getName().hashCode()) + this.getTerms().hashCode());
    }

}
