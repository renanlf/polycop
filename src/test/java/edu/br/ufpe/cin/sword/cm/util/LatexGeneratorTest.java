package edu.br.ufpe.cin.sword.cm.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import edu.br.ufpe.cin.sword.cm.mapper.exceptions.FileParserException;
import edu.br.ufpe.cin.sword.cm.propositional.PropositionalConnectionProverDecorator;

public class LatexGeneratorTest {

    @Test
    public void testGenerateLatexPaperExample() throws IOException, FileParserException {  
        // GIVEN      
        File file = new File("src/test/resources/propositional/paper_example.cnf");
        PropositionalConnectionProverDecorator prover = new PropositionalConnectionProverDecorator();
        var proofTree = prover.prove(file);
        var expectedOutput = """
\\documentclass[convert={outext=.eps, command=\\unexpanded{pdftops -eps \\infile}}]{standalone}
\\usepackage{bussproofs} % for sequent-style proofs
\\begin{document}
\\AxiomC{}
\\RightLabel{Ax}
\\UnaryInfC{$\\{\\}, M, [1, 2, -3]$}
\\AxiomC{}
\\RightLabel{Ax}
\\UnaryInfC{$\\{\\}, M, [1, 2]$}
\\RightLabel{\\textit{Ext}}
\\BinaryInfC{$[-3], M, [1, 2]$}

\\AxiomC{}
\\RightLabel{Ax}
\\UnaryInfC{$\\{\\}, M, [1]$}
\\RightLabel{\\textit{Ext}}
\\BinaryInfC{$[2], M, [1]$}

\\AxiomC{}
\\RightLabel{Ax}
\\UnaryInfC{$\\{\\}, M, []$}
\\RightLabel{\\textit{Ext}}
\\BinaryInfC{$[1], M, []$}

\\RightLabel{\\textit{St}}
\\UnaryInfC{$\\varepsilon, M, \\varepsilon$}

\\DisplayProof

\\end{document}""";
        expectedOutput = expectedOutput.replace("\n", "").replace("\r", "");

        // WHEN
        var latexString = LaTeXGenerator.generateProofDocument(proofTree);

        // THEN
        assertNotNull(latexString);
        var actualOutput = latexString.replace("\n", "").replace("\r", "");
        assertEquals(expectedOutput, actualOutput);
    }
    
}
