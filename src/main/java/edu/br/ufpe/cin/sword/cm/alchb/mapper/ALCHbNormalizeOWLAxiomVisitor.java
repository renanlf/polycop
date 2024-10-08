package edu.br.ufpe.cin.sword.cm.alchb.mapper;

import edu.br.ufpe.cin.sword.cm.alchb.mapper.exceptions.IllegalOWLAxiomException;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class ALCHbNormalizeOWLAxiomVisitor implements OWLAxiomVisitor {

    private static final List<ClassExpressionType> CONJUNCTIONS = List.of(ClassExpressionType.OBJECT_INTERSECTION_OF, ClassExpressionType.OBJECT_SOME_VALUES_FROM);
    private static final List<ClassExpressionType> DISJUNCTIONS = List.of(ClassExpressionType.OBJECT_UNION_OF, ClassExpressionType.OBJECT_ALL_VALUES_FROM);

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
        } else if (axiom.isOfType(AxiomType.SUBCLASS_OF)) {
            this.visit((OWLSubClassOfAxiom) axiom);
        }else if (axiom.isOfType(AxiomType.DISJOINT_CLASSES)) {
            this.visit((OWLDisjointClassesAxiom) axiom);
        } else {
            throw new IllegalOWLAxiomException(axiom);
        }

        return visitResult;
    }

    @Override
    public void visit(OWLClassAssertionAxiom axiom) {
        var classExpression = axiom.getClassExpression().getNNF();

        if (classExpression.isClassExpressionLiteral()) {
            this.visitResult = new ALCHbOWLAxiomVisit(List.of(axiom), List.of());
        } else {
            OWLClass newClass = owlDataFactory.getOWLClass(UUID.randomUUID().toString());
            this.visitResult = new ALCHbOWLAxiomVisit(
                    List.of(owlDataFactory.getOWLClassAssertionAxiom(newClass, axiom.getIndividual())),
                    List.of(owlDataFactory.getOWLEquivalentClassesAxiom(newClass, classExpression))
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
    public void visit(OWLDisjointClassesAxiom axiom) {
        this.visitResult = new ALCHbOWLAxiomVisit(List.of(), axiom.asOWLSubClassOfAxioms().stream().toList());
    }

    @Override
    public void visit(OWLSubClassOfAxiom axiom) {
        var leftSideClassExpression = axiom.getSubClass().getNNF();
        var rightSideClassExpression = axiom.getSuperClass().getNNF();
        if (rightSideClassExpression.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
            // RULE 2
            var axiomsToNormalize = ((OWLObjectIntersectionOf) rightSideClassExpression).operands().map(operand -> owlDataFactory.getOWLSubClassOfAxiom(leftSideClassExpression, operand)).toList();
            this.visitResult = new ALCHbOWLAxiomVisit(List.of(), axiomsToNormalize);
        } else if (rightSideClassExpression.getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
            // RULE 3 and 4 (inside union)
            checkRule3And4(leftSideClassExpression, (OWLObjectUnionOf) rightSideClassExpression);
        } else if (rightSideClassExpression instanceof  OWLObjectAllValuesFrom allValuesFrom && !allValuesFrom.getFiller().isClassExpressionLiteral()) {
            // RULE 3 (without union)
            var newClass = owlDataFactory.getOWLClass(UUID.randomUUID().toString());
            this.visitResult = new ALCHbOWLAxiomVisit(
                    List.of(owlDataFactory.getOWLSubClassOfAxiom(leftSideClassExpression, owlDataFactory.getOWLObjectAllValuesFrom(allValuesFrom.getProperty(), newClass))),
                    List.of(owlDataFactory.getOWLEquivalentClassesAxiom(newClass, allValuesFrom.getFiller()))
            );
        } else if (rightSideClassExpression instanceof  OWLObjectSomeValuesFrom someValuesFrom && !someValuesFrom.getFiller().isClassExpressionLiteral()) {
            // RULE 5
            var newClass = owlDataFactory.getOWLClass(UUID.randomUUID().toString());
            this.visitResult = new ALCHbOWLAxiomVisit(
                    List.of(owlDataFactory.getOWLSubClassOfAxiom(leftSideClassExpression, owlDataFactory.getOWLObjectSomeValuesFrom(someValuesFrom.getProperty(), newClass))),
                    List.of(owlDataFactory.getOWLEquivalentClassesAxiom(newClass, someValuesFrom.getFiller()))
            );
        } else if (leftSideClassExpression.getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
            // RULE 8
            var axiomsToNormalize = ((OWLObjectUnionOf) leftSideClassExpression).operands().map(operand -> owlDataFactory.getOWLSubClassOfAxiom(operand, rightSideClassExpression)).toList();
            this.visitResult = new ALCHbOWLAxiomVisit(List.of(), axiomsToNormalize);
        } else if (leftSideClassExpression.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
            // RULE 9 and 10 (inside intersection)
            checkRule9And10((OWLObjectIntersectionOf) leftSideClassExpression, rightSideClassExpression);
        } else if (leftSideClassExpression instanceof  OWLObjectSomeValuesFrom someValuesFrom && !someValuesFrom.getFiller().isClassExpressionLiteral()) {
            // RULE 10 (without intersection)
            var newClass = owlDataFactory.getOWLClass(UUID.randomUUID().toString());
            this.visitResult = new ALCHbOWLAxiomVisit(
                    List.of(owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLObjectSomeValuesFrom(someValuesFrom.getProperty(), newClass), rightSideClassExpression)),
                    List.of(owlDataFactory.getOWLEquivalentClassesAxiom(newClass, someValuesFrom.getFiller()))
            );
        } else if (leftSideClassExpression instanceof  OWLObjectAllValuesFrom allValuesFrom && !allValuesFrom.getFiller().isClassExpressionLiteral()) {
            // RULE 11
            var newClass = owlDataFactory.getOWLClass(UUID.randomUUID().toString());
            this.visitResult = new ALCHbOWLAxiomVisit(
                    List.of(owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLObjectAllValuesFrom(allValuesFrom.getProperty(), newClass), rightSideClassExpression)),
                    List.of(owlDataFactory.getOWLEquivalentClassesAxiom(newClass, allValuesFrom.getFiller()))
            );
        } else if (!leftSideClassExpression.isClassExpressionLiteral() && rightSideClassExpression instanceof OWLQuantifiedObjectRestriction quantifiedObjectRestriction && quantifiedObjectRestriction.getFiller().isClassExpressionLiteral()) {
            var newClass = owlDataFactory.getOWLClass(UUID.randomUUID().toString());
            this.visitResult = new ALCHbOWLAxiomVisit(
                    List.of(owlDataFactory.getOWLSubClassOfAxiom(newClass, rightSideClassExpression)),
                    List.of(owlDataFactory.getOWLEquivalentClassesAxiom(newClass, leftSideClassExpression))
            );
        } else if (!rightSideClassExpression.isClassExpressionLiteral() && leftSideClassExpression instanceof OWLQuantifiedObjectRestriction quantifiedObjectRestriction && quantifiedObjectRestriction.getFiller().isClassExpressionLiteral()) {
            var newClass = owlDataFactory.getOWLClass(UUID.randomUUID().toString());
            this.visitResult = new ALCHbOWLAxiomVisit(
                    List.of(owlDataFactory.getOWLSubClassOfAxiom(leftSideClassExpression, newClass)),
                    List.of(owlDataFactory.getOWLEquivalentClassesAxiom(newClass, rightSideClassExpression))
            );
        } else {
            this.visitResult = new ALCHbOWLAxiomVisit(List.of(axiom), List.of());
        }
    }

    private void checkRule3And4(OWLClassExpression leftSideClassExpression, OWLObjectUnionOf rightSideClassExpression) {
        var pureDisjunctions = new HashSet<OWLClassExpression>();
        var newAxioms = new ArrayList<OWLEquivalentClassesAxiom>();
        for (var classExpression : rightSideClassExpression.getOperands()) {
            if (classExpression.isClassExpressionLiteral()) {
                pureDisjunctions.add(classExpression);
            } else if (classExpression instanceof OWLObjectAllValuesFrom allValues && !allValues.getFiller().isClassExpressionLiteral()) {
                var newClass = owlDataFactory.getOWLClass(UUID.randomUUID().toString());
                pureDisjunctions.add(owlDataFactory.getOWLObjectAllValuesFrom(allValues.getProperty(), newClass));
                newAxioms.add(owlDataFactory.getOWLEquivalentClassesAxiom(newClass, allValues.getFiller()));
            } else if (CONJUNCTIONS.contains(classExpression.getClassExpressionType())) {
                var newClass = owlDataFactory.getOWLClass(UUID.randomUUID().toString());
                pureDisjunctions.add(newClass);
                newAxioms.add(owlDataFactory.getOWLEquivalentClassesAxiom(newClass, classExpression));
            }
        }
        this.visitResult = new ALCHbOWLAxiomVisit(
                List.of(owlDataFactory.getOWLSubClassOfAxiom(leftSideClassExpression, owlDataFactory.getOWLObjectUnionOf(pureDisjunctions))),
                newAxioms);
    }

    private void checkRule9And10(OWLObjectIntersectionOf leftSideClassExpression, OWLClassExpression rightSideClassExpression) {
        var pureConjunctions = new HashSet<OWLClassExpression>();
        var newAxioms = new ArrayList<OWLEquivalentClassesAxiom>();
        for (var classExpression : leftSideClassExpression.getOperands()) {
            if (classExpression instanceof OWLObjectSomeValuesFrom someValues && !someValues.getFiller().isClassExpressionLiteral()) {
                var newClass = owlDataFactory.getOWLClass(UUID.randomUUID().toString());
                pureConjunctions.add(owlDataFactory.getOWLObjectSomeValuesFrom(someValues.getProperty(), newClass));
                newAxioms.add(owlDataFactory.getOWLEquivalentClassesAxiom(newClass, someValues.getFiller()));
            } else if (DISJUNCTIONS.contains(classExpression.getClassExpressionType())) {
                var newClass = owlDataFactory.getOWLClass(UUID.randomUUID().toString());
                pureConjunctions.add(newClass);
                newAxioms.add(owlDataFactory.getOWLEquivalentClassesAxiom(newClass, classExpression));
            } else {
                pureConjunctions.add(classExpression);
            }
        }
        this.visitResult = new ALCHbOWLAxiomVisit(
                List.of(owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLObjectIntersectionOf(pureConjunctions), rightSideClassExpression)),
                newAxioms);
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
