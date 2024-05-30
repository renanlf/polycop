package edu.br.ufpe.cin.sword.cm.propositional.strategies;

import java.util.List;
import java.util.Set;

import edu.br.ufpe.cin.sword.cm.node.LinkedNode;
import edu.br.ufpe.cin.sword.cm.strategies.BlockingStrategy;

public class PropositionalBlockingStrategy implements BlockingStrategy<Integer, Void, LinkedNode<List<Integer>>> {

    @Override
    public boolean isBlocked(Integer literal, Set<Integer> path, Void connState, LinkedNode<List<Integer>> copyState) {
        return false;
    }

}
