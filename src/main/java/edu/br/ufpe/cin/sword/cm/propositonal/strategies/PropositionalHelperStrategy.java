package edu.br.ufpe.cin.sword.cm.propositonal.strategies;

import edu.br.ufpe.cin.sword.cm.strategies.LiteralHelperStrategy;

public class PropositionalHelperStrategy implements LiteralHelperStrategy<String> {
	
	@Override
	public boolean complementaryOf(String literal, String other) {
		if(literal.startsWith("~")) {
			return literal.substring(1).equals(other);
		}
		
		if(other.startsWith("~")) {
			return other.substring(1).equals(literal);
		}
		
		return false;
	}

}
