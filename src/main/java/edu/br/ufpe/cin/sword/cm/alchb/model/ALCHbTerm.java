package edu.br.ufpe.cin.sword.cm.alchb.model;

public abstract class ALCHbTerm {
	private final String name;
	
	public ALCHbTerm(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
