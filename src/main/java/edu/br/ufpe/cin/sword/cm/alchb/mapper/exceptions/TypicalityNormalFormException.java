package edu.br.ufpe.cin.sword.cm.alchb.mapper.exceptions;

import org.semanticweb.owlapi.model.OWLAxiom;

public class TypicalityNormalFormException extends RuntimeException {
    public TypicalityNormalFormException(OWLAxiom axiom) {
        super(axiom.toString() + " is not an axiom in Typicality Normal Form (TNF)");
    }
}
