package ee.ttu.itx8530.fullprofile.vpn.vpngate;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

public class VPNGateCSVData {

    private Logger logger = Logger.getLogger(getClass());

    /** API seems not to be escaping quotes */
    private static CSVFormat format = CSVFormat.DEFAULT.withQuote(null).withCommentMarker('*').withHeader();

    public List<VPNGateCSVRecord> fetch() {
        List<CSVRecord> data = new ArrayList<>();
        List<VPNGateCSVRecord> records = new ArrayList<>();
        String text = VPNGateURLReader.getText();
        try {
            data = format.parse(new StringReader(text)).getRecords();
        } catch (IOException e) {
            logger.error("Error when parsing VPNGate CSV data", e);
        }
        data.stream().forEach(d -> records.add(new VPNGateCSVRecord(d)));
        return records;
    }


}
