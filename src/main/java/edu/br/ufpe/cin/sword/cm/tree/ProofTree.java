package edu.br.ufpe.cin.sword.cm.tree;

import java.util.Set;

public abstract class ProofTree<Literal> {
	private final Set<Literal> clause, path;
	
    public ProofTree(Set<Literal> clause, Set<Literal> path) {
		super();
		this.clause = clause;
		this.path = path;
	}

	public Set<Literal> getClause() {
		return clause;
	}

	public Set<Literal> getPath() {
		return path;
	}

}
