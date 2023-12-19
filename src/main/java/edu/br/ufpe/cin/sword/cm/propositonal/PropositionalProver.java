package edu.br.ufpe.cin.sword.cm.propositonal;

import java.util.Collection;
import java.util.Set;

import edu.br.ufpe.cin.sword.cm.propositonal.strategies.PropositionalBlockingStrategy;
import edu.br.ufpe.cin.sword.cm.propositonal.strategies.PropositionalConnectionStrategy;
import edu.br.ufpe.cin.sword.cm.propositonal.strategies.PropositionalCopyStrategy;
import edu.br.ufpe.cin.sword.cm.propositonal.strategies.PropositionalHelperStrategy;
import edu.br.ufpe.cin.sword.cm.prover.SimpleProver;

public class PropositionalProver extends SimpleProver<String, Void, Void, Set<Collection<String>>> {

	public PropositionalProver() {
		super(new PropositionalHelperStrategy(),
				new PropositionalConnectionStrategy(),
				new PropositionalCopyStrategy(),
				new PropositionalBlockingStrategy());
	}

}
