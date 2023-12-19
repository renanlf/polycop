package edu.br.ufpe.cin.sword.cm.tree;

import java.util.Collection;
import java.util.Set;

public class ReductionProofTree<Literal> extends ProofTree<Literal> {
	private final ProofTree<Literal> child;

	public ReductionProofTree(Collection<Literal> clause, Set<Literal> path, ProofTree<Literal> child) {
		super(clause, path);
		this.child = child;
	}

	public ProofTree<Literal> getChild() {
		return child;
	}

	@Override
	public String toString() {
		return String.format("%s\nRed( %s, M, %s )", child.toString(), getClause(), getPath());
	}

	@Override
	public String latexString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(child.latexString() + "\n");
		builder.append("\\RightLabel{\\textit{Red}}\n");
		builder.append("\\UnaryInfC{$" + getClause() + ", M, " + getPath() + "$}\n");
		
		return builder.toString();
	}

}
