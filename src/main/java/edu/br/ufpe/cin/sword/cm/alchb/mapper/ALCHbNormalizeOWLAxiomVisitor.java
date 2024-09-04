package edu.br.ufpe.cin.sword.cm.alchb.mapper;

import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.UUID;

public class ALCHbNormalizeOWLAxiomVisitor implements OWLAxiomVisitor {

    private final OWLDataFactory owlDataFactory;
    private ALCHbOWLAxiomVisit visitResult;

    public ALCHbNormalizeOWLAxiomVisitor(OWLDataFactory owlDataFactory) {
        this.owlDataFactory = owlDataFactory;
    }

    public ALCHbOWLAxiomVisit visit(OWLAxiom axiom) {
        if (axiom.isOfType(AxiomType.EQUIVALENT_CLASSES)) {
            this.visit((OWLEquivalentClassesAxiom) axiom);
        } else if (axiom.isOfType(AxiomType.CLASS_ASSERTION)) {
            this.visit((OWLClassAssertionAxiom) axiom);
        }

        return visitResult;
    }

    @Override
    public void visit(OWLClassAssertionAxiom axiom) {
        OWLClass newClass = owlDataFactory.getOWLClass(UUID.randomUUID().toString());
        this.visitResult = new ALCHbOWLAxiomVisit(
                List.of(owlDataFactory.getOWLClassAssertionAxiom(newClass, axiom.getIndividual())),
                List.of(owlDataFactory.getOWLEquivalentClassesAxiom(newClass, axiom.getClassExpression()))
        );
    }

    @Override
    public void visit(OWLEquivalentClassesAxiom axiom) {
        this.visitResult = new ALCHbOWLAxiomVisit(List.of(), axiom.asOWLSubClassOfAxioms().stream().toList());
    }

    @Override
    public void visit(OWLSubClassOfAxiom axiom) {
        OWLAxiomVisitor.super.visit(axiom);
    }

    @Override
    public void visit(OWLSubObjectPropertyOfAxiom axiom) {

    }

    @Override
    public void visit(OWLObjectPropertyAssertionAxiom axiom) {
        OWLAxiomVisitor.super.visit(axiom);
    }

    public ALCHbOWLAxiomVisit getVisitResult() {
        return visitResult;
    }
}
