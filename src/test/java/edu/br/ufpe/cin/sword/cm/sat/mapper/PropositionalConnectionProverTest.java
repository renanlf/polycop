package edu.br.ufpe.cin.sword.cm.sat.mapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import edu.br.ufpe.cin.sword.cm.mapper.exceptions.FileParserException;
import edu.br.ufpe.cin.sword.cm.propositional.PropositionalConnectionProver;

public class PropositionalConnectionProverTest {

    @Test
    public void testFile00001() throws IOException, FileParserException {
        File file = new File("src/test/resources/sat/test_00001.cnf");
        PropositionalConnectionProver prover = new PropositionalConnectionProver();

        assertFalse(prover.unsat(file));
    }

    @Test
    public void testFile00002() throws IOException, FileParserException {
        File file = new File("src/test/resources/sat/test_00002.cnf");
        PropositionalConnectionProver prover = new PropositionalConnectionProver();

        assertTrue(prover.unsat(file));
    }

    @Test
    public void testFile00003() throws IOException, FileParserException {
        File file = new File("src/test/resources/sat/test_00003.cnf");
        PropositionalConnectionProver prover = new PropositionalConnectionProver();

        assertTrue(prover.unsat(file));
    }



    @Test
    public void testFileDubois20() throws IOException, FileParserException {
        File file = new File("src/test/resources/sat/dubois20.cnf");
        PropositionalConnectionProver prover = new PropositionalConnectionProver();

        assertTrue(prover.unsat(file));
    }

}
