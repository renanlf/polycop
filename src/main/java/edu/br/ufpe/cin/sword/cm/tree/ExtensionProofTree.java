package edu.br.ufpe.cin.sword.cm.tree;

import java.util.Collection;
import java.util.Set;

public class ExtensionProofTree<Literal> extends ProofTree<Literal> {
	private final ProofTree<Literal> leftChild;
	private final ProofTree<Literal> rightChild;

	public ExtensionProofTree(Collection<Literal> clause, Set<Literal> path, ProofTree<Literal> leftChild,
			ProofTree<Literal> rightChild) {
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

	@Override
	public String toString() {
		return String.format("%s \t %s\nExt( %s, M, %s )", leftChild.toString(), rightChild.toString(), getClause(), getPath());
	}
	
	@Override
	public String latexString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(leftChild.latexString() + "\n");
		builder.append(rightChild.latexString() + "\n");
		builder.append("\\RightLabel{\\textit{Ext}}\n");
		builder.append("\\BinaryInfC{$" + getClause() + ", M, " + getPath() + "$}\n");
		
		return builder.toString();
	}

}
