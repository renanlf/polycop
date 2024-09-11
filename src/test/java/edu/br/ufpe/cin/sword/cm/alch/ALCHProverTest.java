package edu.br.ufpe.cin.sword.cm.alch;

import edu.br.ufpe.cin.sword.cm.alchb.ALCHbSimpleProver;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbLiteral;
import edu.br.ufpe.cin.sword.cm.tree.AxiomProofTree;
import edu.br.ufpe.cin.sword.cm.tree.FailProofTree;
import edu.br.ufpe.cin.sword.cm.tree.StartProofTree;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class ALCHProverTest {

    private ALCHbSimpleProver prover;

    @Before
    public void setUp() {
        this.prover = new ALCHbSimpleProver();
    }

    @Test
    public void testProveOntology01() {
        // GIVEN
        var file = new File("src/test/resources/alch/01_test_acyclic_tbox_abox_inconsistency.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof StartProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology02() {
        // GIVEN
        var file = new File("src/test/resources/alch/02_test_acyclic_inconsistency001.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof StartProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology03() {
        // GIVEN
        var file = new File("src/test/resources/alch/03_test_acyclic_tbox_inconsistency001.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof StartProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology04() {
        // GIVEN
        var file = new File("src/test/resources/alch/04_test_acyclic_tbox_inconsistency002.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof AxiomProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology05() {
        // GIVEN
        var file = new File("src/test/resources/alch/05_test_acyclic_tbox_inconsistency003.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof StartProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology06() {
        // GIVEN
        var file = new File("src/test/resources/alch/06_test_thing_consistency.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof FailProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology07() {
        // GIVEN
        var file = new File("src/test/resources/alch/07_test_thing_inconsistency.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof AxiomProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology08() {
        // GIVEN
        var file = new File("src/test/resources/alch/08_test_nothing_consistency.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof FailProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology09() {
        // GIVEN
        var file = new File("src/test/resources/alch/09_test_nothing_inconsistency.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof AxiomProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology10() {
        // GIVEN
        var file = new File("src/test/resources/alch/10_test_skolem_inconsistency.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof StartProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology11() {
        // GIVEN
        var file = new File("src/test/resources/alch/11_test_skolem_consistency.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof FailProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology12() {
        // GIVEN
        var file = new File("src/test/resources/alch/12_test_path_instantiation_consistency.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof FailProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology13() {
        // GIVEN
        var file = new File("src/test/resources/alch/13_test_cyclic_consistency001.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof FailProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology14() {
        // GIVEN
        var file = new File("src/test/resources/alch/14_test_cyclic_inconsistency001.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof StartProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology15() {
        // GIVEN
        var file = new File("src/test/resources/alch/15_test_cyclic_consistency002.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof FailProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology16() {
        // GIVEN
        var file = new File("src/test/resources/alch/16_test_cyclic_inconsistency002.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof StartProofTree<ALCHbLiteral>);
    }

    @Test
    public void testProveOntology17() {
        // GIVEN
        var file = new File("src/test/resources/alch/17_test_cyclic_consistency003.owl");

        // WHEN
        var result = this.prover.prove(file);

        // THEN
        assertTrue(result instanceof FailProofTree<ALCHbLiteral>);
    }
}
