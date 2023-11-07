package edu.br.ufpe.cin.sword.cm.propositonal;

import java.util.Set;

import edu.br.ufpe.cin.sword.cm.prover.SimpleProver;
import edu.br.ufpe.cin.sword.cm.strategies.ConnectionStrategy;
import edu.br.ufpe.cin.sword.cm.strategies.CopyStrategy;
import edu.br.ufpe.cin.sword.cm.strategies.LiteralHelperStrategy;

public class PropositionalProver extends SimpleProver<String, Void, Set<Set<String>>> {

	public PropositionalProver(LiteralHelperStrategy<String> litHelperStrategy,
			ConnectionStrategy<String, Void> connStrategy, CopyStrategy<String, Set<Set<String>>> copyStrategy) {
		super(litHelperStrategy, connStrategy, copyStrategy);
	}

}
