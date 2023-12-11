package edu.br.ufpe.cin.sword.cm.alchb.model;

public class ALCHbRoleLiteral {
	private final String name;
	private final ALCHbTerm first, second;

	public ALCHbRoleLiteral(String name, ALCHbTerm first, ALCHbTerm second) {
		this.name = name;
		this.first = first;
		this.second = second;
	}

	public String getName() {
		return name;
	}

	public ALCHbTerm getFirst() {
		return first;
	}

	public ALCHbTerm getSecond() {
		return second;
	}

}
