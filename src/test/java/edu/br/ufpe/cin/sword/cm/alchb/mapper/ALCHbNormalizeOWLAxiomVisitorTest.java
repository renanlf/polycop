package edu.br.ufpe.cin.sword.cm.alchb.mapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;

import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class ALCHbNormalizeOWLAxiomVisitorTest {

    private OWLDataFactory owlDataFactory;
    private ALCHbNormalizeOWLAxiomVisitor visitor;

    @Before
    public void setUpBefore() throws Exception {
        this.owlDataFactory = OWLManager.getOWLDataFactory();
        this.visitor = new ALCHbNormalizeOWLAxiomVisitor(owlDataFactory);
    }

    @Test
    public void testOWLClassAssertionAxiomVisitor() {
        // GIVEN
        OWLAxiom axiom = owlDataFactory.getOWLClassAssertionAxiom(
                owlDataFactory.getOWLObjectUnionOf(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLObjectSomeValuesFrom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLClass("B"))), owlDataFactory.getOWLNamedIndividual("a"));
        UUID uuid = UUID.randomUUID();
        Mockito.mockStatic(UUID.class).when(UUID::randomUUID).thenReturn(uuid);
        String expectedClassAssertionAxiom = String.format("ClassAssertion(<%s> <a>)", uuid.toString());
        String expectedClassEquivalenceAxiom = String.format("EquivalentClasses(<%s> ObjectUnionOf(<A> ObjectSomeValuesFrom(<r> <B>)))", uuid.toString());

        // WHEN
        var visitResult = this.visitor.visit(axiom);

        // THEN
        assertEquals(1, visitResult.getNormalizedAxioms().size());
        assertEquals(1, visitResult.getAxiomsToNormalize().size());
        assertEquals(expectedClassAssertionAxiom, visitResult.getNormalizedAxioms().get(0).toString());
        assertEquals(expectedClassEquivalenceAxiom, visitResult.getAxiomsToNormalize().get(0).toString());
    }

    @Test
    public void testOWLEquivalentClassesAxiomVisitorWith2Classes() {
        OWLEquivalentClassesAxiom axiom = owlDataFactory.getOWLEquivalentClassesAxiom(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLClass("B"));
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertTrue(normalizedAxioms.isEmpty());
        assertEquals(2, axiomsToNormalize.size());
    }

    @Test
    public void testOWLEquivalentClassesAxiomVisitorWith3Classes() {
        OWLEquivalentClassesAxiom axiom = owlDataFactory.getOWLEquivalentClassesAxiom(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLClass("B"), owlDataFactory.getOWLClass("C"));
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertTrue(normalizedAxioms.isEmpty());
        assertEquals(6, axiomsToNormalize.size());
    }
}
