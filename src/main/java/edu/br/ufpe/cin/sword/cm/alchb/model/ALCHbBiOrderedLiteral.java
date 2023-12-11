package edu.br.ufpe.cin.sword.cm.alchb.model;

public class ALCHbBiOrderedLiteral {
	
	private final ALCHbTerm first, second, third, fourth;
	
	public ALCHbBiOrderedLiteral(ALCHbTerm first, ALCHbTerm second, ALCHbTerm third, ALCHbTerm fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}

	public ALCHbTerm getFirst() {
		return first;
	}
	
	public ALCHbTerm getSecond() {
		return second;
	}
	
	public ALCHbTerm getThird() {
		return third;
	}
	
	public ALCHbTerm getFourth() {
		return fourth;
	}

}
