package edu.br.ufpe.cin.sword.cm.propositional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import edu.br.ufpe.cin.sword.cm.mapper.exceptions.FileParserException;

public class PropositionalConnectionProverTest {

    @Test
    public void testFile00001() throws IOException, FileParserException {
        File file = new File("src/test/resources/propositional/test_00001.cnf");
        PropositionalConnectionProverDecorator prover = new PropositionalConnectionProverDecorator();

        assertFalse(prover.unsat(file));
    }

    @Test
    public void testFile00002() throws IOException, FileParserException {
        File file = new File("src/test/resources/propositional/test_00002.cnf");
        PropositionalConnectionProverDecorator prover = new PropositionalConnectionProverDecorator();

        assertTrue(prover.unsat(file));
    }

    @Test
    public void testFile00003() throws IOException, FileParserException {
        File file = new File("src/test/resources/propositional/test_00003.cnf");
        PropositionalConnectionProverDecorator prover = new PropositionalConnectionProverDecorator();

        assertTrue(prover.unsat(file));
    }

    @Test
    public void testFileQuinn() throws IOException, FileParserException {
        File file = new File("src/test/resources/propositional/quinn.cnf");
        PropositionalConnectionProverDecorator prover = new PropositionalConnectionProverDecorator();

        assertFalse(prover.unsat(file));
    }

}
