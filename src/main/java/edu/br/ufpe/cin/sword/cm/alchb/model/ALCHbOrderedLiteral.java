package edu.br.ufpe.cin.sword.cm.alchb.model;

public class ALCHbOrderedLiteral {
	
	private final ALCHbTerm first, second;

	public ALCHbOrderedLiteral(ALCHbTerm first, ALCHbTerm second) {
		this.first = first;
		this.second = second;
	}
	
	public ALCHbTerm getFirst() {
		return first;
	}
	
	public ALCHbTerm getSecond() {
		return second;
	}

}
