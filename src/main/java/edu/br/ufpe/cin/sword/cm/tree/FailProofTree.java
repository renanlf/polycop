package edu.br.ufpe.cin.sword.cm.tree;

import java.util.Set;

public class FailProofTree<Literal> extends ProofTree<Literal> {

	public FailProofTree(Set<Literal> clause, Set<Literal> path) {
		super(clause, path);
	}

	@Override
	public String toString() {
		return "fail()";
	}

}
