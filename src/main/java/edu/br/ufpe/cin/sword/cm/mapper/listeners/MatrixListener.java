package edu.br.ufpe.cin.sword.cm.mapper.listeners;

import java.util.Collection;

public interface MatrixListener<Literal> {

    void onMatrixMap(Collection<Collection<Literal>> matrix);

}
