package edu.br.ufpe.cin.sword.cm.donothing;

import edu.br.ufpe.cin.sword.cm.strategies.LiteralHelperStrategy;

public class DoNothingLiteralHelperStrategy<Literal> implements LiteralHelperStrategy<Literal> {

    private boolean alwaysComplementary;

    private DoNothingLiteralHelperStrategy(boolean alwaysComplementary) {
        this.alwaysComplementary = alwaysComplementary;
    }

    public static <L> DoNothingLiteralHelperStrategy<L> alwaysComplementaryHelperStrategy() {
        return new DoNothingLiteralHelperStrategy<>(true);
    }

    public static <L> DoNothingLiteralHelperStrategy<L> neverComplementaryHelperStrategy() {
        return new DoNothingLiteralHelperStrategy<>(false);
    }
 
    @Override
    public boolean complementaryOf(Literal literal, Literal other) {
        return alwaysComplementary;
    }

}
