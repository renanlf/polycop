package edu.br.ufpe.cin.sword.cm.mapper.listeners;

import java.util.List;

public interface MatrixListener<Literal> {

    void onMatrixMap(List<List<Literal>> matrix);

}
