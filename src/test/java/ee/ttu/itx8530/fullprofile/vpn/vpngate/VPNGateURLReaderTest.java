package ee.ttu.itx8530.fullprofile.vpn.vpngate;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VPNGateURLReaderTest {

    @Test
    public void testGetTextLength() {
        String result = VPNGateURLReader.getText();
        assertTrue(result.length() > 0);
        assertTrue(result.split("\n").length > 0);
    }

}
