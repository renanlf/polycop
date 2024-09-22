package edu.br.ufpe.cin.sword.cm.alchb.strategies;

import java.util.*;
import java.util.stream.Collectors;

import edu.br.ufpe.cin.sword.cm.alchb.factories.ALCHbFactory;
import edu.br.ufpe.cin.sword.cm.alchb.model.*;
import edu.br.ufpe.cin.sword.cm.strategies.BlockingStrategy;

public class ALCHbBlockingStrategy implements BlockingStrategy<ALCHbLiteral, Map<ALCHbVariable, ALCHbTerm>, Map<ALCHbTerm, List<ALCHbTerm>>> {

	@Override
	public boolean isBlocked(ALCHbLiteral literal, Set<ALCHbLiteral> path, Map<ALCHbVariable, ALCHbTerm> subs,
			Map<ALCHbTerm, List<ALCHbTerm>> copies) {
		
		if(literal instanceof ALCHbConceptLiteral) 
			return isBlocked((ALCHbConceptLiteral) literal, path, subs, copies);
		
		if(literal instanceof ALCHbRoleLiteral) 
			return isBlocked((ALCHbRoleLiteral) literal, path, subs, copies);
		
		if(literal instanceof ALCHbOrderedLiteral) 
			return isBlocked((ALCHbOrderedLiteral) literal, path, subs);
		
		if(literal instanceof ALCHbBiOrderedLiteral) 
			return isBlocked((ALCHbBiOrderedLiteral) literal, path, subs);
		
		return false;
		
	}
	
	private boolean isBlocked(ALCHbConceptLiteral literal, Set<ALCHbLiteral> path, Map<ALCHbVariable, ALCHbTerm> subs,
			Map<ALCHbTerm, List<ALCHbTerm>> copies) {
		ALCHbTerm term = literal.getTerm();

		Set<String> termConceptSet = conceptsSet(term, path, subs);

		
		if (termConceptSet.contains(literal.getName())) {
			System.out.println(termConceptSet + " | " + term);
			return true;
		}
		
		if(term.getCopyOf() == null) return false;
		
		ALCHbTerm originalTerm = term.getCopyOf();
		List<ALCHbTerm> termCopies = copies.get(originalTerm);

		int index = termCopies.indexOf(term);
		
		if(index < 1) return false;
		

		// to think: the termConceptSet should include literal name.
		termConceptSet = new HashSet<>(termConceptSet);
		termConceptSet.add(literal.getName());

		var substitution = getSubstitution(term, subs);
		var previousSubstitutions = termCopies.subList(0, termCopies.size() - 1).stream().map(termCopy -> getSubstitution(termCopy, subs)).toList();
		if ((substitution instanceof ALCHbIndividual) && !previousSubstitutions.contains(substitution)) {
			return false;
		}

		while (index > 0) {
			ALCHbTerm previousTerm = termCopies.get(index - 1);
			if (conceptsSet(previousTerm, path, subs).containsAll(termConceptSet)) {
				System.out.printf("[blocked] %s(%s) was blocked by %s(%s)%n", term, getSubstitution(term, subs), previousTerm, getSubstitution(previousTerm, subs));
				return true;
			}
			index--;
		}

		
		return false;
	}
	
	private boolean isBlocked(ALCHbRoleLiteral literal, Set<ALCHbLiteral> path, Map<ALCHbVariable, ALCHbTerm> subs,
			Map<ALCHbTerm, List<ALCHbTerm>> copies) {
		ALCHbTerm first = literal.getFirst();
		ALCHbTerm second = literal.getSecond();

		Set<String> rolesSet = rolesSet(first, second, path, subs);
		

		if (rolesSet.contains(literal.getName())) {
			System.out.println(rolesSet + " | " + first + ", " + second);
			return true;
		}
		
		if(first.getCopyOf() == null && second.getCopyOf() == null) return false;
		
		ALCHbTerm previousFirst = first;
		if(first.getCopyOf() != null) {
			int indexFirst = copies.get(first.getCopyOf()).indexOf(first);
			if(indexFirst > 0)
				previousFirst = copies.get(first.getCopyOf()).get(indexFirst - 1);
		}
		
		ALCHbTerm previousSecond = second;
		if(second.getCopyOf() != null) {
			int indexSecond = copies.get(second.getCopyOf()).indexOf(second);
			if(indexSecond > 0)
				previousSecond = copies.get(second.getCopyOf()).get(indexSecond - 1);
		}

		// to think: the rolesSet should include literal name.
		rolesSet = new HashSet<>(rolesSet);
		rolesSet.add(literal.getName());
		
		if (rolesSet(previousFirst, previousSecond, path, subs).containsAll(rolesSet)) {
			System.out.println(rolesSet + " | " + first + ", " + second);
			return true;
		}

		var firstTermSubstituted = getSubstitution(first, subs);
		var conceptsSetFirstTermSubstituted = conceptsSet(firstTermSubstituted, path, subs);
		boolean termBlockedByPredecessor = path.stream()
				.filter(lit -> lit instanceof ALCHbRoleLiteral)
				.map(lit -> (ALCHbRoleLiteral) lit)
				.filter(roleLit -> roleLit.getName().equals(literal.getName()) && getSubstitution(roleLit.getSecond(), subs).equals(firstTermSubstituted))
				.map(ALCHbRoleLiteral::getFirst)
				.map(term -> getSubstitution(term, subs))
				.anyMatch(term -> conceptsSet(term, path, subs).containsAll(conceptsSetFirstTermSubstituted));

		if (termBlockedByPredecessor) {
			return true;
		}

		return false;
	}
	
	private boolean isBlocked(ALCHbOrderedLiteral literal, Set<ALCHbLiteral> path, Map<ALCHbVariable, ALCHbTerm> subs) {
		Set<Map.Entry<ALCHbTerm, ALCHbTerm>> edges = path.stream()
				.filter(l -> l instanceof ALCHbOrderedLiteral)
				.map(l -> Map.entry(((ALCHbOrderedLiteral) l).getFirst(), 
						((ALCHbOrderedLiteral) l).getSecond()))
				.collect(Collectors.toSet());
		
		ALCHbTerm term = getSubstitution(literal.getFirst(), subs);
		
		List<ALCHbTerm> cursors = new ArrayList<>();
		cursors.add(term);
		
		do {
			ALCHbTerm cursor = cursors.get(0);
			
			Set<ALCHbTerm> moreTypicalTerms = edges.stream()
					.filter(e -> Objects.equals(getSubstitution(e.getKey(), subs), cursor))
					.map(e -> getSubstitution(e.getValue(), subs))
					.collect(Collectors.toSet());
			
			if(moreTypicalTerms.contains(term)) {
				return true;
			}
			
			cursors.remove(cursor);
			cursors.addAll(moreTypicalTerms);			
			
		} while (!cursors.isEmpty());
		
		return false;
		
	}
	
	private boolean isBlocked(ALCHbBiOrderedLiteral literal, Set<ALCHbLiteral> path, Map<ALCHbVariable, ALCHbTerm> subs) {
		Set<Map.Entry<Map.Entry<ALCHbTerm, ALCHbTerm>, Map.Entry<ALCHbTerm, ALCHbTerm>>> edges = path.stream()
				.filter(l -> l instanceof ALCHbBiOrderedLiteral)
				.map(l -> Map.entry(
						Map.entry(((ALCHbBiOrderedLiteral) l).getFirst(), ((ALCHbBiOrderedLiteral) l).getSecond()),
						Map.entry(((ALCHbBiOrderedLiteral) l).getThird(), ((ALCHbBiOrderedLiteral) l).getFourth()))
				)
				.collect(Collectors.toSet());
		
		Map.Entry<ALCHbTerm, ALCHbTerm> pair = Map.entry(getSubstitution(literal.getFirst(), subs), getSubstitution(literal.getSecond(), subs));
		
		List<Map.Entry<ALCHbTerm, ALCHbTerm>> cursors = new ArrayList<>();
		cursors.add(pair);
		
		do {
			Map.Entry<ALCHbTerm, ALCHbTerm> cursor = cursors.get(0);
			
			Set<Map.Entry<ALCHbTerm, ALCHbTerm>> moreTypicalPairs = edges.stream()
					.filter(e -> Objects.equals(getSubstitution(e.getKey().getKey(), subs), cursor.getKey())
							&& Objects.equals(getSubstitution(e.getKey().getValue(), subs), cursor.getValue()))
					.map(e -> Map.entry(getSubstitution(e.getValue().getKey(), subs), 
							getSubstitution(e.getValue().getValue(), subs)))
					.collect(Collectors.toSet());
			
			if(moreTypicalPairs.contains(pair)) {
				return true;
			}
			
			cursors.remove(cursor);
			cursors.addAll(moreTypicalPairs);			
			
		} while (!cursors.isEmpty());
		
		return false;
		
	}

	private Set<String> conceptsSet(ALCHbTerm term, Set<ALCHbLiteral> path, Map<ALCHbVariable, ALCHbTerm> subs) {
		final ALCHbTerm subsTerm = getSubstitution(term, subs);
		return path.stream().filter(l -> l instanceof ALCHbConceptLiteral).map(l -> (ALCHbConceptLiteral) l)
				.filter(l -> Objects.equals(getSubstitution(l.getTerm(), subs), subsTerm)).map(ALCHbConceptLiteral::getName)
				.collect(Collectors.toSet());
	}
	
	private Set<String> rolesSet(ALCHbTerm first, ALCHbTerm second, Set<ALCHbLiteral> path, Map<ALCHbVariable, ALCHbTerm> subs) {
		final ALCHbTerm subsFirst = getSubstitution(first, subs);
		final ALCHbTerm subsSecond = getSubstitution(second, subs);
		
		return path.stream().filter(l -> l instanceof ALCHbRoleLiteral).map(l -> (ALCHbRoleLiteral) l)
				.filter(l -> Objects.equals(getSubstitution(l.getFirst(), subs), subsFirst)
						&& Objects.equals(getSubstitution(l.getSecond(), subs), subsSecond))
				.map(ALCHbRoleLiteral::getName)
				.collect(Collectors.toSet());
	}
	
	private ALCHbTerm getSubstitution(ALCHbTerm term, Map<ALCHbVariable, ALCHbTerm> subs) {
		if (term instanceof ALCHbUnaryIndividual unaryIndividual) {
			var fillerTerm = getSubstitution(unaryIndividual.getFillerTerm(), subs);
			return new ALCHbFactory().unaryInd(unaryIndividual.getName(), fillerTerm);
		}

		while (subs.containsKey(term)) {
			term = subs.get(term);
		}

		return term;
	}

}
