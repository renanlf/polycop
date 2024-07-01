package edu.br.ufpe.cin.sword.cm.alchb.model;

import java.util.Objects;

public abstract class ALCHbTerm {

	private final String name;
	private final ALCHbTerm copyOf;
	
	public ALCHbTerm(String name) {
		this.name = name;
		this.copyOf = null;
	}
	
	public ALCHbTerm(String name, ALCHbTerm copyOf) {
		this.name = name;
		this.copyOf = copyOf;
	}
	
	public String getName() {
		return name;
	}
	
	public ALCHbTerm getCopyOf() {
		return copyOf;
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ALCHbTerm other = (ALCHbTerm) obj;
		return Objects.equals(name, other.name);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
