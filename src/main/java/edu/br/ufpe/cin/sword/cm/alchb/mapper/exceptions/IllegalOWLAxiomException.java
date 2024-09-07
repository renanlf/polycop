package edu.br.ufpe.cin.sword.cm.alchb.mapper.exceptions;

import org.semanticweb.owlapi.model.OWLAxiom;

public class IllegalOWLAxiomException extends RuntimeException {

    public IllegalOWLAxiomException(OWLAxiom axiom) {
        super(axiom.toString() + " is not a mappable axiom");
    }
}
