package edu.br.ufpe.cin.sword.cm.propositonal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PropositionalMatrixBuilder {
	
	@SafeVarargs
	public final Collection<Collection<String>> matrix(Set<String> ...clauses) {
		Collection<Collection<String>> matrix = new HashSet<>();
		
		for(Set<String> clause : clauses)
			matrix.add(clause);
		
		return matrix;
	}
	
	public Set<String> clause(String ...literals) {
		Set<String> clause = new HashSet<>();
		
		for(String literal : literals)
			clause.add(literal);
		
		return clause;
	}

}
