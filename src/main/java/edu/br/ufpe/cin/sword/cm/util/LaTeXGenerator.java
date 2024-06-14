package edu.br.ufpe.cin.sword.cm.util;

import edu.br.ufpe.cin.sword.cm.tree.ProofTree;

public class LaTeXGenerator {

    private static final String prefix = """
\\documentclass[convert={outext=.eps, command=\\unexpanded{pdftops -eps \\infile}}]{standalone}
\\usepackage{bussproofs} % for sequent-style proofs
\\begin{document}""";

    private static final String suffix = """
\\DisplayProof

\\end{document}""";

    public static <Literal> String generateProofDocument(ProofTree<Literal> proofTree) {
        String proofString = proofTree.latexString();

        return String.join("\n", prefix, proofString, suffix);
    }
    
}
