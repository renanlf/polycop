package edu.br.ufpe.cin.sword.cm.propositional.strategies;

import edu.br.ufpe.cin.sword.cm.strategies.ConnectionStrategy;

public class PropositionalConnectionStrategy implements ConnectionStrategy<Integer, Void> {

    @Override
    public boolean connect(Integer literal, Integer other) {
        return literal + other == 0;
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
