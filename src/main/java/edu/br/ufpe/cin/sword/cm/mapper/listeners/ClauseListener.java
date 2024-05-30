package edu.br.ufpe.cin.sword.cm.mapper.listeners;

import java.util.List;

public interface ClauseListener<Literal> {

    void onClauseMap(List<Literal> clause);

}
