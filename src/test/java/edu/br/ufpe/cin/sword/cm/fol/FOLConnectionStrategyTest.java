package edu.br.ufpe.cin.sword.cm.fol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import edu.br.ufpe.cin.sword.cm.fol.model.FOLLiteral;
import edu.br.ufpe.cin.sword.cm.fol.model.terms.FOLTerm;
import edu.br.ufpe.cin.sword.cm.fol.model.terms.FOLTermFactory;
import edu.br.ufpe.cin.sword.cm.fol.model.terms.FOLVariable;
import edu.br.ufpe.cin.sword.cm.fol.strategy.FOLConnectionStrategy;

public class FOLConnectionStrategyTest {


    @Test
    public void testConnectionBetweenDifferentPredicate() {
        FOLConnectionStrategy connStrategy = new FOLConnectionStrategy();

        FOLLiteral literal = new FOLLiteral("A", true, FOLTermFactory.variable("x"));
        FOLLiteral other = new FOLLiteral("B", false, FOLTermFactory.variable("y"));

        assertFalse(connStrategy.connect(literal, other));
    }

    @Test
    public void testConnectionSamePredicateSamePositive() {
        FOLConnectionStrategy connStrategy = new FOLConnectionStrategy();

        FOLLiteral literal = new FOLLiteral("A", true, FOLTermFactory.variable("x"));
        FOLLiteral other = new FOLLiteral("A", true, FOLTermFactory.variable("y"));

        assertFalse(connStrategy.connect(literal, other));
    }

    @Test
    public void testConnectionBetweenLiteralsDifferentTermsSize() {
        FOLConnectionStrategy connStrategy = new FOLConnectionStrategy();

        FOLLiteral literal = new FOLLiteral("A", true, FOLTermFactory.variable("x"));
        FOLLiteral other = new FOLLiteral("A", false, FOLTermFactory.variable("y"), FOLTermFactory.variable("z"));

        assertFalse(connStrategy.connect(literal, other));
    }
    
    @Test
    public void testConnectionBetweenVariables() {
        FOLConnectionStrategy connStrategy = new FOLConnectionStrategy();

        FOLLiteral literal = new FOLLiteral("A", true, FOLTermFactory.variable("x"));
        FOLLiteral other = new FOLLiteral("A", false, FOLTermFactory.variable("y"));

        assertTrue(connStrategy.connect(literal, other));
        Map<FOLVariable, FOLTerm> subs = connStrategy.getState();
        assertEquals(1, subs.size());
        assertTrue(subs.containsKey(FOLTermFactory.variable("x")));
        assertFalse(subs.containsKey(FOLTermFactory.variable("y")));
        assertEquals(FOLTermFactory.variable("y"), subs.get(FOLTermFactory.variable("x")));
    }

    @Test
    public void testConnectionBetweenMultipleVariables() {
        FOLConnectionStrategy connStrategy = new FOLConnectionStrategy();

        FOLLiteral literal = new FOLLiteral("A", true, FOLTermFactory.variable("x"), FOLTermFactory.variable("z"));
        FOLLiteral other = new FOLLiteral("A", false, FOLTermFactory.variable("y"), FOLTermFactory.variable("k"));

        assertTrue(connStrategy.connect(literal, other));
        Map<FOLVariable, FOLTerm> subs = connStrategy.getState();
        assertEquals(2, subs.size());
        assertTrue(subs.containsKey(FOLTermFactory.variable("x")));
        assertTrue(subs.containsKey(FOLTermFactory.variable("z")));
        assertEquals(FOLTermFactory.variable("y"), subs.get(FOLTermFactory.variable("x")));
        assertEquals(FOLTermFactory.variable("k"), subs.get(FOLTermFactory.variable("z")));
    }

    @Test
    public void testConnectionBetweenVariableAndGroundTerm() {
        FOLConnectionStrategy connStrategy = new FOLConnectionStrategy();

        FOLLiteral literal = new FOLLiteral("A", true, FOLTermFactory.variable("x"));
        FOLLiteral other = new FOLLiteral("A", false, FOLTermFactory.groundTerm("a"));

        assertTrue(connStrategy.connect(literal, other));
        Map<FOLVariable, FOLTerm> subs = connStrategy.getState();
        assertEquals(1, subs.size());
        assertTrue(subs.containsKey(FOLTermFactory.variable("x")));
        assertFalse(subs.containsKey(FOLTermFactory.variable("a")));
        assertEquals(FOLTermFactory.groundTerm("a"), subs.get(FOLTermFactory.variable("x")));
    }

    @Test
    public void testConnectionBetweenGroundTermAndGroundTerm() {
        FOLConnectionStrategy connStrategy = new FOLConnectionStrategy();

        FOLLiteral literal = new FOLLiteral("A", true, FOLTermFactory.groundTerm("a"));
        FOLLiteral other = new FOLLiteral("A", false, FOLTermFactory.groundTerm("b"));

        assertFalse(connStrategy.connect(literal, other));
        Map<FOLVariable, FOLTerm> subs = connStrategy.getState();
        assertEquals(0, subs.size());
    }

    @Test
    public void testConnectionBetweenGroundTermAndGroundTermWithSameName() {
        FOLConnectionStrategy connStrategy = new FOLConnectionStrategy();

        FOLLiteral literal = new FOLLiteral("A", true, FOLTermFactory.groundTerm("a"));
        FOLLiteral other = new FOLLiteral("A", false, FOLTermFactory.groundTerm("a"));

        assertTrue(connStrategy.connect(literal, other));
        Map<FOLVariable, FOLTerm> subs = connStrategy.getState();
        assertEquals(0, subs.size());
    }

    @Test
    public void testConnectionBetweenVariableAndFunction() {
        FOLConnectionStrategy connStrategy = new FOLConnectionStrategy();

        FOLLiteral literal = new FOLLiteral("A", true, FOLTermFactory.variable("x"));
        FOLLiteral other = new FOLLiteral("A", false, FOLTermFactory.function("f", FOLTermFactory.variable("y")));

        assertTrue(connStrategy.connect(literal, other));
        Map<FOLVariable, FOLTerm> subs = connStrategy.getState();
        assertEquals(1, subs.size());
        assertEquals(FOLTermFactory.function("f", FOLTermFactory.variable("y")), subs.get(FOLTermFactory.variable("x")));
    }

    @Test
    public void testConnectionBetweenVariableAndFunctionOccursCheck() {
        FOLConnectionStrategy connStrategy = new FOLConnectionStrategy();

        FOLLiteral literal = new FOLLiteral("A", true, FOLTermFactory.variable("x"));
        FOLLiteral other = new FOLLiteral("A", false, FOLTermFactory.function("f", FOLTermFactory.variable("x")));

        assertFalse(connStrategy.connect(literal, other));
        Map<FOLVariable, FOLTerm> subs = connStrategy.getState();
        assertEquals(0, subs.size());
    }

    @Test
    public void testConnectionBetweenVariableAndFunctionOccursCheckDeeper() {
        FOLConnectionStrategy connStrategy = new FOLConnectionStrategy();

        FOLLiteral literal = new FOLLiteral("A", true, FOLTermFactory.variable("x"));
        FOLLiteral other = new FOLLiteral("A", false, FOLTermFactory.function("f", FOLTermFactory.function("g", FOLTermFactory.function("h", FOLTermFactory.variable("x")))));

        assertFalse(connStrategy.connect(literal, other));
        Map<FOLVariable, FOLTerm> subs = connStrategy.getState();
        assertEquals(0, subs.size());
    }

    
    @Test
    public void testConnectionBetweenVariableAndFunctionDeeper() {
        FOLConnectionStrategy connStrategy = new FOLConnectionStrategy();

        FOLLiteral literal = new FOLLiteral("A", true, FOLTermFactory.variable("x"));
        FOLLiteral other = new FOLLiteral("A", false, FOLTermFactory.function("f", FOLTermFactory.function("g", FOLTermFactory.function("h", FOLTermFactory.variable("y")))));

        assertTrue(connStrategy.connect(literal, other));
        Map<FOLVariable, FOLTerm> subs = connStrategy.getState();
        assertEquals(1, subs.size());
    }

    @Test
    public void testConnectionBetweenMultipleVariableAndFunction() {
        FOLConnectionStrategy connStrategy = new FOLConnectionStrategy();

        FOLLiteral literal = new FOLLiteral("A", true, FOLTermFactory.variable("x"), FOLTermFactory.variable("y"));
        FOLLiteral other = new FOLLiteral("A", false, FOLTermFactory.groundTerm("a"), FOLTermFactory.function("f", FOLTermFactory.variable("x")));

        assertTrue(connStrategy.connect(literal, other));
        Map<FOLVariable, FOLTerm> subs = connStrategy.getState();
        assertEquals(2, subs.size());
        assertTrue(subs.containsKey(FOLTermFactory.variable("x")));
        assertTrue(subs.containsKey(FOLTermFactory.variable("y")));
        assertEquals(FOLTermFactory.groundTerm("a"), subs.get(FOLTermFactory.variable("x")));
        assertEquals(FOLTermFactory.function("f", FOLTermFactory.variable("x")), subs.get(FOLTermFactory.variable("y")));
    }
}
