package edu.br.ufpe.cin.sword.cm.donothing;

import java.util.Set;

import edu.br.ufpe.cin.sword.cm.strategies.BlockingStrategy;

public class DoNothingBlockingStrategy<Literal> implements BlockingStrategy<Literal, Void, Void> {

    private boolean alwaysBlock;

    private DoNothingBlockingStrategy(boolean alwaysBlock) {
        this.alwaysBlock = alwaysBlock;
    }

    public static <L> DoNothingBlockingStrategy<L> alwaysBlockStrategy() {
        return new DoNothingBlockingStrategy<>(true);
    }

    public static <L> DoNothingBlockingStrategy<L> neverBlockStrategy() {
        return new DoNothingBlockingStrategy<>(false);
    }

    @Override
    public boolean isBlocked(Literal literal, Set<Literal> path, Void connState, Void copyState) {
        return alwaysBlock;
    }

}
