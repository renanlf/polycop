package edu.br.ufpe.cin.sword.cm.mapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.br.ufpe.cin.sword.cm.mapper.exceptions.FileParserException;
import edu.br.ufpe.cin.sword.cm.mapper.listeners.ClauseListener;
import edu.br.ufpe.cin.sword.cm.mapper.listeners.MatrixListener;

public interface MatrixMapper<Literal> {

    List<List<Literal>> map(File file) throws IOException, FileParserException;

    void addClauseListener(ClauseListener<Literal> clauseListener);

    void addMatrixListener(MatrixListener<Literal> matrixListener);

}
