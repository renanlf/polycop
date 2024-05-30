package edu.br.ufpe.cin.sword.cm.sat.strategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import edu.br.ufpe.cin.sword.cm.node.LinkedNode;
import edu.br.ufpe.cin.sword.cm.strategies.CopyStrategy;

public class SATCopyStrategy implements CopyStrategy<Integer, LinkedNode<Collection<Integer>>> {

    private LinkedNode<Collection<Integer>> clausesUsed;

    @Override
    public Optional<Collection<Integer>> copy(Collection<Integer> clause) {
        if (clausesUsed != null) {        
            if (clausesUsed.contains(clause)) 
                return Optional.empty();

            clausesUsed = clausesUsed.push(clause);
        } else {
            clausesUsed = new LinkedNode<Collection<Integer>>(clause);
        }

        return Optional.of(clause);        
    }

    @Override
    public void clear() { 
        clausesUsed = null;
    }

    @Override
    public LinkedNode<Collection<Integer>> getState() {
        return clausesUsed;
    }

    @Override
    public void setState(LinkedNode<Collection<Integer>> state) {
        this.clausesUsed = state;
    }

}
