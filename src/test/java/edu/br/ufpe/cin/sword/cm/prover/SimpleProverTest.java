package edu.br.ufpe.cin.sword.cm.prover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;

import edu.br.ufpe.cin.sword.cm.donothing.DoNothingBlockingStrategy;
import edu.br.ufpe.cin.sword.cm.donothing.DoNothingConnectionStrategy;
import edu.br.ufpe.cin.sword.cm.donothing.DoNothingCopyStrategy;
import edu.br.ufpe.cin.sword.cm.donothing.DoNothingLiteralHelperStrategy;
import edu.br.ufpe.cin.sword.cm.strategies.LiteralHelperStrategy;
import edu.br.ufpe.cin.sword.cm.tree.AxiomProofTree;
import edu.br.ufpe.cin.sword.cm.tree.FailProofTree;
import edu.br.ufpe.cin.sword.cm.tree.ProofTree;
import edu.br.ufpe.cin.sword.cm.tree.ReductionProofTree;
import edu.br.ufpe.cin.sword.cm.tree.StartProofTree;

public class SimpleProverTest {

    @Test
    public void testProveWhenNoCopyIsAllowed() {
        // GIVEN
        DoNothingConnectionStrategy<Integer> connStrategy = spy(DoNothingConnectionStrategy.alwaysConnectStrategy());
        DoNothingCopyStrategy<Integer> copyStrategy = spy(DoNothingCopyStrategy.neverCopyStrategy());
        DoNothingLiteralHelperStrategy<Integer> helperStrategy = spy(
                DoNothingLiteralHelperStrategy.alwaysComplementaryHelperStrategy());
        DoNothingBlockingStrategy<Integer> blockStrategy = spy(DoNothingBlockingStrategy.neverBlockStrategy());

        SimpleProver<Integer, Void, Void> prover = new SimpleProver<>(
                helperStrategy,
                connStrategy,
                copyStrategy,
                blockStrategy);

        // WHEN
        ProofTree<Integer> proof = prover.prove(Set.of(
                Set.of(1, 2, 3),
                Set.of(1, 2)));

        // THEN
        assertNotNull(proof);
        assertTrue(proof instanceof FailProofTree);
        verify(connStrategy, times(1)).clear();
        verify(copyStrategy, times(1)).clear();
        verify(copyStrategy, times(1)).getState();
        verify(copyStrategy, times(2)).copy(any());
        verify(copyStrategy, times(0)).setState(any());

    }

    @Test
    public void testProveWithNullMatrix() {
        // GIVEN
        DoNothingConnectionStrategy<Integer> connStrategy = spy(DoNothingConnectionStrategy.alwaysConnectStrategy());
        DoNothingCopyStrategy<Integer> copyStrategy = spy(DoNothingCopyStrategy.neverCopyStrategy());
        DoNothingLiteralHelperStrategy<Integer> helperStrategy = spy(
                DoNothingLiteralHelperStrategy.alwaysComplementaryHelperStrategy());
        DoNothingBlockingStrategy<Integer> blockStrategy = spy(DoNothingBlockingStrategy.neverBlockStrategy());

        SimpleProver<Integer, Void, Void> prover = new SimpleProver<>(
                helperStrategy,
                connStrategy,
                copyStrategy,
                blockStrategy);

        // WHEN
        var proof = prover.prove(null);

        // THEN
        assertNotNull(proof);
        assertTrue(proof instanceof AxiomProofTree);
        assertEquals(Set.of(), ((AxiomProofTree<Integer>) proof).getPath());
    }

    @Test
    public void testProveWithEmptyMatrix() {
        // GIVEN
        DoNothingConnectionStrategy<Integer> connStrategy = spy(DoNothingConnectionStrategy.alwaysConnectStrategy());
        DoNothingCopyStrategy<Integer> copyStrategy = spy(DoNothingCopyStrategy.neverCopyStrategy());
        DoNothingLiteralHelperStrategy<Integer> helperStrategy = spy(
                DoNothingLiteralHelperStrategy.alwaysComplementaryHelperStrategy());
        DoNothingBlockingStrategy<Integer> blockStrategy = spy(DoNothingBlockingStrategy.neverBlockStrategy());

        SimpleProver<Integer, Void, Void> prover = new SimpleProver<>(
                helperStrategy,
                connStrategy,
                copyStrategy,
                blockStrategy);

        // WHEN
        var proof = prover.prove(Set.of());

        // THEN
        assertNotNull(proof);
        assertTrue(proof instanceof AxiomProofTree);
        assertEquals(Set.of(), ((AxiomProofTree<Integer>) proof).getPath());
    }

    @Test
    public void testProveWithMatrixWithinEmptyClause() {
        // GIVEN
        DoNothingConnectionStrategy<Integer> connStrategy = spy(DoNothingConnectionStrategy.alwaysConnectStrategy());
        DoNothingCopyStrategy<Integer> copyStrategy = spy(DoNothingCopyStrategy.neverCopyStrategy());
        DoNothingLiteralHelperStrategy<Integer> helperStrategy = spy(
                DoNothingLiteralHelperStrategy.alwaysComplementaryHelperStrategy());
        DoNothingBlockingStrategy<Integer> blockStrategy = spy(DoNothingBlockingStrategy.neverBlockStrategy());

        SimpleProver<Integer, Void, Void> prover = new SimpleProver<>(
                helperStrategy,
                connStrategy,
                copyStrategy,
                blockStrategy);

        // WHEN
        var proof = prover.prove(Set.of(
            Set.of()
        ));

        // THEN
        assertNotNull(proof);
        assertTrue(proof instanceof FailProofTree);
    }

    @Test
    public void testProveWithRedRule() {
        // GIVEN
        DoNothingConnectionStrategy<Integer> connStrategy = spy(DoNothingConnectionStrategy.alwaysConnectStrategy());
        DoNothingCopyStrategy<Integer> copyStrategy = spy(DoNothingCopyStrategy.alwaysCopyStrategy());
        DoNothingBlockingStrategy<Integer> blockStrategy = spy(DoNothingBlockingStrategy.neverBlockStrategy());
        LiteralHelperStrategy<Integer> helperStrategy = mock(LiteralHelperStrategy.class);

        when(helperStrategy.complementaryOf(any(), any(Collection.class))).thenReturn(Set.of(1));

        SimpleProver<Integer, Void, Void> prover = new SimpleProver<>(
                helperStrategy,
                connStrategy,
                copyStrategy,
                blockStrategy);

        // WHEN
        ProofTree<Integer> proof = prover.prove(Set.of(
                Set.of(1),
                Set.of(-1)));

        // THEN
        assertNotNull(proof);
        assertTrue(proof instanceof StartProofTree);
        assertTrue(((StartProofTree<Integer>) proof).getChild() instanceof ReductionProofTree);
        assertTrue(((ReductionProofTree<Integer>) ((StartProofTree<Integer>) proof).getChild())
                .getChild() instanceof AxiomProofTree);
        verify(connStrategy, times(1)).clear();
        verify(copyStrategy, times(1)).clear();
        verify(copyStrategy, times(1)).getState();
        verify(copyStrategy, times(1)).copy(any());
        verify(copyStrategy, times(0)).setState(any());

        verify(connStrategy, times(1)).getState();
        verify(connStrategy, times(1)).connect(any(), any());

    }

    @Test
    public void testProveWithTwoComplementaryLiteralsInThePath() {
        // GIVEN
        DoNothingConnectionStrategy<Integer> connStrategy = spy(DoNothingConnectionStrategy.alwaysConnectStrategy());
        DoNothingCopyStrategy<Integer> copyStrategy = spy(DoNothingCopyStrategy.alwaysCopyStrategy());
        DoNothingBlockingStrategy<Integer> blockStrategy = spy(DoNothingBlockingStrategy.neverBlockStrategy());
        LiteralHelperStrategy<Integer> helperStrategy = mock(LiteralHelperStrategy.class);

        when(helperStrategy.complementaryOf(any(), any(Collection.class))).thenReturn(Set.of(10, 1));
        when(connStrategy.connect(1, 10)).thenReturn(false);

        SimpleProver<Integer, Void, Void> prover = new SimpleProver<>(
                helperStrategy,
                connStrategy,
                copyStrategy,
                blockStrategy);

        // WHEN
        ProofTree<Integer> proof = prover.prove(Set.of(
                Set.of(1),
                Set.of(-1)));

        // THEN
        assertNotNull(proof);
        assertTrue(proof instanceof StartProofTree);
        assertTrue(((StartProofTree<Integer>) proof).getChild() instanceof ReductionProofTree);
        assertTrue(((ReductionProofTree<Integer>) ((StartProofTree<Integer>) proof).getChild())
                .getChild() instanceof AxiomProofTree);
        verify(connStrategy, times(1)).clear();
        verify(copyStrategy, times(1)).clear();
        verify(copyStrategy, times(1)).getState();
        verify(copyStrategy, times(1)).copy(any());
        verify(copyStrategy, times(0)).setState(any());

        verify(connStrategy, times(1)).getState();
        verify(connStrategy, times(2)).connect(any(), any());

    }
}
