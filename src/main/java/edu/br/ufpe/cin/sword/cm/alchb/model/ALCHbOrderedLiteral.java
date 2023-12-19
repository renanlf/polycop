package edu.br.ufpe.cin.sword.cm.alchb.model;

import java.util.Objects;
import java.util.stream.Stream;

public class ALCHbOrderedLiteral implements ALCHbLiteral {

	private static final String ORDERED_LITERAL_NAME = "<";
	
	private final boolean positive;
	private final ALCHbTerm first, second;

	public ALCHbOrderedLiteral(boolean positive, ALCHbTerm first, ALCHbTerm second) {
		this.positive = positive;
		this.first = first;
		this.second = second;
	}
	
	@Override
	public String getName() {
		return ORDERED_LITERAL_NAME;
	}
	
	public boolean isPositive() {
		return positive;
	}
	
	public ALCHbTerm getFirst() {
		return first;
	}
	
	public ALCHbTerm getSecond() {
		return second;
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(first, positive, second);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ALCHbOrderedLiteral other = (ALCHbOrderedLiteral) obj;
		return Objects.equals(first, other.first) && positive == other.positive && Objects.equals(second, other.second);
	}
	
	@Override
	public String toString() {
		String negation = isPositive() ? "" : "\\nao ";
		
		return String.format("%s(%s %s %s)", negation, first.toString(), ORDERED_LITERAL_NAME, second.toString());
	}
	
	@Override
	public Stream<ALCHbTerm> terms() {
		return Stream.of(first, second);
	}

}
