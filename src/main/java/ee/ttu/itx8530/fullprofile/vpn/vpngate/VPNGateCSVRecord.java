package ee.ttu.itx8530.fullprofile.vpn.vpngate;

import java.util.Base64;

import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

/**
 * 
 * Encapsulating class for CSV record from VPNGate API.
 * 
 * @author Taavi T.
 *
 */
public class VPNGateCSVRecord {

    private Logger logger = Logger.getLogger(getClass());

    private final String hostName, IP, config, countryLong;
    private final long score;
    private final int speed;

    private final CSVRecord csvRecord;

    // #HostName,IP,Score,Ping,Speed,CountryLong,CountryShort,NumVpnSessions,Uptime,TotalUsers,TotalTraffic,LogType,Operator,Message,OpenVPN_ConfigData_Base64

    /**
     * Extracts necessary data according to API's CSV headers.
     * 
     * @param csvRecord
     */
    public VPNGateCSVRecord(CSVRecord csvRecord) throws IllegalArgumentException {
        this.csvRecord = csvRecord;
        if (csvRecord.isConsistent()) {
            hostName = csvRecord.get("#HostName");
            IP = csvRecord.get("IP");
            score = Long.parseLong(csvRecord.get("Score"));
            speed = Integer.parseInt(csvRecord.get("Speed"));
            config = new String(Base64.getDecoder().decode(csvRecord.get("OpenVPN_ConfigData_Base64")));
            countryLong = csvRecord.get("CountryLong");
        } else {
            throw new IllegalArgumentException("CSVRecord is incosistent");
        }
    }

    public String get(String columnName) {
        return csvRecord.get(columnName);
    }

    public String getHostName() {
        return hostName;
    }

    public String getIP() {
        return IP;
    }

    public String getConfig() {
        return config;
    }

    public long getScore() {
        return score;
    }

    public int getSpeed() {
        return speed;
    }

    public String getCountryLong() {
        return countryLong;
    }

}
