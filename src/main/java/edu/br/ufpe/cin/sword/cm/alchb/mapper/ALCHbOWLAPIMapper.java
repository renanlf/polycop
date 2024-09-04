package edu.br.ufpe.cin.sword.cm.alchb.mapper;

import edu.br.ufpe.cin.sword.cm.alchb.factories.ALCHbFactory;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import edu.br.ufpe.cin.sword.cm.mapper.MatrixMapper;
import edu.br.ufpe.cin.sword.cm.mapper.exceptions.FileParserException;
import edu.br.ufpe.cin.sword.cm.mapper.listeners.ClauseListener;
import edu.br.ufpe.cin.sword.cm.mapper.listeners.MatrixListener;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DLExpressivityChecker;
import org.semanticweb.owlapi.util.Languages;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ALCHbOWLAPIMapper implements MatrixMapper<ALCHbLiteral> {
    private final List<ClauseListener<ALCHbLiteral>> clauseListeners;
    private final List<MatrixListener<ALCHbLiteral>> matrixListeners;

    public ALCHbOWLAPIMapper() {
        clauseListeners = new ArrayList<>();
        matrixListeners = new ArrayList<>();
    }

    @Override
    public List<List<ALCHbLiteral>> map(File file) throws IOException, FileParserException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        try {
            var ontology = manager.loadOntologyFromOntologyDocument(file);
            checkExpressivity(ontology);
            var normalizedAxioms = getNormalizedAxioms(ontology);
            return mapAxiomsToMatrix(normalizedAxioms);
        } catch (OWLOntologyCreationException e) {
            throw new FileParserException();
        }
    }

    @Override
    public void addClauseListener(ClauseListener<ALCHbLiteral> clauseListener) {
        clauseListeners.add(clauseListener);
    }

    @Override
    public void addMatrixListener(MatrixListener<ALCHbLiteral> matrixListener) {
        matrixListeners.add(matrixListener);
    }

    private List<List<ALCHbLiteral>> mapAxiomsToMatrix(List<OWLAxiom> axioms) {
        ALCHbMapOWLAxiomVisitor mapOWLAxiomVisitor = new ALCHbMapOWLAxiomVisitor(new ALCHbFactory());
        return axioms.stream()
                .flatMap(axiom -> mapOWLAxiomVisitor.visit(axiom).stream())
                .toList();
    }

    private List<OWLAxiom> getNormalizedAxioms(OWLOntology ontology) {
        var owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
        List<OWLAxiom> axiomsToNormalize = new ArrayList<>(ontology.getAxioms());
        List<OWLAxiom> normalizedAxioms = new ArrayList<>();
        while (!axiomsToNormalize.isEmpty()) {
            var axiomToNormalize = axiomsToNormalize.remove(0);
            var axiomVisitor = new ALCHbNormalizeOWLAxiomVisitor(owlDataFactory);
            var visit = axiomVisitor.visit(axiomToNormalize);
            normalizedAxioms.addAll(visit.getNormalizedAxioms());
            axiomsToNormalize.addAll(visit.getAxiomsToNormalize());
        }

        return normalizedAxioms;
    }

    private void checkExpressivity(OWLOntology ontology) throws FileParserException {
        DLExpressivityChecker dlExpressivityChecker = new DLExpressivityChecker(List.of(ontology));
        if (!dlExpressivityChecker.isWithin(Languages.ALCH)) { // if ontology expressivity is greater than alch
            throw new FileParserException();
        }
    }
}
