package edu.br.ufpe.cin.sword.cm.tree;

import java.util.Collection;
import java.util.Set;

public class StartProofTree<Literal> extends ProofTree<Literal> {
	private final ProofTree<Literal> child;

	public StartProofTree(Collection<Literal> clause, Set<Literal> path, ProofTree<Literal> child) {
		super(clause, path);
		this.child = child;
	}

	public ProofTree<Literal> getChild() {
		return child;
	}

	@Override
	public String toString() {
		return String.format("%s\nSt( \\epsilon, M, \\epsilon )", child.toString());
	}

	@Override
	public String latexString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(child.latexString() + "\n");
		builder.append("\\RightLabel{\\textit{St}}\n");
		builder.append("\\UnaryInfC{$\\varepsilon, M, \\varepsilon$}\n");
		
		return builder.toString();
	}

}
