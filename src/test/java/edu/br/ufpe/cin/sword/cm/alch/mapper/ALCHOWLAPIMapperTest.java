package edu.br.ufpe.cin.sword.cm.alch.mapper;

import edu.br.ufpe.cin.sword.cm.alchb.mapper.ALCHbOWLAPIMapper;
import edu.br.ufpe.cin.sword.cm.mapper.exceptions.FileParserException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ALCHOWLAPIMapperTest {

    private ALCHbOWLAPIMapper mapper;

    @Before
    public void setUp() {
        this.mapper = new ALCHbOWLAPIMapper();
    }

    @Test
    public void testMapOntology01() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/01_test_acyclic_tbox_abox_inconsistency.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology02() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/02_test_acyclic_inconsistency001.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology03() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/03_test_acyclic_tbox_inconsistency001.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology04() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/04_test_acyclic_tbox_inconsistency002.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology05() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/05_test_acyclic_tbox_inconsistency003.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology06() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/06_test_thing_consistency.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology07() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/07_test_thing_inconsistency.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology08() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/08_test_nothing_consistency.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology09() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/09_test_nothing_inconsistency.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }
}
