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

    @Test
    public void testMapOntology10() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/10_test_skolem_inconsistency.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology11() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/11_test_skolem_consistency.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology12() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/12_test_path_instantiation_consistency.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology13() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/13_test_cyclic_consistency001.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology14() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/14_test_cyclic_inconsistency001.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology15() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/15_test_cyclic_consistency002.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology16() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/16_test_cyclic_inconsistency002.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology17() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/17_test_cyclic_consistency003.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology18() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/18_test_cyclic_inconsistency003.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology19() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/19_test_cyclic_consistency004.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology20() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/20_test_cyclic_consistency007.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology21() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/21_test_cyclic_consistency008.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology22() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/22_test_cyclic_pure.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology23() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/23_test_cyclic_consistency005_slow.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }

    @Test
    public void testMapOntology24() throws FileParserException, IOException {
        // GIVEN
        var file = new File("src/test/resources/alch/24_test_cyclic_consistency006_slow.owl");

        // WHEN
        var matrix = this.mapper.map(file);

        // THEN
        System.out.println(matrix);
    }
}
