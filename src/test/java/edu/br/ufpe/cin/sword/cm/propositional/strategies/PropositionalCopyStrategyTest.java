package edu.br.ufpe.cin.sword.cm.propositional.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import edu.br.ufpe.cin.sword.cm.node.LinkedNode;

public class PropositionalCopyStrategyTest {

    private PropositionalCopyStrategy strategy;

    @Before
    public void setUp() {
        this.strategy = new PropositionalCopyStrategy();
    }


    @Test
    public void testCopyEmptyClausesUsed_ExpectReturnSameClause() {
        // GIVEN
        List<Integer> clause = List.of(1, 2);

        // WHEN
        Optional<List<Integer>> result = strategy.copy(clause);
    
        // THEN
        assertTrue(result.isPresent());
        assertEquals(clause, result.get());
    }

    @Test
    public void testCopyClauseAlreadyUsed_ExpectReturnOptionalEmpty() {
        // GIVEN
        List<Integer> clause = List.of(1, 2);
        strategy.copy(clause);

        // WHEN
        Optional<List<Integer>> result = strategy.copy(clause);
    
        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCopyClauseWithNonEmptyClausesUsed_ExpectReturnSameClause() {
        // GIVEN
        List<Integer> clause = List.of(1, 2);
        List<Integer> previousClause = List.of(2,3,4);
        strategy.copy(previousClause);

        // WHEN
        Optional<List<Integer>> result = strategy.copy(clause);
    
        // THEN
        assertTrue(result.isPresent());
        assertEquals(clause, result.get());
    }

    @Test
    public void testCopyClauseUsedFewNodesBefore_ExpectReturnEmptyClause() {
        // GIVEN
        List<Integer> clause = List.of(1, 2);
        List<Integer> previousClause = List.of(2,3,4);
        List<Integer> previousPreviousClause = List.of(-5);
        strategy.copy(clause);
        strategy.copy(previousClause);
        strategy.copy(previousPreviousClause);

        // WHEN
        Optional<List<Integer>> result = strategy.copy(clause);
    
        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCopyClauseSetStateProperly_ExpectStateContainingLastCorrectNode() {
        // GIVEN
        List<Integer> clause = List.of(1, 2);
        List<Integer> previousClause = List.of(2,3,4);
        List<Integer> previousPreviousClause = List.of(-5);
        strategy.copy(clause);
        strategy.copy(previousClause);
        strategy.copy(previousPreviousClause);

        // WHEN
        LinkedNode<List<Integer>> state = strategy.getState();
    
        // THEN
        assertNotNull(state);
        assertNotNull(state.getValue());
        assertEquals(previousPreviousClause, state.getValue());

        Optional<LinkedNode<List<Integer>>> optPreviousNode = state.getPrevious();

        assertTrue(optPreviousNode.isPresent());
        assertEquals(previousClause, optPreviousNode.get().getValue());
        
        optPreviousNode = optPreviousNode.get().getPrevious();

        assertTrue(optPreviousNode.isPresent());
        assertEquals(clause, optPreviousNode.get().getValue());
        
        optPreviousNode = optPreviousNode.get().getPrevious();

        assertTrue(optPreviousNode.isEmpty());
    }

    @Test
    public void testCopyClauseUsedFewNodesBeforeAndSetStateToNull_ExpectReturnClause() {
        // GIVEN
        List<Integer> clause = List.of(1, 2);
        List<Integer> previousClause = List.of(2,3,4);
        List<Integer> previousPreviousClause = List.of(-5);
        strategy.copy(clause);
        strategy.copy(previousClause);
        strategy.copy(previousPreviousClause);

        // WHEN
        strategy.setState(null);
        Optional<List<Integer>> result = strategy.copy(clause);
    
        // THEN
        assertTrue(result.isPresent());
        assertEquals(clause, result.get());

        LinkedNode<List<Integer>> state = strategy.getState();
        assertEquals(clause, state.getValue());
        assertTrue(state.getPrevious().isEmpty());
    }

    @Test
    public void testCopyClauseUsedFewNodesBeforeAndClear_ExpectReturnClause() {
        // GIVEN
        List<Integer> clause = List.of(1, 2);
        List<Integer> previousClause = List.of(2,3,4);
        List<Integer> previousPreviousClause = List.of(-5);
        strategy.copy(clause);
        strategy.copy(previousClause);
        strategy.copy(previousPreviousClause);

        // WHEN
        strategy.clear();
        Optional<List<Integer>> result = strategy.copy(clause);
    
        // THEN
        assertTrue(result.isPresent());
        assertEquals(clause, result.get());

        LinkedNode<List<Integer>> state = strategy.getState();
        assertEquals(clause, state.getValue());
        assertTrue(state.getPrevious().isEmpty());
    }
}
