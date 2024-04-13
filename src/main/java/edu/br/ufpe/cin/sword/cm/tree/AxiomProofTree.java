package edu.br.ufpe.cin.sword.cm.tree;

import java.util.Set;

public class AxiomProofTree<Literal> extends ProofTree<Literal> {

	public AxiomProofTree(Set<Literal> path) {
		super(Set.of(), path);
	}

	@Override
	public String toString() {
		return String.format("Ax( %s, M, %s )", getClause(), getPath());
	}

	@Override
	public String latexString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("\\AxiomC{}\r\n");
		builder.append("\\RightLabel{Ax}\n");
		builder.append("\\UnaryInfC{$\\{\\}, M, "+ getPath() + "$}");
		
		return builder.toString();
	}

}
