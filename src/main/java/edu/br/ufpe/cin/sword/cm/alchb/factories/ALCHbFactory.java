package edu.br.ufpe.cin.sword.cm.alchb.factories;

import java.util.List;

import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbBiOrderedLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbConceptLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbIndividual;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbOrderedLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbRoleLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbTerm;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbUnaryIndividual;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbVariable;

public class ALCHbFactory {
	
	public ALCHbVariable var(String name) {
		return new ALCHbVariable(name);
	}
	
	public ALCHbVariable var(String name, ALCHbVariable copyOf) {
		return new ALCHbVariable(name, copyOf);
	}
	
	public ALCHbIndividual ind(String name) {
		return new ALCHbIndividual(name);
	}
	
	public ALCHbUnaryIndividual unaryInd(String name, ALCHbTerm term) {
		return new ALCHbUnaryIndividual(name, term);
	}
	
	public ALCHbUnaryIndividual unaryInd(String name, ALCHbTerm term, ALCHbUnaryIndividual copyOf) {
		return new ALCHbUnaryIndividual(name, term, copyOf);
	}
	
	public ALCHbConceptLiteral conLiteral(String name, boolean positive, ALCHbTerm term) {
		return new ALCHbConceptLiteral(name, positive, term);
	}
	
	public ALCHbRoleLiteral roleLiteral(String name, boolean positive, ALCHbTerm first, ALCHbTerm second) {
		return new ALCHbRoleLiteral(name, positive, first, second);
	}
	
	public ALCHbOrderedLiteral ordLiteral(boolean positive, ALCHbTerm first, ALCHbTerm second) {
		return new ALCHbOrderedLiteral(positive, first, second);
	}
	
	public ALCHbBiOrderedLiteral biOrdLiteral(boolean positive, ALCHbTerm first, ALCHbTerm second, ALCHbTerm third, ALCHbTerm fourth) {
		return new ALCHbBiOrderedLiteral(positive, first, second, third, fourth);
	}
	
	public List<ALCHbLiteral> clauseOf(ALCHbLiteral ...literals) {
		return List.of(literals);
	}
	
	@SuppressWarnings("unchecked")
	public List<List<ALCHbLiteral>> matrixOf(List<ALCHbLiteral> ...clauses) {
		return List.of(clauses);
	}

}
