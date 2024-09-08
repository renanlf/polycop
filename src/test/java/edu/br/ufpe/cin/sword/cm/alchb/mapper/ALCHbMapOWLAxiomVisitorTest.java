package edu.br.ufpe.cin.sword.cm.alchb.mapper;

import edu.br.ufpe.cin.sword.cm.alchb.factories.ALCHbFactory;
import edu.br.ufpe.cin.sword.cm.alchb.mapper.exceptions.TypicalityNormalFormException;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbConceptLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbRoleLiteral;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.UUID;

import static org.junit.Assert.*;

public class ALCHbMapOWLAxiomVisitorTest {

    private static UUID uuid;
    private static UUID secondUuid;
    private static UUID thirdUuid;
    private static UUID fourthUuid;
    private static MockedStatic<UUID> uuidMock;
    private OWLDataFactory owlDataFactory;
    private ALCHbMapOWLAxiomVisitor visitor;

    @BeforeClass
    public static void setUpBeforeClass() {
        uuid = UUID.randomUUID();
        secondUuid = UUID.randomUUID();
        thirdUuid = UUID.randomUUID();
        fourthUuid = UUID.randomUUID();
        uuidMock = Mockito.mockStatic(UUID.class);
    }

    @Before
    public void setUp() {
        this.visitor = new ALCHbMapOWLAxiomVisitor(new ALCHbFactory());
        this.owlDataFactory = OWLManager.getOWLDataFactory();
        uuidMock.when(UUID::randomUUID).thenReturn(uuid, secondUuid, thirdUuid, fourthUuid);
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

    @Test
    public void testMapNonLiteralClassAssertionAxiom_expectedTypicalityNormalFormExceptionThrown() {
        OWLAxiom axiom = owlDataFactory.getOWLClassAssertionAxiom(owlDataFactory.getOWLObjectSomeValuesFrom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLClass("A")), owlDataFactory.getOWLNamedIndividual("a"));
        try {
            this.visitor.visit(axiom);
            fail();
        } catch (TypicalityNormalFormException e) {
            assertTrue(e.getMessage().startsWith(axiom.toString()));
        }
    }

    @Test
    public void testMapPositivePropertyAssertionAxiom_expectedNegativeRoleLiteral() {
        OWLAxiom axiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLNamedIndividual("a"), owlDataFactory.getOWLNamedIndividual("b"));
        var matrix = this.visitor.visit(axiom);
        assertEquals(1, matrix.size());
        assertEquals(1, matrix.get(0).size());
        assertFalse(matrix.get(0).get(0).isPositive());
        assertEquals("~r", matrix.get(0).get(0).getLiteralName());
        assertEquals("a", ((ALCHbRoleLiteral) matrix.get(0).get(0)).getFirst().getName());
        assertEquals("b", ((ALCHbRoleLiteral) matrix.get(0).get(0)).getSecond().getName());
    }

    @Test
    public void testMapNegativePropertyAssertionAxiom_expectedNegativeRoleLiteral() {
        OWLAxiom axiom = owlDataFactory.getOWLNegativeObjectPropertyAssertionAxiom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLNamedIndividual("a"), owlDataFactory.getOWLNamedIndividual("b"));
        var matrix = this.visitor.visit(axiom);
        assertEquals(1, matrix.size());
        assertEquals(1, matrix.get(0).size());
        assertTrue(matrix.get(0).get(0).isPositive());
        assertEquals("r", matrix.get(0).get(0).getLiteralName());
        assertEquals("a", ((ALCHbRoleLiteral) matrix.get(0).get(0)).getFirst().getName());
        assertEquals("b", ((ALCHbRoleLiteral) matrix.get(0).get(0)).getSecond().getName());
    }

    @Test
    public void testMapSubPropertyOfAxiom_expectedSubPositiveRoleLiteralAndSuperNegativeRoleLiteral() {
        OWLAxiom axiom = owlDataFactory.getOWLSubObjectPropertyOfAxiom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLObjectProperty("s"));
        var matrix = this.visitor.visit(axiom);
        assertEquals(1, matrix.size());
        assertEquals(2, matrix.get(0).size());
        assertTrue(matrix.get(0).get(0).isPositive());
        assertEquals("r", matrix.get(0).get(0).getLiteralName());
        assertEquals(uuid.toString(), ((ALCHbRoleLiteral) matrix.get(0).get(0)).getFirst().getName());
        assertEquals(secondUuid.toString(), ((ALCHbRoleLiteral) matrix.get(0).get(0)).getSecond().getName());
        assertFalse(matrix.get(0).get(1).isPositive());
        assertEquals("~s", matrix.get(0).get(1).getLiteralName());
        assertEquals(uuid.toString(), ((ALCHbRoleLiteral) matrix.get(0).get(1)).getFirst().getName());
        assertEquals(secondUuid.toString(), ((ALCHbRoleLiteral) matrix.get(0).get(1)).getSecond().getName());
    }

    @Test
    public void testMapSubClassOfAxiom_LeftSidePureConjunctionRightSidePureDisjunction() {
        OWLAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(
                owlDataFactory.getOWLObjectIntersectionOf(
                        owlDataFactory.getOWLClass("A"),
                        owlDataFactory.getOWLObjectSomeValuesFrom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLClass("B"))
                ), owlDataFactory.getOWLObjectUnionOf(
                        owlDataFactory.getOWLClass("C"),
                        owlDataFactory.getOWLObjectAllValuesFrom(owlDataFactory.getOWLObjectProperty("s"), owlDataFactory.getOWLClass("D").getObjectComplementOf())
                )
        );

        var matrix = this.visitor.visit(axiom);
        assertEquals(1, matrix.size());
        assertEquals(6, matrix.get(0).size());
        assertTrue(matrix.get(0).get(0).isPositive());
        assertEquals(String.format("A(%s)", uuid), matrix.get(0).get(0).toString());
        assertEquals(String.format("r(%s, %s)", uuid, secondUuid), matrix.get(0).get(1).toString());
        assertEquals(String.format("B(%s)", secondUuid), matrix.get(0).get(2).toString());
        assertEquals(String.format("\\nao C(%s)", uuid), matrix.get(0).get(3).toString());
        assertEquals(String.format("s(%s, %s)", uuid, thirdUuid), matrix.get(0).get(4).toString());
        assertEquals(String.format("D(%s)", thirdUuid), matrix.get(0).get(5).toString());
    }

    @Test
    public void testMapSubClassOfAxiom_LeftSideLiteralRightSideSomeValuesFrom() {
        OWLAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(
                owlDataFactory.getOWLClass("A").getObjectComplementOf(),
                owlDataFactory.getOWLObjectSomeValuesFrom(owlDataFactory.getOWLObjectProperty("s"), owlDataFactory.getOWLClass("B"))
        );

        var matrix = this.visitor.visit(axiom);
        assertEquals(2, matrix.size());
        assertEquals(2, matrix.get(0).size());
        assertEquals(String.format("\\nao A(%s)", uuid), matrix.get(0).get(0).toString());
        assertEquals(String.format("\\nao s(%s, %s_{%s})", uuid, thirdUuid, uuid), matrix.get(0).get(1).toString());
        assertEquals(String.format("\\nao A(%s)", secondUuid), matrix.get(1).get(0).toString());
        assertEquals(String.format("\\nao B(%s_{%s})", fourthUuid, secondUuid), matrix.get(1).get(1).toString());
    }

    @Test
    public void testMapSubClassOfAxiom_LeftSideAllValuesFromRightSideLiteral() {
        OWLAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(
                owlDataFactory.getOWLObjectAllValuesFrom(owlDataFactory.getOWLObjectProperty("r"), owlDataFactory.getOWLClass("A")),
                owlDataFactory.getOWLClass("B")
        );

        var matrix = this.visitor.visit(axiom);
        assertEquals(2, matrix.size());
        assertEquals(2, matrix.get(0).size());
        assertEquals(String.format("\\nao B(%s)", uuid), matrix.get(0).get(0).toString());
        assertEquals(String.format("\\nao r(%s, %s_{%s})", uuid, thirdUuid, uuid), matrix.get(0).get(1).toString());
        assertEquals(String.format("\\nao B(%s)", secondUuid), matrix.get(1).get(0).toString());
        assertEquals(String.format("A(%s_{%s})", fourthUuid, secondUuid), matrix.get(1).get(1).toString());
    }
}
