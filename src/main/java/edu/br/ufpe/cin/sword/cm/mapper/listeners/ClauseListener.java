package edu.br.ufpe.cin.sword.cm.mapper.listeners;

import java.util.Collection;

public interface ClauseListener<Literal> {

    void onClauseMap(Collection<Literal> clause);

}
