package edu.br.ufpe.cin.sword.cm.alchb.mapper;

import edu.br.ufpe.cin.sword.cm.alchb.factories.ALCHbFactory;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import org.semanticweb.owlapi.model.*;

import java.util.List;

public class ALCHbMapOWLAxiomVisitor implements OWLAxiomVisitor {

    private List<List<ALCHbLiteral>> matrix;
    private final ALCHbFactory alchbFactory;

    public ALCHbMapOWLAxiomVisitor(ALCHbFactory alchbFactory) {
        this.alchbFactory = alchbFactory;
    }

    public List<List<ALCHbLiteral>> visit(OWLAxiom axiom) {
        if (axiom.isOfType(AxiomType.CLASS_ASSERTION)) {
            this.visit((OWLClassAssertionAxiom) axiom);
        }

        return matrix;
    }

    @Override
    public void visit(OWLClassAssertionAxiom axiom) {
        assert axiom.getClassExpression().isClassExpressionLiteral();
        boolean positive = axiom.getClassExpression().isOWLClass();
        String conceptName = positive
                ? axiom.getClassExpression().asOWLClass().getIRI().getShortForm()
                : ((OWLObjectComplementOf) axiom.getClassExpression()).getOperand().asOWLClass().getIRI().getShortForm();

        this.matrix = List.of(List.of(alchbFactory.conLiteral(
                conceptName,
                !positive,
                alchbFactory.ind(axiom.getIndividual().toStringID())
        )));
    }
}
