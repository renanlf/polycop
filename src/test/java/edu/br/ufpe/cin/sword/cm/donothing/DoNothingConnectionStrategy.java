package edu.br.ufpe.cin.sword.cm.donothing;

import edu.br.ufpe.cin.sword.cm.strategies.ConnectionStrategy;

public class DoNothingConnectionStrategy<Literal> implements ConnectionStrategy<Literal, Void> {

    private boolean alwaysConnect;

    private DoNothingConnectionStrategy(boolean alwaysConnect) {
        this.alwaysConnect = alwaysConnect;
    }

    public static <L> DoNothingConnectionStrategy<L> alwaysConnectStrategy() {
        return new DoNothingConnectionStrategy<L>(true);
    }

    public static <L> DoNothingConnectionStrategy<L> neverConnectStrategy() {
        return new DoNothingConnectionStrategy<L>(true);
    }

    @Override
    public boolean connect(Literal literal, Literal other) {
        return alwaysConnect;
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
