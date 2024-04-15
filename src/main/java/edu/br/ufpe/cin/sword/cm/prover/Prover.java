package edu.br.ufpe.cin.sword.cm.prover;

import java.util.List;

import edu.br.ufpe.cin.sword.cm.tree.ProofTree;

public interface Prover<Literal> {
    ProofTree<Literal> prove(List<List<Literal>> matrix);
}
