package de.goeuro.devtest.mapper;

import de.goeuro.devtest.exception.ApplicationException;
import de.goeuro.devtest.model.Result;
import de.goeuro.devtest.model.Results;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Rolf Schuster
 */
public class CsvWriter {
    private final String[] HEADER = {"_type", "_id", "name", "type", "latitude", "longitude"};

    public void writeCsv(Results input, Writer destination) throws ApplicationException {
        if (input != null) {
            try (ICsvListWriter csvListWriter = new CsvListWriter(destination, CsvPreference.STANDARD_PREFERENCE)) {
                csvListWriter.writeHeader(HEADER);
                if (input.getResults() != null && !input.getResults().isEmpty()) {
                    for (Result result : input.getResults()) {
                        List<String> row = new LinkedList<>();
                        row.add(result.getTypeFriendlyName());
                        row.add(result.getId());
                        row.add(result.getName());
                        row.add(result.getType());
                        if (result.getGeoPosition() != null) {
                            row.add(result.getGeoPosition().getLatitude());
                            row.add(result.getGeoPosition().getLongitude());
                        }

                        csvListWriter.write(row);
                    }
                }
                // no need to close the writer "destination";
                // csvListWriter.close(), which is called automatically, does that
            } catch (IOException e) {
                throw new ApplicationException(ApplicationException.ErrorCode.INTERNAL_ERROR,
                        "Error writing outout CSV: " + e.getMessage(), e);
            }

        }
    }
}
