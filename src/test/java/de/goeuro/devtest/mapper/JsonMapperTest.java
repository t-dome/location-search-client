package de.goeuro.devtest.mapper;

import de.goeuro.devtest.exception.ApplicationException;
import de.goeuro.devtest.model.Results;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the behaviour of JsonMapper.
 * @author Rolf Schuster
 */
@Test
public class JsonMapperTest {
    private static final String RESULT_OK = "{\n" +
            "  \"results\" : [ {\n" +
            "    \"_type\" : \"Position\",\n" +
            "    \"_id\" : 410978,\n" +
            "    \"name\" : \"Potsdam, USA\",\n" +
            "    \"type\" : \"location\",\n" +
            "    \"geo_position\" : {\n" +
            "        \"latitude\" : 44.66978,\n" +
            "        \"longitude\" : -74.98131\n" +
            "      }\n" +
            "  }, {\n" +
            "    \"_type\" : \"Position\",\n" +
            "    \"_id\" : 377078,\n" +
            "    \"name\" : \"Potsdam, Deutschland\",\n" +
            "    \"type\" : \"location\",\n" +
            "    \"geo_position\" : {\n" +
            "        \"latitude\" : 52.39886,\n" +
            "        \"longitude\" : 13.06566\n" +
            "      }" +
            "   } ]\n" +
            "}";

    private static final String RESULT_EMPTY = "{ \"results\" : []}";
    private static final String RESULT_NULL = "{ \"results\" : null}";
    private static final String RESULT_WRONG_NAME = "{ \"ufo\" : \"\"}";
    private static final String RESULT_INVALID_JSON = "{ \"results\" }";

    private JsonMapper target;

    @BeforeMethod
    public void setup() {
        target = new JsonMapper();
    }


    /**
     * Happy flow.
     *
     * @throws ApplicationException
     */
    public void fromJsonOk() throws ApplicationException {
        // execute
        Results result = target.fromJson(RESULT_OK);

        // verify
        assertNotNull(result);
        assertNotNull(result.getResults());
        assertEquals(result.getResults().size(), 2);

        assertEquals(result.getResults().get(0).getId(), "410978");
        assertNotNull(result.getResults().get(0).getGeoPosition());
        assertEquals(result.getResults().get(0).getGeoPosition().getLongitude(), "-74.98131");

        assertEquals(result.getResults().get(1).getId(), "377078");
        assertNotNull(result.getResults().get(1).getGeoPosition());
        assertEquals(result.getResults().get(1).getGeoPosition().getLatitude(), "52.39886");
    }


    public void fromJsonEmptyResult() throws ApplicationException {
        // execute
        Results result = target.fromJson(RESULT_EMPTY);

        // verify
        assertEmpty(result);
    }

    public void fromJsonNullResult() throws ApplicationException {
        // execute
        Results result = target.fromJson(RESULT_NULL);

        // verify
        assertEmpty(result);
    }

    public void fromJsonWrongResultName() throws ApplicationException {
        // execute
        Results result = target.fromJson(RESULT_WRONG_NAME);
        assertNotNull(result);
        assertNull(result.getResults());
    }

    public void fromJsonError() throws ApplicationException {
        try {
            // execute
            target.fromJson(RESULT_INVALID_JSON);
            fail("Expected an exception.");
        } catch (ApplicationException e) {
            assertEquals(e.getCode(), ApplicationException.ErrorCode.JSON_PARSING_ERROR);
            assertEquals(e.getDetailMessage(), RESULT_INVALID_JSON);
            assertNotNull(e.getCause());
        }
    }

    private void assertEmpty(Results result) {
        assertTrue(result == null || result.getResults() == null || result.getResults().isEmpty());
    }

}
