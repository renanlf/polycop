package edu.br.ufpe.cin.sword.cm.propositional;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.br.ufpe.cin.sword.cm.propositonal.PropositionalMatrixBuilder;
import edu.br.ufpe.cin.sword.cm.propositonal.PropositionalProver;
import edu.br.ufpe.cin.sword.cm.tree.ExtensionProofTree;
import edu.br.ufpe.cin.sword.cm.tree.FailProofTree;
import edu.br.ufpe.cin.sword.cm.tree.ProofTree;
import edu.br.ufpe.cin.sword.cm.tree.StartProofTree;

public class PropositionalProverTest {

	@Test
	public void test() {
		
		PropositionalMatrixBuilder builder = new PropositionalMatrixBuilder();
		
		PropositionalProver prover = new PropositionalProver();
		
		ProofTree<String> proof;
		
		proof = prover.prove(builder.matrix(
				builder.clause("A"),
				builder.clause("~A")
		));
		
		assertTrue(proof instanceof StartProofTree<?>);
		assertTrue(((StartProofTree<String>) proof).getChild() instanceof ExtensionProofTree<?>);
		
		proof = prover.prove(builder.matrix(
				builder.clause("A"),
				builder.clause("~B")
		));
		
		assertTrue(proof instanceof FailProofTree<?>);
		
		proof = prover.prove(builder.matrix(
				builder.clause("A"),
				builder.clause("~B"),
				builder.clause("B", "~A")
		));
		
		assertTrue(proof instanceof StartProofTree<?>);
		
		System.out.println(proof.toString());
		
		proof = prover.prove(builder.matrix(
				builder.clause("A"),
				builder.clause("~B"),
				builder.clause("B", "~A", "C"),
				builder.clause("~C", "~B")
		));
		
		assertTrue(proof instanceof FailProofTree<?>);
		
		System.out.println(proof.toString());
		
		proof = prover.prove(builder.matrix(
				builder.clause("B", "~A"),
				builder.clause("~B", "A")
		));
		
		assertTrue(proof instanceof FailProofTree<?>);
	}
}
