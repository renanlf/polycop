package edu.br.ufpe.cin.sword.cm.propositional.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import edu.br.ufpe.cin.sword.cm.mapper.exceptions.FileParserException;

public class DimacsCNFMatrixMapperTest {

    @Test
    public void testMapDimacsCNFFile() throws IOException, FileParserException {
        var mapper = new DimacsCNFMatrixMapper();
        var matrix = mapper.map(new File("src/test/resources/propositional/test_00001.cnf"));
        assertNotNull(matrix);
        assertEquals(2, matrix.size());
        assertEquals(List.of(-1, 3), matrix.get(0));
        assertEquals(List.of(-2, -3, 1), matrix.get(1));
    }

    @Test
    public void testMapDimacsCNFFileDubois20() throws IOException, FileParserException {
        var mapper = new DimacsCNFMatrixMapper();
        var matrix = mapper.map(new File("src/test/resources/propositional/dubois20.cnf"));
        assertNotNull(matrix);
        assertEquals(160, matrix.size());
    }
}
