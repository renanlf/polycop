package edu.br.ufpe.cin.sword.cm.alchb.mapper;

import edu.br.ufpe.cin.sword.cm.alchb.factories.ALCHbFactory;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbConceptLiteral;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import static org.junit.Assert.*;

public class ALCHbMapOWLAxiomVisitorTest {

    private OWLDataFactory owlDataFactory;
    private ALCHbMapOWLAxiomVisitor visitor;

    @Before
    public void setUp() {
        this.visitor = new ALCHbMapOWLAxiomVisitor(new ALCHbFactory());
        this.owlDataFactory = OWLManager.getOWLDataFactory();
    }

    @Test
    public void testMapPositiveClassAssertionAxiom_expectedNegativeConceptLiteral() {
        OWLAxiom axiom = owlDataFactory.getOWLClassAssertionAxiom(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLNamedIndividual("a"));
        var matrix = this.visitor.visit(axiom);
        assertEquals(1, matrix.size());
        assertEquals(1, matrix.get(0).size());
        assertFalse(matrix.get(0).get(0).isPositive());
        assertEquals("~A", matrix.get(0).get(0).getLiteralName());
        assertEquals("a", ((ALCHbConceptLiteral) matrix.get(0).get(0)).getTerm().getName());
    }

    @Test
    public void testMapNegativeClassAssertionAxiom_expectedPositiveConceptLiteral() {
        OWLAxiom axiom = owlDataFactory.getOWLClassAssertionAxiom(owlDataFactory.getOWLClass("A").getObjectComplementOf(), owlDataFactory.getOWLNamedIndividual("a"));
        var matrix = this.visitor.visit(axiom);
        assertEquals(1, matrix.size());
        assertEquals(1, matrix.get(0).size());
        assertTrue(matrix.get(0).get(0).isPositive());
        assertEquals("A", matrix.get(0).get(0).getLiteralName());
        assertEquals("a", ((ALCHbConceptLiteral) matrix.get(0).get(0)).getTerm().getName());
    }
}
