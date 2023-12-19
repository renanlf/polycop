package edu.br.ufpe.cin.sword.cm.tree;

import java.util.Collection;
import java.util.Set;

public class ProofTreeFactory<Literal> {
	
	public ExtensionProofTree<Literal> ext(Collection<Literal> clause, Set<Literal> path, ProofTree<Literal> left, ProofTree<Literal> right) {
		return new ExtensionProofTree<Literal>(clause, path, left, right);
	}
	
	public ReductionProofTree<Literal> red(Collection<Literal> clause, Set<Literal> path, ProofTree<Literal> child) {
		return new ReductionProofTree<Literal>(clause, path, child);
	}
	
	public StartProofTree<Literal> st(Collection<Literal> clause, Set<Literal> path, ProofTree<Literal> child) {
		return new StartProofTree<Literal>(clause, path, child);
	}
	
	public AxiomProofTree<Literal> ax(Set<Literal> path) {
		return new AxiomProofTree<Literal>(path);
	}
	
	public FailProofTree<Literal> fail(Collection<Literal> clause, Set<Literal> path) {
		return new FailProofTree<Literal>(clause, path);
	}

}
