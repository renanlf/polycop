package edu.br.ufpe.cin.sword.cm.propositional.mapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.br.ufpe.cin.sword.cm.mapper.MatrixMapper;
import edu.br.ufpe.cin.sword.cm.mapper.exceptions.FileParserException;
import edu.br.ufpe.cin.sword.cm.mapper.listeners.ClauseListener;
import edu.br.ufpe.cin.sword.cm.mapper.listeners.MatrixListener;

public class DimacsCNFMatrixMapper implements MatrixMapper<Integer> {

    private final Set<ClauseListener<Integer>> clauseListeners;
    private final Set<MatrixListener<Integer>> matrixListeners;

    public DimacsCNFMatrixMapper() {
        this.clauseListeners = new HashSet<>();
        this.matrixListeners = new HashSet<>();
    }

    @Override
    public List<List<Integer>> map(File file) throws IOException, FileParserException {
        Pattern pLinePattern = Pattern.compile("p[ \t\n]*cnf[ \t\n]*(\\d+)[ \t\n]*(\\d+)");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean readingClauses = false;
            List<List<Integer>> matrix = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (!readingClauses) {
                    Matcher matcher = pLinePattern.matcher(line);
                    if (matcher.matches()) {
                        readingClauses = true;
                    }
                } else {
                    List<Integer> clause = new ArrayList<>();
                    try (Scanner scanner = new Scanner(line)) {
                        while (scanner.hasNextInt()) {
                            int number = scanner.nextInt();
                            number *= -1; // converting to DNF

                            if (number == 0) {
                                break;
                            }

                            clause.add(number);
                        }
                    }

                    notifyClauseListeners(clause);
                    matrix.add(clause);
                }
            }
            notifyMatrixListeners(matrix);
            return matrix;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new FileParserException();
        }
    }

    @Override
    public void addClauseListener(ClauseListener<Integer> clauseListener) {
        clauseListeners.add(clauseListener);
    }

    @Override
    public void addMatrixListener(MatrixListener<Integer> matrixListener) {
        matrixListeners.add(matrixListener);
    }

    private void notifyClauseListeners(List<Integer> clause) {
        clauseListeners.forEach(clauseListener -> clauseListener.onClauseMap(clause));
    }

    private void notifyMatrixListeners(List<List<Integer>> matrix) {
        matrixListeners.forEach(matrixListener -> matrixListener.onMatrixMap(matrix));
    }

}
