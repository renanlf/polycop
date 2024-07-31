package edu.br.ufpe.cin.sword.cm.alchb.model;

import java.util.stream.Stream;

public interface ALCHbLiteral {
	
	boolean isPositive();
	
	Stream<ALCHbTerm> terms();
	
	default Stream<ALCHbTerm> fullTerms() {		
		return Stream.concat(terms(), 
				terms()
					.filter(t -> t instanceof ALCHbUnaryIndividual)
					.map(t -> ((ALCHbUnaryIndividual) t).getFillerTerm()));
	}
	
	String getName();
	
	default String getLiteralName() {
		return isPositive() ? getName() : "~" + getName();
	}
	
}
