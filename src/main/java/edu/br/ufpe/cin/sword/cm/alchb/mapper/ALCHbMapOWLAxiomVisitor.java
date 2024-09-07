package edu.br.ufpe.cin.sword.cm.alchb.mapper;

import edu.br.ufpe.cin.sword.cm.alchb.factories.ALCHbFactory;
import edu.br.ufpe.cin.sword.cm.alchb.mapper.exceptions.IllegalOWLAxiomException;
import edu.br.ufpe.cin.sword.cm.alchb.mapper.exceptions.TypicalityNormalFormException;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.UUID;

public class ALCHbMapOWLAxiomVisitor {

    private final ALCHbFactory alchbFactory;

    public ALCHbMapOWLAxiomVisitor(ALCHbFactory alchbFactory) {
        this.alchbFactory = alchbFactory;
    }

    public List<List<ALCHbLiteral>> visit(OWLAxiom axiom) {
        if (axiom.isOfType(AxiomType.CLASS_ASSERTION)) {
            return this.visit((OWLClassAssertionAxiom) axiom);
        } else if (axiom.isOfType(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
            return this.visit((OWLObjectPropertyAssertionAxiom) axiom, true);
        } else if (axiom.isOfType(AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION)) {
            return this.visit((OWLNegativeObjectPropertyAssertionAxiom) axiom, false);
        } else if (axiom.isOfType(AxiomType.SUB_OBJECT_PROPERTY)) {
            return this.visit((OWLSubObjectPropertyOfAxiom) axiom);
        } else {
            throw new IllegalOWLAxiomException(axiom);
        }
    }

    private List<List<ALCHbLiteral>> visit(OWLClassAssertionAxiom axiom) {
        if (!axiom.getClassExpression().isClassExpressionLiteral() || !axiom.getIndividual().isNamed()) {
            throw new TypicalityNormalFormException(axiom);
        }

        boolean positive = axiom.getClassExpression().isOWLClass();
        String conceptName = positive
                ? axiom.getClassExpression().asOWLClass().getIRI().getShortForm()
                : ((OWLObjectComplementOf) axiom.getClassExpression()).getOperand().asOWLClass().getIRI().getShortForm();

        return List.of(List.of(alchbFactory.conLiteral(
                conceptName,
                !positive,
                alchbFactory.ind(axiom.getIndividual().toStringID())
        )));
    }

    private List<List<ALCHbLiteral>> visit(OWLPropertyAssertionAxiom<OWLObjectPropertyExpression, OWLIndividual> axiom, boolean positive) {
        if (!axiom.getProperty().isNamed() || !axiom.getSubject().isNamed() || !axiom.getObject().isNamed()) {
            throw new TypicalityNormalFormException(axiom);
        }

        var propertyName = axiom.getProperty().getNamedProperty().getIRI().getShortForm();
        var firstTerm = alchbFactory.ind(axiom.getSubject().asOWLNamedIndividual().getIRI().getShortForm());
        var secondTerm = alchbFactory.ind(axiom.getObject().asOWLNamedIndividual().getIRI().getShortForm());
        return List.of(List.of(alchbFactory.roleLiteral(propertyName, !positive, firstTerm, secondTerm)));
    }

    private List<List<ALCHbLiteral>> visit(OWLSubObjectPropertyOfAxiom axiom) {
        if (!axiom.getSubProperty().isNamed() || !axiom.getSuperProperty().isNamed()) {
            throw new TypicalityNormalFormException(axiom);
        }

        var subPropertyName = axiom.getSubProperty().getNamedProperty().getIRI().getShortForm();
        var superPropertyName = axiom.getSuperProperty().getNamedProperty().getIRI().getShortForm();
        var firstTerm = alchbFactory.var(UUID.randomUUID().toString());
        var secondTerm = alchbFactory.var(UUID.randomUUID().toString());

        return List.of(List.of(
                alchbFactory.roleLiteral(subPropertyName, true, firstTerm, secondTerm),
                alchbFactory.roleLiteral(superPropertyName, false, firstTerm, secondTerm)
        ));
    }
}
