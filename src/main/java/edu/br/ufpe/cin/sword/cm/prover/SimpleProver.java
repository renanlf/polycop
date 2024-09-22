package edu.br.ufpe.cin.sword.cm.prover;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import edu.br.ufpe.cin.sword.cm.strategies.BlockingStrategy;
import edu.br.ufpe.cin.sword.cm.strategies.ConnectionStrategy;
import edu.br.ufpe.cin.sword.cm.strategies.CopyStrategy;
import edu.br.ufpe.cin.sword.cm.strategies.LiteralHelperStrategy;
import edu.br.ufpe.cin.sword.cm.tree.FailProofTree;
import edu.br.ufpe.cin.sword.cm.tree.ProofTree;
import edu.br.ufpe.cin.sword.cm.tree.ProofTreeFactory;

public class SimpleProver<Literal, ConnectionState, CopyState> implements Prover<Literal> {

	private final ConnectionStrategy<Literal, ConnectionState> connStrategy;
	private final CopyStrategy<Literal, CopyState> copyStrategy;
	private final LiteralHelperStrategy<Literal> litHelperStrategy;
	private final BlockingStrategy<Literal, ConnectionState, CopyState> blockingStrategy;
	private final ProofTreeFactory<Literal> proofFactory;

	public SimpleProver(LiteralHelperStrategy<Literal> litHelperStrategy,
			ConnectionStrategy<Literal, ConnectionState> connStrategy,
			CopyStrategy<Literal, CopyState> copyStrategy,
			BlockingStrategy<Literal, ConnectionState, CopyState> blockingStrategy) {
		this.connStrategy = connStrategy;
		this.copyStrategy = copyStrategy;
		this.litHelperStrategy = litHelperStrategy;
		this.blockingStrategy = blockingStrategy;
		this.proofFactory = new ProofTreeFactory<>();
	}

	public ProofTree<Literal> prove(List<List<Literal>> matrix) {
		if (matrix == null || matrix.isEmpty()) {
			return proofFactory.ax(Set.of());
		}

		connStrategy.clear();
		copyStrategy.clear();

		CopyState copyState = copyStrategy.getState();
		for (List<Literal> clause : matrix) {
			Optional<List<Literal>> copyClause = copyStrategy.copy(clause);

			if (copyClause.isPresent() && !copyClause.get().isEmpty()) {

//				System.out.printf("[St] - %s  %n", copyClause.get());
				ProofTree<Literal> proof = proveClause(copyClause.get(), matrix, Set.of());

				if (!(proof instanceof FailProofTree)) {
					return proofFactory.st(copyClause.get(), Set.of(), proof);
				}

				copyStrategy.setState(copyState);
			}
		}

		return proofFactory.fail(null, null);
	}

	private ProofTree<Literal> proveClause(List<Literal> clause, List<List<Literal>> matrix,
			Set<Literal> path) {

		if (clause.isEmpty()) 
			return proofFactory.ax(path);

		Literal literal = clause.get(0);

		// Regularity
		if (path.contains(literal))
			return proofFactory.fail(clause, path);

		ConnectionState connState = connStrategy.getState();
		for (Literal negLiteral : litHelperStrategy.complementaryOf(literal, path)) {
			if (connStrategy.connect(literal, negLiteral)) {

//				System.out.printf("[Red] - <%s, %s, %s>  %n", literal, path, connStrategy.getState());
				ProofTree<Literal> subProof = proveClause(minus(clause, literal), matrix, path);

				if (!(subProof instanceof FailProofTree)) {	
					return proofFactory.red(clause, path, subProof);
				}
				connStrategy.setState(connState);
			}
		}

		CopyState copyState = copyStrategy.getState();

		List<List<Literal>> clausesWithComplementaryLiteral = litHelperStrategy.complementaryOfInMatrix(literal, matrix);
		for (List<Literal> matrixClause : clausesWithComplementaryLiteral) {
			Optional<List<Literal>> copyClause = copyStrategy.copy(matrixClause);

			if (copyClause.isPresent()) {
				List<Literal> negLiterals = litHelperStrategy.complementaryOf(literal, copyClause.get());
				for (Literal negLiteral : negLiterals) {
					if (connStrategy.connect(literal, negLiteral)) {
						if (!blockingStrategy.isBlocked(negLiteral, path, connStrategy.getState(),
								copyStrategy.getState())) {							

//							System.out.printf("[Ext-Left] - <%s, %s, %s, %s>  %n", literal, copyClause.get(), path, connStrategy.getState());
							ProofTree<Literal> subProofLeft = proveClause(minus(copyClause.get(), negLiteral), matrix,
									add(path, literal));

							if (!(subProofLeft instanceof FailProofTree)) {
//								System.out.printf("[Ext-Right] - <%s, %s, %s, %s>  %n", literal, clause, path, connStrategy.getState());
								ProofTree<Literal> subProofRight = proveClause(minus(clause, literal), matrix, path);
								if (!(subProofRight instanceof FailProofTree)) {
									return proofFactory.ext(clause, path, subProofLeft, subProofRight);
								}
							}
						}
						// if comes here, then some subProof is failed.
//						System.out.printf("[Backtrack connection] - <%s, %s, %s, %s, %s>  %n", literal, negLiteral, copyClause.get(), path, connStrategy.getState());
						connStrategy.setState(connState);
					}
				}
				// if comes here, then this copy is failed
//				System.out.printf("[Backtrack copy] - <%s, %s, %s, %s>  %n", literal, copyClause.get(), path, connStrategy.getState());
				copyStrategy.setState(copyState);
			}
		}

		return proofFactory.fail(clause, path);
	}

	private Set<Literal> add(Set<Literal> previousSet, Literal toAdd) {
		Set<Literal> newSet = new HashSet<>(previousSet);
		newSet.add(toAdd);
		return newSet;
	}

	private List<Literal> minus(List<Literal> previousList, Literal toRemove) {
		List<Literal> newList = new ArrayList<>(previousList);
		newList.remove(toRemove);
		return newList;
	}

}
