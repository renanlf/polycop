package edu.br.ufpe.cin.sword.cm;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.br.ufpe.cin.sword.cm.mapper.exceptions.FileParserException;
import edu.br.ufpe.cin.sword.cm.propositional.PropositionalConnectionProverDecorator;
import edu.br.ufpe.cin.sword.cm.tree.FailProofTree;
import edu.br.ufpe.cin.sword.cm.tree.ProofTree;
import edu.br.ufpe.cin.sword.cm.util.LaTeXGenerator;

public class MainExecutable {
    public static void main(String[] args) throws IOException, FileParserException {
        String regex = "-([^\s]+)\s+([^\s]+)";
        Pattern pattern = Pattern.compile(regex);

        Map<String, String> mapArgs = new HashMap<>();
        int length = args.length;

        if (length % 2 == 1) {
            throw new IllegalArgumentException("invalid number of arguments");
        }

        for (int i = 0; i < length - 1; i=i+2) {
            String arg = String.join(" ", args[i], args[i+1]);
            Matcher matcher = pattern.matcher(arg);
            if (matcher.matches()) {
                mapArgs.put(matcher.group(1), matcher.group(2));
            } else {
                throw new IllegalArgumentException("invalid argument for '" + arg + "'");
            }
        }

        String prover = mapArgs.get("prover");       
        String file = mapArgs.get("file");
        String latexFilepath = mapArgs.get("latex");

        if (prover == null) {
            throw new IllegalArgumentException("must provide a prover");
        }

        if (file == null) {
            throw new IllegalArgumentException("must provide a file");
        }

        runProver(prover, file, latexFilepath);
    }

    private static void runProver(String prover, String file, String latexFilepath) throws IOException, FileParserException {
        switch (prover.toLowerCase()) {
            case "propositional":
                runPropositionalProver(file, latexFilepath);
                break;
            default:
                throw new IllegalArgumentException("prover invalid");
        }
    }

    private static void runPropositionalProver(String file, String latexFilepath) throws IOException, FileParserException {
        PropositionalConnectionProverDecorator prover = new PropositionalConnectionProverDecorator();
        var proofTree = prover.prove(new File(file));

        if (proofTree instanceof FailProofTree<Integer>) {
            System.out.println(file + " is unsatisfiable");
        } else {
            System.out.println(file + " is satisfiable");
        }

        if (latexFilepath != null) {
            generateLaTeXFile(proofTree, latexFilepath);
        }
    }

    private static <Literal> void generateLaTeXFile(ProofTree<Literal> proofTree, String filepath) throws FileNotFoundException {
        var laTeXString = LaTeXGenerator.generateProofDocument(proofTree);

        try (PrintWriter printWriter = new PrintWriter(filepath)) {
            printWriter.println(laTeXString);
        }
    }
}
