package edu.br.ufpe.cin.sword.cm.propositional.strategies;

import java.util.List;
import java.util.Optional;

import edu.br.ufpe.cin.sword.cm.node.LinkedNode;
import edu.br.ufpe.cin.sword.cm.strategies.CopyStrategy;

public class PropositionalCopyStrategy implements CopyStrategy<Integer, LinkedNode<List<Integer>>> {

    private LinkedNode<List<Integer>> clausesUsed;

    @Override
    public Optional<List<Integer>> copy(List<Integer> clause) {
        if (clausesUsed != null) {        
            if (clausesUsed.contains(clause)) 
                return Optional.empty();

            clausesUsed = clausesUsed.push(clause);
        } else {
            clausesUsed = new LinkedNode<List<Integer>>(clause);
        }

        return Optional.of(clause);        
    }

    @Override
    public void clear() { 
        clausesUsed = null;
    }

    @Override
    public LinkedNode<List<Integer>> getState() {
        return clausesUsed;
    }

    @Override
    public void setState(LinkedNode<List<Integer>> state) {
        this.clausesUsed = state;
    }

}
