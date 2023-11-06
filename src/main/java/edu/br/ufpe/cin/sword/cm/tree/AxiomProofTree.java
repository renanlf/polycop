package edu.br.ufpe.cin.sword.cm.tree;

import java.util.Collections;
import java.util.Set;

public class AxiomProofTree<Literal> extends ProofTree<Literal> {

	public AxiomProofTree(Set<Literal> path) {
		super(Collections.emptySet(), path);
	}
    
}
