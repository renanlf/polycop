package edu.br.ufpe.cin.sword.cm.alchb.strategies;

import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import edu.br.ufpe.cin.sword.cm.strategies.LiteralHelperStrategy;

public class ALCHbHelperStrategy implements LiteralHelperStrategy<ALCHbLiteral> {

	@Override
	public boolean complementaryOf(ALCHbLiteral literal, ALCHbLiteral other) {
		return literal.getClass().equals(other.getClass())	
				&& literal.getName().equals(other.getName())
				&& literal.isPositive() != other.isPositive();
	}

}
