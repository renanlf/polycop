package edu.br.ufpe.cin.sword.cm.alchb.mapper;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;

import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class ALCHbNormalizeOWLAxiomVisitorTest {

    private static UUID uuid;
    private OWLDataFactory owlDataFactory;
    private ALCHbNormalizeOWLAxiomVisitor visitor;

    @BeforeClass
    public static void setUpBeforeClass() {
        uuid = UUID.randomUUID();
        MockedStatic<UUID> uuidMock = Mockito.mockStatic(UUID.class);
        uuidMock.when(UUID::randomUUID).thenReturn(uuid);
    }

    @Before
    public void setUpBefore() {
        this.owlDataFactory = OWLManager.getOWLDataFactory();
        this.visitor = new ALCHbNormalizeOWLAxiomVisitor(owlDataFactory);
    }

    @Test
    public void testOWLClassAssertionAxiomVisitorWithNonLiteral() {
        // GIVEN
        OWLAxiom axiom = owlDataFactory.getOWLClassAssertionAxiom(
                owlDataFactory.getOWLObjectUnionOf(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLObjectSomeValuesFrom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLClass("B"))), owlDataFactory.getOWLNamedIndividual("a"));

        String expectedClassAssertionAxiom = String.format("ClassAssertion(<%s> <a>)", uuid);
        String expectedClassEquivalenceAxiom = String.format("EquivalentClasses(<%s> ObjectUnionOf(<A> ObjectSomeValuesFrom(<r> <B>)))", uuid);

        // WHEN
        var visitResult = this.visitor.visit(axiom);

        // THEN
        assertEquals(1, visitResult.getNormalizedAxioms().size());
        assertEquals(1, visitResult.getAxiomsToNormalize().size());
        assertEquals(expectedClassAssertionAxiom, visitResult.getNormalizedAxioms().get(0).toString());
        assertEquals(expectedClassEquivalenceAxiom, visitResult.getAxiomsToNormalize().get(0).toString());
    }

    @Test
    public void testOWLClassAssertionAxiomVisitorWithPositiveLiteral() {
        // GIVEN
        OWLAxiom axiom = owlDataFactory.getOWLClassAssertionAxiom(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLNamedIndividual("a"));
        String expectedClassAssertionAxiom = "ClassAssertion(<A> <a>)";

        // WHEN
        var visitResult = this.visitor.visit(axiom);

        // THEN
        assertEquals(1, visitResult.getNormalizedAxioms().size());
        assertEquals(0, visitResult.getAxiomsToNormalize().size());
        assertEquals(expectedClassAssertionAxiom, visitResult.getNormalizedAxioms().get(0).toString());
    }

    @Test
    public void testOWLClassAssertionAxiomVisitorWithNegativeLiteral() {
        // GIVEN
        OWLAxiom axiom = owlDataFactory.getOWLClassAssertionAxiom(owlDataFactory.getOWLClass("A").getObjectComplementOf(), owlDataFactory.getOWLNamedIndividual("a"));
        String expectedClassAssertionAxiom = "ClassAssertion(ObjectComplementOf(<A>) <a>)";

        // WHEN
        var visitResult = this.visitor.visit(axiom);

        // THEN
        assertEquals(1, visitResult.getNormalizedAxioms().size());
        assertEquals(0, visitResult.getAxiomsToNormalize().size());
        assertEquals(expectedClassAssertionAxiom, visitResult.getNormalizedAxioms().get(0).toString());
    }

    @Test
    public void testOWLObjectPropertyAssertionAxiomVisitorWithLiteral() {
        // GIVEN
        OWLAxiom axiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLNamedIndividual("a"), owlDataFactory.getOWLNamedIndividual("b"));
        String expectedClassAssertionAxiom = "ObjectPropertyAssertion(<r> <a> <b>)";

        // WHEN
        var visitResult = this.visitor.visit(axiom);

        // THEN
        assertEquals(1, visitResult.getNormalizedAxioms().size());
        assertEquals(0, visitResult.getAxiomsToNormalize().size());
        assertEquals(expectedClassAssertionAxiom, visitResult.getNormalizedAxioms().get(0).toString());
    }

    @Test
    public void testOWLObjectPropertyAssertionAxiomVisitorWithTypicalLiteral() {
        // GIVEN
        OWLAxiom axiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(owlDataFactory.getOWLObjectProperty("r").getInverseProperty(), owlDataFactory.getOWLNamedIndividual("a"), owlDataFactory.getOWLNamedIndividual("b"));
        String expectedObjectPropertyAssertion = String.format("ObjectPropertyAssertion(<%s> <a> <b>)", uuid);
        String expectedSubObjectPropertyAxiom = String.format("SubObjectPropertyOf(<%s> ObjectInverseOf(<r>))", uuid);

        // WHEN
        var visitResult = this.visitor.visit(axiom);

        // THEN
        assertEquals(1, visitResult.getNormalizedAxioms().size());
        assertEquals(1, visitResult.getAxiomsToNormalize().size());
        assertEquals(expectedObjectPropertyAssertion, visitResult.getNormalizedAxioms().get(0).toString());
        assertEquals(expectedSubObjectPropertyAxiom, visitResult.getAxiomsToNormalize().get(0).toString());
    }

    @Test
    public void testOWLSubObjectPropertyOfAxiomVisitorWithTypicalLiteralInOneHand() {
        // GIVEN
        OWLAxiom axiom = owlDataFactory.getOWLSubObjectPropertyOfAxiom(owlDataFactory.getOWLObjectProperty("r").getInverseProperty(), owlDataFactory.getOWLObjectProperty("s").getInverseProperty());
        String expectedSubObjectPropertyAxiom1 = String.format("SubObjectPropertyOf(ObjectInverseOf(<r>) <%s>)", uuid);
        String expectedSubObjectPropertyAxiom2 = String.format("SubObjectPropertyOf(<%s> ObjectInverseOf(<s>))", uuid);

        // WHEN
        var visitResult = this.visitor.visit(axiom);

        // THEN
        assertEquals(2, visitResult.getNormalizedAxioms().size());
        assertEquals(0, visitResult.getAxiomsToNormalize().size());
        assertEquals(expectedSubObjectPropertyAxiom1, visitResult.getNormalizedAxioms().get(0).toString());
        assertEquals(expectedSubObjectPropertyAxiom2, visitResult.getNormalizedAxioms().get(1).toString());
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
