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
        } else if (axiom.isOfType(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
            this.visit((OWLObjectPropertyAssertionAxiom) axiom);
        } else if (axiom.isOfType(AxiomType.SUB_OBJECT_PROPERTY)) {
            this.visit((OWLSubObjectPropertyOfAxiom) axiom);
        }

        return visitResult;
    }

    @Override
    public void visit(OWLClassAssertionAxiom axiom) {
        if (axiom.getClassExpression().isClassExpressionLiteral()) {
            this.visitResult = new ALCHbOWLAxiomVisit(List.of(axiom), List.of());
        } else {
            OWLClass newClass = owlDataFactory.getOWLClass(UUID.randomUUID().toString());
            this.visitResult = new ALCHbOWLAxiomVisit(
                    List.of(owlDataFactory.getOWLClassAssertionAxiom(newClass, axiom.getIndividual())),
                    List.of(owlDataFactory.getOWLEquivalentClassesAxiom(newClass, axiom.getClassExpression()))
            );
        }
    }

    @Override
    public void visit(OWLObjectPropertyAssertionAxiom axiom) {
        if (axiom.getProperty().isNamed()) {
            this.visitResult = new ALCHbOWLAxiomVisit(List.of(axiom), List.of());
        } else {
            OWLObjectProperty newProperty = owlDataFactory.getOWLObjectProperty(UUID.randomUUID().toString());
            this.visitResult = new ALCHbOWLAxiomVisit(
                    List.of(owlDataFactory.getOWLObjectPropertyAssertionAxiom(newProperty, axiom.getSubject(), axiom.getObject())),
                    List.of(owlDataFactory.getOWLSubObjectPropertyOfAxiom(newProperty, axiom.getProperty()))
            );
        }
    }

    @Override
    public void visit(OWLEquivalentClassesAxiom axiom) {
        this.visitResult = new ALCHbOWLAxiomVisit(List.of(), axiom.asOWLSubClassOfAxioms().stream().toList());
    }

    @Override
    public void visit(OWLSubClassOfAxiom axiom) {
        // TODO
        OWLAxiomVisitor.super.visit(axiom);
    }

    @Override
    public void visit(OWLSubObjectPropertyOfAxiom axiom) {
        if (!axiom.getSubProperty().isNamed() && !axiom.getSuperProperty().isNamed()) {
            OWLObjectProperty newProperty = owlDataFactory.getOWLObjectProperty(UUID.randomUUID().toString());
            this.visitResult = new ALCHbOWLAxiomVisit(List.of(
                    owlDataFactory.getOWLSubObjectPropertyOfAxiom(axiom.getSubProperty(), newProperty),
                    owlDataFactory.getOWLSubObjectPropertyOfAxiom(newProperty, axiom.getSuperProperty())
            ), List.of());
        } else {
            this.visitResult = new ALCHbOWLAxiomVisit(List.of(axiom), List.of());
        }
    }

    public ALCHbOWLAxiomVisit getVisitResult() {
        return visitResult;
    }
}
