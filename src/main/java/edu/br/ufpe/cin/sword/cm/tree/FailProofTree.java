package edu.br.ufpe.cin.sword.cm.tree;

import java.util.Collection;
import java.util.Set;

public class FailProofTree<Literal> extends ProofTree<Literal> {

	public FailProofTree(Collection<Literal> clause, Set<Literal> path) {
		super(clause, path);
	}

	@Override
	public String toString() {
		return "fail(" + getClause() + ", " + getPath() + ")";
	}
	
	@Override
	public String latexString() {
		return "\\AxiomC{" + getClause() + ", " + getPath() + "}\r\n";
	}

}
