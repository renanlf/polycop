package edu.br.ufpe.cin.sword.cm.alchb.model;

public class ALCHbConceptLiteral implements ALCHbLiteral{
	
	private final String name;
	private final ALCHbTerm term;
	
	public ALCHbConceptLiteral(String name, ALCHbTerm term) {
		this.name = name;
		this.term = term;
	}
	
	public String getName() {
		return name;
	}
	
	public ALCHbTerm getTerm() {
		return term;
	}

}
