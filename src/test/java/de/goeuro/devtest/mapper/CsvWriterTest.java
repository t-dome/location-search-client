package de.goeuro.devtest.mapper;

import de.goeuro.devtest.exception.ApplicationException;
import de.goeuro.devtest.model.GeoPosition;
import de.goeuro.devtest.model.Result;
import de.goeuro.devtest.model.Results;
import static org.testng.Assert.*;

import static org.mockito.Mockito.*;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * @author Rolf Schuster
 */
@Test
public class CsvWriterTest {
    private static final String EOL_SEPARATOR = System.getProperty("line.separator");

    private static final String HEADER = "_type,_id,name,type,latitude,longitude" + EOL_SEPARATOR;
    private static final String TWO_RESULTS = "_type,_id,name,type,latitude,longitude" + EOL_SEPARATOR +
            "type1,id1,name1,type1,latid1,long1" + EOL_SEPARATOR +
            "type2,id2,name2,type2,latid2,long2" + EOL_SEPARATOR;

    private static final String ID1 = "id1";
    private static final String NAME1 = "name1";
    private static final String TYPE1 = "type1";
    private static final String TYPE_FRIENDLY1 = "type1";
    private static final String LATITUDE1 = "latid1";
    private static final String LONGITUDE1 = "long1";

    private static final String ID2 = "id2";
    private static final String NAME2 = "name2";
    private static final String TYPE2 = "type2";
    private static final String TYPE_FRIENDLY2 = "type2";
    private static final String LATITUDE2 = "latid2";
    private static final String LONGITUDE2 = "long2";

    private CsvWriter target;

    public void setup() {
        target = new CsvWriter();
    }

    public void writeCsvOk() throws ApplicationException {
        // setup
        Result r1 = new Result().withId(ID1).withName(NAME1).withType(TYPE1).withTypeFriendlyName(TYPE_FRIENDLY1)
                .withGeoPosition(new GeoPosition().withLatitude(LATITUDE1).withLongitude(LONGITUDE1));
        Result r2 = new Result().withId(ID2).withName(NAME2).withType(TYPE2).withTypeFriendlyName(TYPE_FRIENDLY2)
                .withGeoPosition(new GeoPosition().withLatitude(LATITUDE2).withLongitude(LONGITUDE2));
        Results results = new Results().withResults(Arrays.asList(r1, r2));

        StringWriter sw = new StringWriter();
        // execute
        target.writeCsv(results, sw);

        //verify
        assertEquals(sw.toString(), TWO_RESULTS);
    }

    public void writeCsvEmpty() throws ApplicationException {
        // setup
        Results results = new Results();

        StringWriter sw = new StringWriter();
        // execute
        target.writeCsv(results, sw);

        //verify
        assertEquals(sw.toString(), HEADER);
    }

    public void writeCsvNull() throws ApplicationException {
        StringWriter sw = new StringWriter();
        // execute
        target.writeCsv(null, sw);

        //verify
        assertEquals(sw.toString(), "");
    }

    /**
     * Check that the writer is properly closed.
     *
     * @throws ApplicationException
     */
    public void writeCsvWriterIsClosed() throws ApplicationException, IOException {
        // setup
        Results results = new Results();

        StringWriter sw = spy(new StringWriter());
        // execute
        target.writeCsv(results, sw);

        verify(sw).close();
    }

}
