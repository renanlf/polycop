package edu.br.ufpe.cin.sword.cm.tree;

import java.util.Set;

public class StartProofTree<Literal> extends ProofTree<Literal>{
    private final ProofTree<Literal> child;

	public StartProofTree(Set<Literal> clause, Set<Literal> path, ProofTree<Literal> child) {
		super(clause, path);
		this.child = child;
	}

	public ProofTree<Literal> getChild() {
		return child;
	}
	
}
