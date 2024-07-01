package edu.br.ufpe.cin.sword.cm.alchb.model;

import java.util.Objects;
import java.util.stream.Stream;

public class ALCHbConceptLiteral implements ALCHbLiteral{
	
	private final String name;
	private final boolean positive;
	private final ALCHbTerm term;
	
	public ALCHbConceptLiteral(String name, boolean positive, ALCHbTerm term) {
		this.name = name;
		this.positive = positive;
		this.term = term;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isPositive() {
		return positive;
	}
	
	public ALCHbTerm getTerm() {
		return term;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, positive, term);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ALCHbConceptLiteral other = (ALCHbConceptLiteral) obj;
		return Objects.equals(name, other.name) && positive == other.positive && Objects.equals(term, other.term);
	}
	
	@Override
	public String toString() {
		String negation = isPositive() ? "" : "\\nao ";
		
		return String.format("%s%s(%s)", negation, name, term.toString());
	}

	@Override
	public Stream<ALCHbTerm> terms() {
		return Stream.of(term);
	}

}
