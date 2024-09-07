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
    private static MockedStatic<UUID> uuidMock;
    private OWLDataFactory owlDataFactory;
    private ALCHbMapOWLAxiomVisitor visitor;

    @BeforeClass
    public static void setUpBeforeClass() {
        uuid = UUID.randomUUID();
        secondUuid = UUID.randomUUID();
        uuidMock = Mockito.mockStatic(UUID.class);
    }

    @Before
    public void setUp() {
        this.visitor = new ALCHbMapOWLAxiomVisitor(new ALCHbFactory());
        this.owlDataFactory = OWLManager.getOWLDataFactory();
        uuidMock.when(UUID::randomUUID).thenReturn(uuid, secondUuid);
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
}
