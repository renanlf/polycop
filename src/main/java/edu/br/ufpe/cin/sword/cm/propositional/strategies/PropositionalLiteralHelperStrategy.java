package edu.br.ufpe.cin.sword.cm.propositional.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.br.ufpe.cin.sword.cm.mapper.listeners.ClauseListener;
import edu.br.ufpe.cin.sword.cm.mapper.listeners.MatrixListener;
import edu.br.ufpe.cin.sword.cm.strategies.LiteralHelperStrategy;

public class PropositionalLiteralHelperStrategy implements LiteralHelperStrategy<Integer>, ClauseListener<Integer>, MatrixListener<Integer> {

    private Map<Integer, List<List<Integer>>> clausesComplementaryLiterals;

    public PropositionalLiteralHelperStrategy() {
        this.clausesComplementaryLiterals = new HashMap<>();
    }

    @Override
    public boolean complementaryOf(Integer literal, Integer other) {
        return literal + other == 0;
    }

    @Override
    public List<List<Integer>> complementaryOfInMatrix(Integer literal, List<List<Integer>> matrix) {
        return clausesComplementaryLiterals.getOrDefault(literal, List.of());
    }

    @Override
    public void onClauseMap(List<Integer> clause) {
        for (Integer literal : clause) {
            Integer negLiteral = -literal;
            List<List<Integer>> clausesComplementary = clausesComplementaryLiterals.getOrDefault(negLiteral, new ArrayList<>());
            clausesComplementary.add(clause);
            clausesComplementaryLiterals.put(negLiteral, clausesComplementary);
        }
    }

    @Override
    public void onMatrixMap(List<List<Integer>> matrix) {
        clausesComplementaryLiterals = Map.copyOf(clausesComplementaryLiterals);
    }

    @Override
    public void clear() {
        clausesComplementaryLiterals = new HashMap<>();
    }

}
