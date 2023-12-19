package edu.br.ufpe.cin.sword.cm.tree;

import java.util.Collection;
import java.util.Set;

public abstract class ProofTree<Literal> {
	private final Collection<Literal> clause;
	private final Set<Literal> path;
	
    public ProofTree(Collection<Literal> clause, Set<Literal> path) {
		super();
		this.clause = clause;
		this.path = path;
	}

	public Collection<Literal> getClause() {
		return clause;
	}

	public Set<Literal> getPath() {
		return path;
	}
	
	public abstract String latexString();

}
