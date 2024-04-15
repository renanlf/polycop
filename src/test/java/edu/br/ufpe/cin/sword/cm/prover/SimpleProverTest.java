package edu.br.ufpe.cin.sword.cm.prover;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

import edu.br.ufpe.cin.sword.cm.tree.ProofTreeFactory;

public class SimpleProverTest {

    private SimpleProver<Integer, Void, Void> prover;

    private ProofTreeFactory<Integer> proofTreeFactory;

    @Before
    public void setUp() throws IllegalAccessException {
        proofTreeFactory = spy(new ProofTreeFactory<>());

        prover = new SimpleProver<Integer, Void, Void>(
                (literal, other) -> literal == -other,
                (literal, other) -> literal == -other,
                (clause) -> Optional.of(clause),
                (literal, clause, connState, copyState) -> false);

        FieldUtils.writeField(prover, "proofFactory", proofTreeFactory, true);
    }

    @Test
    public void proveWithNullMatrix_ExpectAxiom() {
        // GIVEN
        List<List<Integer>> matrix = null;

        // WHEN
        prover.prove(matrix);

        // THEN
        verify(proofTreeFactory, times(1)).ax(Set.of());
        verify(proofTreeFactory, times(0)).st(any(), any(), any());
        verify(proofTreeFactory, times(0)).red(any(), any(), any());
        verify(proofTreeFactory, times(0)).ext(any(), any(), any(), any());
        verify(proofTreeFactory, times(0)).fail(any(), any());
    }

    @Test
    public void proveWithEmptyMatrix_ExpectAxiom() {
        // GIVEN
        List<List<Integer>> matrix = List.of();

        // WHEN
        prover.prove(matrix);

        // THEN
        verify(proofTreeFactory, times(1)).ax(Set.of());
        verify(proofTreeFactory, times(0)).st(any(), any(), any());
        verify(proofTreeFactory, times(0)).red(any(), any(), any());
        verify(proofTreeFactory, times(0)).ext(any(), any(), any(), any());
        verify(proofTreeFactory, times(0)).fail(any(), any());

    }

    @Test
    public void proveWithSingletonEmptyClause_ExpectFail() {
        // GIVEN
        List<List<Integer>> matrix = List.of(List.of());

        // WHEN
        prover.prove(matrix);

        // THEN
        verify(proofTreeFactory, times(0)).ax(any());
        verify(proofTreeFactory, times(0)).st(any(), any(), any());
        verify(proofTreeFactory, times(0)).red(any(), any(), any());
        verify(proofTreeFactory, times(0)).ext(any(), any(), any(), any());
        verify(proofTreeFactory, times(1)).fail(null, null);

    }

    @Test
    public void proveWithSingletonLiteralClause_ExpectFail() {
        // GIVEN
        List<List<Integer>> matrix = List.of(List.of(1));

        // WHEN
        prover.prove(matrix);

        // THEN
        verify(proofTreeFactory, times(0)).ax(any());
        verify(proofTreeFactory, times(0)).st(any(), any(), any());
        verify(proofTreeFactory, times(0)).red(any(), any(), any());
        verify(proofTreeFactory, times(0)).ext(any(), any(), any(), any());
        verify(proofTreeFactory, times(1)).fail(List.of(1), Set.of());
        verify(proofTreeFactory, times(1)).fail(null, null);

    }

    @Test
    public void proveWithTwoComplementarySingletonClauses_ExpectExtAndAx() {
        // GIVEN
        List<List<Integer>> matrix = List.of(
                List.of(1),
                List.of(-1));

        // WHEN
        prover.prove(matrix);

        // THEN
        verify(proofTreeFactory, times(1)).ax(Set.of());
        verify(proofTreeFactory, times(1)).ax(Set.of(1));
        verify(proofTreeFactory, times(1)).st(eq(List.of(1)), eq(Set.of()), any());
        verify(proofTreeFactory, times(0)).red(any(), any(), any());
        verify(proofTreeFactory, times(1)).ext(eq(List.of(1)), any(), any(), any());
        verify(proofTreeFactory, times(0)).fail(any(), any());
    }

    @Test
    public void proveWithTwoClausesButOneHasNoComplementaryLiteral_ExpectTryEveryClauseAndFail() {
        // GIVEN
        List<List<Integer>> matrix = List.of(
                List.of(1),
                List.of(-1, 2));

        // WHEN
        prover.prove(matrix);

        // THEN
        verify(proofTreeFactory, times(1)).fail(List.of(2), Set.of(1));
        verify(proofTreeFactory, times(1)).fail(List.of(1), Set.of());

        verify(proofTreeFactory, times(1)).ax(Set.of(-1));
        verify(proofTreeFactory, times(1)).fail(List.of(2), Set.of());
        verify(proofTreeFactory, times(1)).fail(List.of(-1, 2), Set.of());

        verify(proofTreeFactory, times(0)).st(any(), any(), any());
        verify(proofTreeFactory, times(0)).red(any(), any(), any());
        verify(proofTreeFactory, times(0)).ext(any(), any(), any(), any());
    }

    @Test
    public void proveWithComplementaryLiteralInThePath_ExpectRed() {
        // GIVEN
        List<List<Integer>> matrix = List.of(
                List.of(1),
                List.of(-1, 2),
                List.of(-2, -1));

        // WHEN
        prover.prove(matrix);

        // THEN

        verify(proofTreeFactory, times(1)).red(any(), any(), any());
        verify(proofTreeFactory, times(3)).ax(any());
        verify(proofTreeFactory, times(2)).ext(any(), any(), any(), any());
        verify(proofTreeFactory, times(1)).st(any(), any(), any());

        verify(proofTreeFactory, times(0)).fail(any(), any());

    }

    @Test
    public void proveWithComplementaryLiteralInThePath_ExpectRedAndFail() {
        // GIVEN
        List<List<Integer>> matrix = List.of(
                List.of(1),
                List.of(-1, 2, 5),
                List.of(-2, -1));

        // WHEN
        prover.prove(matrix);

        // THEN
        verify(proofTreeFactory, times(1)).red(any(), any(), any());
        verify(proofTreeFactory, times(1)).fail(List.of(5), Set.of(1));

    }

}
