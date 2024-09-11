package edu.br.ufpe.cin.sword.cm.alch.mapper;

import edu.br.ufpe.cin.sword.cm.alchb.mapper.ALCHbNormalizeOWLAxiomVisitor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class ALCHbNormalizeOWLAxiomVisitorTest {

    private static UUID uuid;
    private static UUID secondUuid;
    private static MockedStatic<UUID> uuidMock;
    private OWLDataFactory owlDataFactory;
    private ALCHbNormalizeOWLAxiomVisitor visitor;

    @BeforeClass
    public static void setUpBeforeClass() {
        uuid = UUID.randomUUID();
        secondUuid = UUID.randomUUID();
        uuidMock = Mockito.mockStatic(UUID.class);
    }

    @Before
    public void setUpBefore() {
        this.owlDataFactory = OWLManager.getOWLDataFactory();
        this.visitor = new ALCHbNormalizeOWLAxiomVisitor(owlDataFactory);
        uuidMock.when(UUID::randomUUID).thenReturn(uuid, secondUuid);
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

    @Test
    public void testOWLSubClassOfAxiomVisitorWithIntersectionOfOnRightSide_Rule1() {
        OWLSubClassOfAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLObjectIntersectionOf(owlDataFactory.getOWLClass("B"), owlDataFactory.getOWLClass("C").getObjectComplementOf()));
        String expectedSubClassOfAxiom1 = "SubClassOf(<A> <B>)";
        String expectedSubClassOfAxiom2 = "SubClassOf(<A> ObjectComplementOf(<C>))";
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertTrue(normalizedAxioms.isEmpty());
        assertEquals(expectedSubClassOfAxiom1, axiomsToNormalize.get(0).toString());
        assertEquals(expectedSubClassOfAxiom2, axiomsToNormalize.get(1).toString());
    }

    @Test
    public void testOWLSubClassOfAxiomVisitorWithNNFUnionOfOnRightSide_Rule1() {
        OWLSubClassOfAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLObjectUnionOf(owlDataFactory.getOWLClass("B"), owlDataFactory.getOWLClass("C").getObjectComplementOf()).getObjectComplementOf());
        String expectedSubClassOfAxiom1 = "SubClassOf(<A> <C>)";
        String expectedSubClassOfAxiom2 = "SubClassOf(<A> ObjectComplementOf(<B>))";
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertTrue(normalizedAxioms.isEmpty());
        assertEquals(expectedSubClassOfAxiom1, axiomsToNormalize.get(0).toString());
        assertEquals(expectedSubClassOfAxiom2, axiomsToNormalize.get(1).toString());
    }

    @Test
    public void testOWLSubClassOfAxiomVisitorWithIntersectionOfOnRightSide_Rule8() {
        OWLSubClassOfAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLObjectUnionOf(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLClass("B").getObjectComplementOf()), owlDataFactory.getOWLClass("C"));
        String expectedSubClassOfAxiom1 = "SubClassOf(<A> <C>)";
        String expectedSubClassOfAxiom2 = "SubClassOf(ObjectComplementOf(<B>) <C>)";
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertTrue(normalizedAxioms.isEmpty());
        assertEquals(expectedSubClassOfAxiom1, axiomsToNormalize.get(0).toString());
        assertEquals(expectedSubClassOfAxiom2, axiomsToNormalize.get(1).toString());
    }

    @Test
    public void testOWLSubClassOfAxiomVisitorWithUnionOfWithinNonLiteralInAllValuesFiller_Rule3() {
        OWLSubClassOfAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLObjectUnionOf(owlDataFactory.getOWLClass("D"), owlDataFactory.getOWLObjectAllValuesFrom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLObjectIntersectionOf(owlDataFactory.getOWLClass("B"), owlDataFactory.getOWLClass("C")))));
        String expectedSubClassOfAxiom = String.format("SubClassOf(<A> ObjectUnionOf(<D> ObjectAllValuesFrom(<r> <%s>)))", uuid);
        String expectedEquivalenceAxiom = String.format("EquivalentClasses(<%s> ObjectIntersectionOf(<B> <C>))", uuid);
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertEquals(1, normalizedAxioms.size());
        assertEquals(1, axiomsToNormalize.size());
        assertEquals(expectedSubClassOfAxiom, normalizedAxioms.get(0).toString());
        assertEquals(expectedEquivalenceAxiom, axiomsToNormalize.get(0).toString());
    }

    @Test
    public void testOWLSubClassOfAxiomVisitorWithNonLiteralInAllValuesFiller_Rule3() {
        OWLSubClassOfAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLObjectAllValuesFrom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLObjectIntersectionOf(owlDataFactory.getOWLClass("B"), owlDataFactory.getOWLClass("C"))));
        String expectedSubClassOfAxiom = String.format("SubClassOf(<A> ObjectAllValuesFrom(<r> <%s>))", uuid);
        String expectedEquivalenceAxiom = String.format("EquivalentClasses(<%s> ObjectIntersectionOf(<B> <C>))", uuid);
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertEquals(1, normalizedAxioms.size());
        assertEquals(1, axiomsToNormalize.size());
        assertEquals(expectedSubClassOfAxiom, normalizedAxioms.get(0).toString());
        assertEquals(expectedEquivalenceAxiom, axiomsToNormalize.get(0).toString());
    }

    @Test
    public void testOWLSubClassOfAxiomVisitorWithUnionOfWithinConjunction_Rule4() {
        OWLSubClassOfAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLObjectUnionOf(owlDataFactory.getOWLClass("D"), owlDataFactory.getOWLObjectIntersectionOf(owlDataFactory.getOWLClass("B"), owlDataFactory.getOWLClass("C"))));
        String expectedSubClassOfAxiom = String.format("SubClassOf(<A> ObjectUnionOf(<D> <%s>))", uuid);
        String expectedEquivalenceAxiom = String.format("EquivalentClasses(<%s> ObjectIntersectionOf(<B> <C>))", uuid);
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertEquals(1, normalizedAxioms.size());
        assertEquals(1, axiomsToNormalize.size());
        assertEquals(expectedSubClassOfAxiom, normalizedAxioms.get(0).toString());
        assertEquals(expectedEquivalenceAxiom, axiomsToNormalize.get(0).toString());
    }

    @Test
    public void testOWLSubClassOfAxiomVisitorWithUnionOfWithinConjunctionAndNonLiteralInAllValuesFiller_Rule3And4() {
        OWLSubClassOfAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLObjectUnionOf(
                owlDataFactory.getOWLClass("D"),
                owlDataFactory.getOWLObjectIntersectionOf(owlDataFactory.getOWLClass("B"), owlDataFactory.getOWLClass("C")),
                owlDataFactory.getOWLObjectAllValuesFrom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLObjectIntersectionOf(owlDataFactory.getOWLClass("E"), owlDataFactory.getOWLClass("F")))));
        String expectedSubClassOfAxiom = String.format("SubClassOf(<A> ObjectUnionOf(<D> <%s> ObjectAllValuesFrom(<r> <%s>)))", uuid, secondUuid);
        String expectedEquivalenceAxiom1 = String.format("EquivalentClasses(<%s> ObjectIntersectionOf(<B> <C>))", uuid);
        String expectedEquivalenceAxiom2 = String.format("EquivalentClasses(<%s> ObjectIntersectionOf(<E> <F>))", secondUuid);
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertEquals(1, normalizedAxioms.size());
        assertEquals(2, axiomsToNormalize.size());
        assertEquals(expectedSubClassOfAxiom, normalizedAxioms.get(0).toString());
        assertEquals(expectedEquivalenceAxiom1, axiomsToNormalize.get(0).toString());
        assertEquals(expectedEquivalenceAxiom2, axiomsToNormalize.get(1).toString());
    }

    @Test
    public void testOWLSubClassOfAxiomVisitorWithNonLiteralInSomeValuesFiller_Rule5() {
        OWLSubClassOfAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLObjectSomeValuesFrom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLObjectIntersectionOf(owlDataFactory.getOWLClass("B"), owlDataFactory.getOWLClass("C"))));
        String expectedSubClassOfAxiom = String.format("SubClassOf(<A> ObjectSomeValuesFrom(<r> <%s>))", uuid);
        String expectedEquivalenceAxiom = String.format("EquivalentClasses(<%s> ObjectIntersectionOf(<B> <C>))", uuid);
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertEquals(1, normalizedAxioms.size());
        assertEquals(1, axiomsToNormalize.size());
        assertEquals(expectedSubClassOfAxiom, normalizedAxioms.get(0).toString());
        assertEquals(expectedEquivalenceAxiom, axiomsToNormalize.get(0).toString());
    }

    @Test
    public void testOWLSubClassOfAxiomVisitorWithIntersectionOfWithinNonLiteralInSomeValuesFiller_Rule9() {
        OWLSubClassOfAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLObjectIntersectionOf(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLObjectSomeValuesFrom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLObjectUnionOf(owlDataFactory.getOWLClass("B"), owlDataFactory.getOWLClass("C")))), owlDataFactory.getOWLClass("D"));
        String expectedSubClassOfAxiom = String.format("SubClassOf(ObjectIntersectionOf(<A> ObjectSomeValuesFrom(<r> <%s>)) <D>)", uuid);
        String expectedEquivalenceAxiom = String.format("EquivalentClasses(<%s> ObjectUnionOf(<B> <C>))", uuid);
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertEquals(1, normalizedAxioms.size());
        assertEquals(1, axiomsToNormalize.size());
        assertEquals(expectedSubClassOfAxiom, normalizedAxioms.get(0).toString());
        assertEquals(expectedEquivalenceAxiom, axiomsToNormalize.get(0).toString());
    }

    @Test
    public void testOWLSubClassOfAxiomVisitorWithIntersectionOfWithinDisjunction_Rule10() {
        OWLSubClassOfAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLObjectIntersectionOf(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLObjectUnionOf(owlDataFactory.getOWLClass("B"), owlDataFactory.getOWLClass("C"))), owlDataFactory.getOWLClass("D"));
        String expectedSubClassOfAxiom = String.format("SubClassOf(ObjectIntersectionOf(<A> <%s>) <D>)", uuid);
        String expectedEquivalenceAxiom = String.format("EquivalentClasses(<%s> ObjectUnionOf(<B> <C>))", uuid);
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertEquals(1, normalizedAxioms.size());
        assertEquals(1, axiomsToNormalize.size());
        assertEquals(expectedSubClassOfAxiom, normalizedAxioms.get(0).toString());
        assertEquals(expectedEquivalenceAxiom, axiomsToNormalize.get(0).toString());
    }

    @Test
    public void testOWLSubClassOfAxiomVisitorWithIntersectionOfWithinDisjunctionAndNonLiteralInSomeValuesFiller_Rule9And10_() {
        OWLSubClassOfAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLObjectIntersectionOf(
                    owlDataFactory.getOWLClass("A"),
                    owlDataFactory.getOWLObjectUnionOf(owlDataFactory.getOWLClass("B"), owlDataFactory.getOWLClass("C")),
                    owlDataFactory.getOWLObjectSomeValuesFrom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLObjectUnionOf(owlDataFactory.getOWLClass("D"), owlDataFactory.getOWLClass("E")))),
                owlDataFactory.getOWLClass("F"));
        String expectedSubClassOfAxiom = String.format("SubClassOf(ObjectIntersectionOf(<A> <%s> ObjectSomeValuesFrom(<r> <%s>)) <F>)", uuid, secondUuid);
        String expectedEquivalenceAxiom1 = String.format("EquivalentClasses(<%s> ObjectUnionOf(<B> <C>))", uuid);
        String expectedEquivalenceAxiom2 = String.format("EquivalentClasses(<%s> ObjectUnionOf(<D> <E>))", secondUuid);
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertEquals(1, normalizedAxioms.size());
        assertEquals(2, axiomsToNormalize.size());
        assertEquals(expectedSubClassOfAxiom, normalizedAxioms.get(0).toString());
        assertEquals(expectedEquivalenceAxiom1, axiomsToNormalize.get(0).toString());
        assertEquals(expectedEquivalenceAxiom2, axiomsToNormalize.get(1).toString());
    }

    @Test
    public void testOWLSubClassOfAxiomVisitorWithNonLiteralInSomeValuesFiller_Rule10() {
        OWLSubClassOfAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLObjectSomeValuesFrom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLObjectIntersectionOf(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLClass("B"))), owlDataFactory.getOWLClass("C"));
        String expectedSubClassOfAxiom = String.format("SubClassOf(ObjectSomeValuesFrom(<r> <%s>) <C>)", uuid);
        String expectedEquivalenceAxiom = String.format("EquivalentClasses(<%s> ObjectIntersectionOf(<A> <B>))", uuid);
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertEquals(1, normalizedAxioms.size());
        assertEquals(1, axiomsToNormalize.size());
        assertEquals(expectedSubClassOfAxiom, normalizedAxioms.get(0).toString());
        assertEquals(expectedEquivalenceAxiom, axiomsToNormalize.get(0).toString());
    }

    @Test
    public void testOWLSubClassOfAxiomVisitorWithNonLiteralInAllValuesFiller_Rule11() {
        OWLSubClassOfAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(owlDataFactory.getOWLObjectAllValuesFrom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLObjectIntersectionOf(owlDataFactory.getOWLClass("A"), owlDataFactory.getOWLClass("B"))), owlDataFactory.getOWLClass("C"));
        String expectedSubClassOfAxiom = String.format("SubClassOf(ObjectAllValuesFrom(<r> <%s>) <C>)", uuid);
        String expectedEquivalenceAxiom = String.format("EquivalentClasses(<%s> ObjectIntersectionOf(<A> <B>))", uuid);
        this.visitor.visit(axiom);
        var visit = this.visitor.getVisitResult();
        var normalizedAxioms = visit.getNormalizedAxioms();
        var axiomsToNormalize = visit.getAxiomsToNormalize();

        assertEquals(1, normalizedAxioms.size());
        assertEquals(1, axiomsToNormalize.size());
        assertEquals(expectedSubClassOfAxiom, normalizedAxioms.get(0).toString());
        assertEquals(expectedEquivalenceAxiom, axiomsToNormalize.get(0).toString());
    }

}
