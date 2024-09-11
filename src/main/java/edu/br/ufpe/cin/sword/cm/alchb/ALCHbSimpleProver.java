package edu.br.ufpe.cin.sword.cm.alchb;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import edu.br.ufpe.cin.sword.cm.alchb.factories.ALCHbFactory;
import edu.br.ufpe.cin.sword.cm.alchb.mapper.ALCHbOWLAPIMapper;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbTerm;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbVariable;
import edu.br.ufpe.cin.sword.cm.alchb.strategies.ALCHbBlockingStrategy;
import edu.br.ufpe.cin.sword.cm.alchb.strategies.ALCHbConnectionStrategy;
import edu.br.ufpe.cin.sword.cm.alchb.strategies.ALCHbCopyStrategy;
import edu.br.ufpe.cin.sword.cm.alchb.strategies.ALCHbHelperStrategy;
import edu.br.ufpe.cin.sword.cm.mapper.exceptions.FileParserException;
import edu.br.ufpe.cin.sword.cm.prover.SimpleProver;
import edu.br.ufpe.cin.sword.cm.tree.ProofTree;

public class ALCHbSimpleProver extends SimpleProver<ALCHbLiteral, Map<ALCHbVariable, ALCHbTerm>, Map<ALCHbTerm, List<ALCHbTerm>>> {

	private final ALCHbOWLAPIMapper mapper;

	public ALCHbSimpleProver() {		
		super(new ALCHbHelperStrategy(), new ALCHbConnectionStrategy(), new ALCHbCopyStrategy(new ALCHbFactory()), new ALCHbBlockingStrategy());
		this.mapper = new ALCHbOWLAPIMapper();
	}

	public ProofTree<ALCHbLiteral> prove(File file) {
        try {
            var matrix = this.mapper.map(file);
			return this.prove(matrix);
        } catch (IOException | FileParserException e) {
            throw new RuntimeException(e);
        }
    }

}
