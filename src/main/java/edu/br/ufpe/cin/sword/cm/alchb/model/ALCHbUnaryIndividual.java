package edu.br.ufpe.cin.sword.cm.alchb.model;

import java.util.Objects;

public class ALCHbUnaryIndividual extends ALCHbTerm {

	private final ALCHbTerm fillerTerm;
	
	public ALCHbUnaryIndividual(String name, ALCHbTerm fillerTerm) {
		super(name);
		this.fillerTerm = fillerTerm;
	}
	
	public ALCHbUnaryIndividual(String name, ALCHbTerm fillerTerm, ALCHbUnaryIndividual copyOf) {
		super(name, copyOf);
		this.fillerTerm = fillerTerm;
	}
	
	public ALCHbTerm getFillerTerm() {
		return fillerTerm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(fillerTerm);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ALCHbUnaryIndividual other = (ALCHbUnaryIndividual) obj;
		return Objects.equals(fillerTerm, other.fillerTerm);
	}	
	
	@Override
	public String toString() {
		return String.format("%s_%s", getName(), fillerTerm.toString());
	}
	
}
