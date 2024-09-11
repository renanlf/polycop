package edu.br.ufpe.cin.sword.cm.alchb.mapper;

import edu.br.ufpe.cin.sword.cm.alchb.factories.ALCHbFactory;
import edu.br.ufpe.cin.sword.cm.alchb.mapper.exceptions.IllegalOWLAxiomException;
import edu.br.ufpe.cin.sword.cm.alchb.mapper.exceptions.TypicalityNormalFormException;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbTerm;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
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
        } else if (axiom.isOfType(AxiomType.SUBCLASS_OF)) {
            return this.visit((OWLSubClassOfAxiom) axiom);
        } else {
            throw new IllegalOWLAxiomException(axiom);
        }
    }

    private List<List<ALCHbLiteral>> visit(OWLClassAssertionAxiom axiom) {
        if (!axiom.getClassExpression().isClassExpressionLiteral() || !axiom.getIndividual().isNamed()) {
            throw new TypicalityNormalFormException(axiom);
        }

        var classExpression = axiom.getClassExpression().getNNF();

        if (classExpression.isOWLThing()) {
            // if it is Thing, then the negation is Nothing
            return List.of(List.of());
        } else if (classExpression.isOWLNothing()) {
            // if it is Nothing, then the negation is Thing
            return List.of();
        } else {
            boolean positive = classExpression.isOWLClass();
            String conceptName = positive
                    ? classExpression.asOWLClass().getIRI().getShortForm()
                    : ((OWLObjectComplementOf) classExpression).getOperand().asOWLClass().getIRI().getShortForm();

            return List.of(List.of(alchbFactory.conLiteral(
                    conceptName,
                    !positive,
                    alchbFactory.ind(axiom.getIndividual().toStringID())
            )));
        }
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

    private List<List<ALCHbLiteral>> visit(OWLSubClassOfAxiom axiom) {
        var leftClassExpression = axiom.getSubClass();
        var nnfRightClassExpression = axiom.getSuperClass().getObjectComplementOf().getNNF();

        var x1 = alchbFactory.var(UUID.randomUUID().toString());

        if (nnfRightClassExpression instanceof OWLObjectAllValuesFrom allValuesFrom) {
            return createMatrixWithAllValuesFrom(allValuesFrom, leftClassExpression, x1);
        } else if (leftClassExpression instanceof OWLObjectAllValuesFrom allValuesFrom) {
            return createMatrixWithAllValuesFrom(allValuesFrom, nnfRightClassExpression, x1);
        } else {
            var conjuncts = new ArrayList<OWLClassExpression>();
            conjuncts.addAll(leftClassExpression.asConjunctSet());
            conjuncts.addAll(nnfRightClassExpression.asConjunctSet());

            List<ALCHbLiteral> clause = new ArrayList<>();
            for (var classExpression : conjuncts) {
                if (classExpression.isOWLThing()) {
                    continue;
                }

                if (classExpression instanceof OWLClass owlClass) {
                    clause.add(alchbFactory.conLiteral(owlClass.getIRI().getShortForm(), true, x1));
                } else if (classExpression instanceof OWLObjectComplementOf complementOf) {
                    clause.add(alchbFactory.conLiteral(complementOf.getOperand().asOWLClass().getIRI().getShortForm(), false, x1));
                } else if (classExpression instanceof OWLObjectSomeValuesFrom someValuesFrom) {
                    var y1 = alchbFactory.var(UUID.randomUUID().toString());
                    var propName = someValuesFrom.getProperty().getNamedProperty().getIRI().getShortForm();
                    var fillerIsPositive = someValuesFrom.getFiller().isOWLClass();
                    var fillerClassName = fillerIsPositive
                            ? someValuesFrom.getFiller().asOWLClass().getIRI().getShortForm()
                            : someValuesFrom.getFiller().getComplementNNF().asOWLClass().getIRI().getShortForm();
                    clause.add(alchbFactory.roleLiteral(propName, true, x1, y1));
                    clause.add(alchbFactory.conLiteral(fillerClassName, fillerIsPositive, y1));
                } else {
                    throw new TypicalityNormalFormException(axiom);
                }
            }

            if (clause.isEmpty()) {
                return List.of();
            }

            clause = clause.stream().filter(literal -> !literal.getName().equals("Nothing")).toList();

            return List.of(List.copyOf(clause));
        }
    }

    private List<List<ALCHbLiteral>> createMatrixWithAllValuesFrom(OWLObjectAllValuesFrom allValuesFrom, OWLClassExpression otherLiteralExpression, ALCHbTerm x1) {
        var x2 = alchbFactory.var(UUID.randomUUID().toString());
        var aName = UUID.randomUUID().toString();
        var aX1 = alchbFactory.unaryInd(aName, x1);
        var aX2 = alchbFactory.unaryInd(aName, x2);
        var positive = otherLiteralExpression.isOWLClass();
        var className = positive
                ? otherLiteralExpression.asOWLClass().getIRI().getShortForm()
                : otherLiteralExpression.getComplementNNF().asOWLClass().getIRI().getShortForm();
        var fillerIsPositive = allValuesFrom.getFiller().isOWLClass();
        var fillerClassName = fillerIsPositive
                ? allValuesFrom.getFiller().asOWLClass().getIRI().getShortForm()
                : allValuesFrom.getFiller().getComplementNNF().asOWLClass().getIRI().getShortForm();
        var propName = allValuesFrom.getProperty().getNamedProperty().getIRI().getShortForm();

        return List.of(
                List.of(alchbFactory.conLiteral(className, positive, x1), alchbFactory.roleLiteral(propName, false, x1, aX1)),
                List.of(alchbFactory.conLiteral(className, positive, x2), alchbFactory.conLiteral(fillerClassName, fillerIsPositive, aX2))
        );
    }
}
