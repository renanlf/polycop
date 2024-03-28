package edu.br.ufpe.cin.sword.cm.donothing;

import java.util.Collection;
import java.util.Optional;

import edu.br.ufpe.cin.sword.cm.strategies.CopyStrategy;

public class DoNothingCopyStrategy<Literal> implements CopyStrategy<Literal, Void> {

    private boolean alwaysCopy;

    private DoNothingCopyStrategy(boolean alwaysCopy) {
        this.alwaysCopy = alwaysCopy;
    }

    public static <L> DoNothingCopyStrategy<L> alwaysCopyStrategy() {
        return new DoNothingCopyStrategy<L>(true);
    }

    public static <L> DoNothingCopyStrategy<L> neverCopyStrategy() {
        return new DoNothingCopyStrategy<L>(false);
    }

    @Override
    public Optional<Collection<Literal>> copy(Collection<Literal> clause) {
        if (alwaysCopy) {
            return Optional.of(clause);
        }

        return Optional.empty();
    }

    @Override
    public void clear() { }

    @Override
    public Void getState() {
        return null;
    }

    @Override
    public void setState(Void state) { }

}
