package edu.br.ufpe.cin.sword.cm.prover;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
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

import edu.br.ufpe.cin.sword.cm.strategies.BlockingStrategy;
import edu.br.ufpe.cin.sword.cm.strategies.ConnectionStrategy;
import edu.br.ufpe.cin.sword.cm.strategies.CopyStrategy;
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

    @Test
    public void proveWithEmptyCopies_ExpectFail() throws IllegalAccessException {
        // GIVEN
        List<List<Integer>> matrix = List.of(
                List.of(1));

        FieldUtils.writeField(prover, "copyStrategy", new CopyStrategy<Integer, Void>() {
            @Override
            public Optional<List<Integer>> copy(List<Integer> clause) {
                return Optional.empty();
            }
        }, true);

        // WHEN
        prover.prove(matrix);

        // THEN
        verify(proofTreeFactory, times(1)).fail(null, null);

    }

    @Test
    public void proveWithNoCopiesAllowed_ExpectFail() throws IllegalAccessException {
        // GIVEN
        List<List<Integer>> matrix = List.of(
                List.of(1),
                List.of(-1));

        FieldUtils.writeField(prover, "copyStrategy", new CopyStrategy<Integer, Void>() {
            @Override
            public Optional<List<Integer>> copy(List<Integer> clause) {
                return clause.get(0) == -1
                        ? Optional.empty()
                        : Optional.of(clause);
            }
        }, true);

        // WHEN
        prover.prove(matrix);

        // THEN
        verify(proofTreeFactory, times(1)).fail(eq(List.of(1)), anySet());

    }

    @Test
    public void proveWithBlockingStrategyReturingTrue_ExpectFail() throws IllegalAccessException {
        // GIVEN
        List<List<Integer>> matrix = List.of(
                List.of(1),
                List.of(-1));

        FieldUtils.writeField(prover, "blockingStrategy", new BlockingStrategy<Integer, Void, Void>() {
            @Override
            public boolean isBlocked(Integer literal, Set<Integer> path, Void connState, Void copyState) {
                return literal == -1;
            }
        }, true);

        // WHEN
        prover.prove(matrix);

        // THEN
        verify(proofTreeFactory, times(1)).fail(eq(List.of(1)), anySet());

    }

    @Test
    public void proveWithComplementaryLiteralInThePathButConnectionNotAllowed_ExpectFail()
            throws IllegalAccessException {
        // GIVEN
        List<List<Integer>> matrix = List.of(
                List.of(1),
                List.of(-1, 2),
                List.of(-2, -1));
        FieldUtils.writeField(prover, "connStrategy", new ConnectionStrategy<Integer, Void>() {
            private int count = 0;
            @Override
            public boolean connect(Integer literal, Integer other) {
                if (other == -1 && count < 1) {
                    count++;
                    return true;
                }

                return false;
            }
        }, true);

        // WHEN
        prover.prove(matrix);

        // THEN
        verify(proofTreeFactory, times(5)).fail(any(), any());

    }

    @Test
    public void proveWithTwoComplementarySingletonClausesButConnectionNotAllowed_ExpectFail() throws IllegalAccessException {
        // GIVEN
        List<List<Integer>> matrix = List.of(
                List.of(1),
                List.of(-1));

        FieldUtils.writeField(prover, "connStrategy", new ConnectionStrategy<Integer, Void>() {
            @Override
            public boolean connect(Integer literal, Integer other) {
                return false;
            }
        }, true);

        // WHEN
        prover.prove(matrix);

        // THEN
        verify(proofTreeFactory, times(3)).fail(any(), any());
    }


}
