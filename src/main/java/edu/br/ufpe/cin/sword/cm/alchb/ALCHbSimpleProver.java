package edu.br.ufpe.cin.sword.cm.alchb;

import java.util.List;
import java.util.Map;

import edu.br.ufpe.cin.sword.cm.alchb.factories.ALCHbFactory;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbTerm;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbVariable;
import edu.br.ufpe.cin.sword.cm.alchb.strategies.ALCHbBlockingStrategy;
import edu.br.ufpe.cin.sword.cm.alchb.strategies.ALCHbConnectionStrategy;
import edu.br.ufpe.cin.sword.cm.alchb.strategies.ALCHbCopyStrategy;
import edu.br.ufpe.cin.sword.cm.alchb.strategies.ALCHbHelperStrategy;
import edu.br.ufpe.cin.sword.cm.node.LinkedNode;
import edu.br.ufpe.cin.sword.cm.prover.SimpleProver;

public class ALCHbSimpleProver extends SimpleProver<ALCHbLiteral, LinkedNode<Map<ALCHbVariable, ALCHbTerm>>, Map<ALCHbTerm, List<ALCHbTerm>>> {

	public ALCHbSimpleProver() {		
		super(new ALCHbHelperStrategy(), new ALCHbConnectionStrategy(), new ALCHbCopyStrategy(new ALCHbFactory()), new ALCHbBlockingStrategy());
	}

}
