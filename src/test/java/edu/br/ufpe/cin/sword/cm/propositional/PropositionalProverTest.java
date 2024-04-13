package edu.br.ufpe.cin.sword.cm.propositional;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import edu.br.ufpe.cin.sword.cm.propositonal.PropositionalProver;
import edu.br.ufpe.cin.sword.cm.tree.ExtensionProofTree;
import edu.br.ufpe.cin.sword.cm.tree.FailProofTree;
import edu.br.ufpe.cin.sword.cm.tree.ProofTree;
import edu.br.ufpe.cin.sword.cm.tree.StartProofTree;

public class PropositionalProverTest {

	@Test
	public void test() {		
		PropositionalProver prover = new PropositionalProver();
		
		ProofTree<String> proof;
		
		proof = prover.prove(List.of(
				List.of("A"),
				List.of("~A")
		));
		
		assertTrue(proof instanceof StartProofTree<?>);
		assertTrue(((StartProofTree<String>) proof).getChild() instanceof ExtensionProofTree<?>);
		
		proof = prover.prove(List.of(
				List.of("A"),
				List.of("~B")
		));
		
		assertTrue(proof instanceof FailProofTree<?>);
		
		proof = prover.prove(List.of(
				List.of("A"),
				List.of("~B"),
				List.of("B", "~A")
		));
		
		assertTrue(proof instanceof StartProofTree<?>);
		
		System.out.println(proof.toString());
		
		proof = prover.prove(List.of(
				List.of("A"),
				List.of("~B"),
				List.of("B", "~A", "C"),
				List.of("~C", "~B")
		));
		
		assertTrue(proof instanceof FailProofTree<?>);
		
		System.out.println(proof.toString());
		
		proof = prover.prove(List.of(
				List.of("B", "~A"),
				List.of("~B", "A")
		));
		
		assertTrue(proof instanceof FailProofTree<?>);
	}
}
