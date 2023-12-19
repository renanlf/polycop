package edu.br.ufpe.cin.sword.cm.alchb.model;

import java.util.Objects;
import java.util.stream.Stream;

public class ALCHbRoleLiteral implements ALCHbLiteral {
	private final String name;
	private final boolean positive;
	private final ALCHbTerm first, second;

	public ALCHbRoleLiteral(String name, boolean positive, ALCHbTerm first, ALCHbTerm second) {
		this.name = name;
		this.positive = positive;
		this.first = first;
		this.second = second;
	}

	public String getName() {
		return name;
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
		return Objects.hash(first, name, positive, second);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ALCHbRoleLiteral other = (ALCHbRoleLiteral) obj;
		return Objects.equals(first, other.first) && Objects.equals(name, other.name) && positive == other.positive
				&& Objects.equals(second, other.second);
	}
	
	@Override
	public String toString() {
		String negation = isPositive() ? "" : "\\nao ";
		
		return String.format("%s%s(%s, %s)", negation, name, first.toString(), second.toString());
	}
	
	@Override
	public Stream<ALCHbTerm> terms() {
		return Stream.of(first, second);
	}

}
