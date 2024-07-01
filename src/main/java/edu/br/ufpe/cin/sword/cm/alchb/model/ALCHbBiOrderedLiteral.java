package edu.br.ufpe.cin.sword.cm.alchb.model;

import java.util.Objects;
import java.util.stream.Stream;

public class ALCHbBiOrderedLiteral implements ALCHbLiteral {

	private static final String BIORDERED_LITERAL_NAME = "<<";

	private final boolean positive;
	private final ALCHbTerm first, second, third, fourth;

	public ALCHbBiOrderedLiteral(boolean positive, ALCHbTerm first, ALCHbTerm second, ALCHbTerm third,
			ALCHbTerm fourth) {
		this.positive = positive;
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}

	@Override
	public String getName() {
		return BIORDERED_LITERAL_NAME;
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

	public ALCHbTerm getThird() {
		return third;
	}

	public ALCHbTerm getFourth() {
		return fourth;
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, fourth, positive, second, third);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ALCHbBiOrderedLiteral other = (ALCHbBiOrderedLiteral) obj;
		return Objects.equals(first, other.first) && Objects.equals(fourth, other.fourth) && positive == other.positive
				&& Objects.equals(second, other.second) && Objects.equals(third, other.third);
	}

	@Override
	public String toString() {
		String negation = isPositive() ? "" : "\\nao ";

		return String.format("%s( (%s, %s) %s (%s, %s) )", negation, first.toString(), second.toString(),
				BIORDERED_LITERAL_NAME, third.toString(), fourth.toString());
	}

	@Override
	public Stream<ALCHbTerm> terms() {
		return Stream.of(first, second, third, fourth);
	}

}
