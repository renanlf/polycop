package edu.br.ufpe.cin.sword.cm.tree;

import java.util.Set;

public class ExtensionProofTree<Literal> extends ProofTree<Literal> {
	private final ProofTree<Literal> leftChild;
	private final ProofTree<Literal> rightChild;
	
	public ExtensionProofTree(Set<Literal> clause, Set<Literal> path, ProofTree<Literal> leftChild, ProofTree<Literal> rightChild) {
		super(clause, path);
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

	public ProofTree<Literal> getLeftChild() {
		return leftChild;
	}

	public ProofTree<Literal> getRightChild() {
		return rightChild;
	}
	
}
