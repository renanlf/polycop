package edu.br.ufpe.cin.sword.cm.alchb.strategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import edu.br.ufpe.cin.sword.cm.alchb.factories.ALCHbFactory;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbBiOrderedLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbConceptLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbIndividual;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbOrderedLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbRoleLiteral;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbTerm;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbUnaryIndividual;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbVariable;
import edu.br.ufpe.cin.sword.cm.strategies.CopyStrategy;

public class ALCHbCopyStrategy implements CopyStrategy<ALCHbLiteral, ALCHbTerm, Map<ALCHbTerm, List<ALCHbTerm>>> {

	private static final String COPY_SYMBOL = "'";
	
	private Map<ALCHbTerm, List<ALCHbTerm>> copies;
	private final ALCHbFactory factory;
	
	public ALCHbCopyStrategy(ALCHbFactory factory) {
		this.copies = new HashMap<>();
		this.factory = factory;
	}

	@Override
	public Optional<Collection<ALCHbLiteral>> copy(Collection<ALCHbLiteral> clause) {

		// "open" map to add new copies
		copies = new HashMap<>(copies);
		
		// get terms and their copies
		Set<ALCHbTerm> terms = clause.stream()
				.flatMap(ALCHbLiteral::fullTerms)
				.collect(Collectors.toSet());
		
		Set<ALCHbIndividual> inds = terms.stream()
				.filter(t -> t instanceof ALCHbIndividual)
				.map(t -> (ALCHbIndividual) t)
				.collect(Collectors.toSet());
		
		Map<ALCHbTerm, ALCHbTerm> termsMap = new HashMap<>();
		inds.forEach(ind -> termsMap.put(ind, ind));
		
		Map<ALCHbVariable, ALCHbVariable> varsMap = terms.stream()
				.filter(t -> t instanceof ALCHbVariable)
				.collect(Collectors.toMap(t -> (ALCHbVariable) t, t -> copyVariable((ALCHbVariable) t)));
		
		termsMap.putAll(varsMap);
		
		Map<ALCHbUnaryIndividual, ALCHbUnaryIndividual> unIndMap = terms.stream()
				.filter(t -> t instanceof ALCHbUnaryIndividual)
				.collect(Collectors.toMap(t -> (ALCHbUnaryIndividual) t, t -> copyUnaryInd((ALCHbUnaryIndividual) t, termsMap)));
		
		termsMap.putAll(unIndMap);
		
		// get the new clause
		Set<ALCHbLiteral> copiedClause = clause.stream()
				.map(l -> copyLiteral(l, termsMap))
				.collect(Collectors.toSet());
		
		// "closes" the map after adding copies
		copies = Collections.unmodifiableMap(copies);
		
		return Optional.of(copiedClause);
		
	}
	
	private ALCHbVariable copyVariable(ALCHbVariable var) {
		var = var.getCopyOf() == null ? var : (ALCHbVariable) var.getCopyOf();
				
		if (!copies.containsKey(var))
			copies.put(var, Collections.emptyList());

		// "opens" the list to add new copies
		List<ALCHbTerm> copiesList = new ArrayList<>(copies.get(var));
		
		int numberOfCopies = copiesList.size();
		String newTermName = var.getName() + COPY_SYMBOL.repeat(numberOfCopies + 1);
		
		ALCHbVariable newVar = factory.var(newTermName, var);
		copiesList.add(newVar);
		
		// "closes" the list and adds it to the map
		copies.put(var, Collections.unmodifiableList(copiesList));
		
		return newVar;
	}
	
	private ALCHbUnaryIndividual copyUnaryInd(ALCHbUnaryIndividual term, Map<ALCHbTerm, ALCHbTerm> termsMap) {
		term = term.getCopyOf() == null ? term : (ALCHbUnaryIndividual) term.getCopyOf();
		
		if (!copies.containsKey(term))
			copies.put(term, Collections.emptyList());

		// "opens" the list to add new copies
		List<ALCHbTerm> copiesList = new ArrayList<>(copies.get(term));
		
		int numberOfCopies = copiesList.size();
		String newTermName = term.getName() + COPY_SYMBOL.repeat(numberOfCopies + 1);
		
		ALCHbUnaryIndividual newUnaryInd = factory.unaryInd(newTermName, termsMap.get(term.getFillerTerm()), term);
		copiesList.add(newUnaryInd);

		// "closes" the list and adds it to the map
		copies.put(term, Collections.unmodifiableList(copiesList));
		
		return newUnaryInd;
	}

	private ALCHbLiteral copyLiteral(ALCHbLiteral literal, Map<ALCHbTerm, ALCHbTerm> termsMap) {
		if(literal instanceof ALCHbConceptLiteral) {
			ALCHbConceptLiteral conLit = (ALCHbConceptLiteral) literal;
			return factory.conLiteral(conLit.getName(), conLit.isPositive(), termsMap.get(conLit.getTerm()));
		}
		
		if(literal instanceof ALCHbRoleLiteral) {
			ALCHbRoleLiteral roleLit = (ALCHbRoleLiteral) literal;
			return factory.roleLiteral(roleLit.getName(), roleLit.isPositive(), 
					termsMap.get(roleLit.getFirst()),
					termsMap.get(roleLit.getSecond()));
		}
		
		if(literal instanceof ALCHbOrderedLiteral) {
			ALCHbOrderedLiteral ordLit = (ALCHbOrderedLiteral) literal;
			return factory.ordLiteral(ordLit.isPositive(), 
					termsMap.get(ordLit.getFirst()),
					termsMap.get(ordLit.getSecond()));
		}
		
		if(literal instanceof ALCHbBiOrderedLiteral) {
			ALCHbBiOrderedLiteral biOrdLit = (ALCHbBiOrderedLiteral) literal;
			return factory.biOrdLiteral(biOrdLit.isPositive(), 
					termsMap.get(biOrdLit.getFirst()),
					termsMap.get(biOrdLit.getSecond()), 
					termsMap.get(biOrdLit.getThird()),
					termsMap.get(biOrdLit.getFourth()));
		}
		
		throw new ClassCastException("Literal invalid for " + literal.getClass());		
	}

	@Override
	public void clear() {
		copies = Collections.emptyMap();
	}

	@Override
	public Map<ALCHbTerm, List<ALCHbTerm>> getState() {
		return copies;
	}

	@Override
	public void setState(Map<ALCHbTerm, List<ALCHbTerm>> state) {
		copies = Collections.unmodifiableMap(new HashMap<>(state));
	}

}
