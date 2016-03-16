package ee.ttu.itx8530.fullprofile.vpn.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

/**
 * 
 * Some properties have default values.
 * 
 * @author Kasutaja
 *
 */
public class OpenVpnConfig {

    private String content;
    private Charset charset = Charset.forName("UTF-8");

    /** tun (layer-3), tap (layer-2) */
    private String connectionType = "";
    /** tcp/udp */
    private String protocol = "tcp";

    /** remote <HOSTNAME> <PORT> hostname / IP address */
    private String remoteHost;
    private int remotePort;

    /*
     * cipher: [NULL-CIPHER] NULL AES-128-CBC AES-192-CBC AES-256-CBC BF-CBC
     * CAST-CBC CAST5-CBC DES-CBC DES-EDE-CBC DES-EDE3-CBC DESX-CBC RC2-40-CBC
     * RC2-64-CBC RC2-CBC auth: SHA SHA1 MD5 MD4 RMD160
     */
    private String encryptionCipher = "AES-128-CBC";
    private String authDigest = "SHA1";

    private String timesToTryResolvingHost = "5";

    public OpenVpnConfig(String confContent) {
        this.content = confContent;
        initialize();
    }

    public OpenVpnConfig(File conf) throws FileNotFoundException, IOException {
        this.content = "";
        try (FileInputStream fis = new FileInputStream(conf)) {
            this.content = IOUtils.toString(fis);
        }
        initialize();
    }

    private void initialize() {
        // regex hostname and hostport (?<=remote\s)([^\s]*)(?:\s)(\d+)
        Pattern pattern = Pattern.compile("(?<=remote\\s)(?<host>[^\\s]*)(?:\\s)(?<port>\\d+)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            remoteHost = matcher.group("host");
            remotePort = Integer.parseInt(matcher.group("port"));
        }
        pattern = Pattern.compile("(?<=proto\\s)(?<protocol>tcp|udp)");
        if (matcher.find()) {
            protocol = matcher.group("protocol");
        }

    }

    public String getConnectionType() {
        return connectionType;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getEncryptionCipher() {
        return encryptionCipher;
    }

    public void setEncryptionCipher(String encryptionCipher) {
        // TODO replace in content
        this.encryptionCipher = encryptionCipher;
    }

    public String getAuthDigest() {
        return authDigest;
    }

    public void setAuthDigest(String authDigest) {
        // TODO replace in content
        this.authDigest = authDigest;
    }

    public String getTimesToTryResolvingHost() {
        return timesToTryResolvingHost;
    }

    public void setTimesToTryResolvingHost(String timesToTryResolvingHost) {
        // TODO replace in content
        this.timesToTryResolvingHost = timesToTryResolvingHost;
    }

    String getContent() {
        return content;
    }

    public Charset getCharset() {
        return charset;
    }

}
