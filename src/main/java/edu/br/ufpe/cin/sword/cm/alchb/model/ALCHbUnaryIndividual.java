package edu.br.ufpe.cin.sword.cm.alchb.model;

public class ALCHbUnaryIndividual extends ALCHbTerm {

	private final ALCHbVariable variable;
	
	public ALCHbUnaryIndividual(String name, ALCHbVariable variable) {
		super(name);
		this.variable = variable;
	}
	
	public ALCHbVariable getVariable() {
		return variable;
	}
	
}
