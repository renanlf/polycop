package edu.br.ufpe.cin.sword.cm.alchb.mapper;

import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.List;

public class ALCHbOWLAxiomVisit {
    private final List<? extends OWLAxiom> normalizedAxioms;
    private final List<? extends OWLAxiom> axiomsToNormalize;

    public ALCHbOWLAxiomVisit(List<? extends OWLAxiom> normalizedAxioms, List<? extends OWLAxiom> axiomsToNormalize) {
        this.normalizedAxioms = normalizedAxioms;
        this.axiomsToNormalize = axiomsToNormalize;
    }

    public ALCHbOWLAxiomVisit(List<OWLAxiom> normalizedAxioms) {
        this.normalizedAxioms = normalizedAxioms;
        this.axiomsToNormalize = List.of();
    }

    public List<? extends OWLAxiom> getNormalizedAxioms() {
        return normalizedAxioms;
    }

    public List<? extends OWLAxiom> getAxiomsToNormalize() {
        return axiomsToNormalize;
    }
}
