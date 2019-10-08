package hessian.migrate;

import com.datastax.dse.driver.api.core.DseSession;
import com.datastax.dse.driver.api.core.DseSessionBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;

public class DseSessionOptions {
    private String username = null;
    private String password = null;
    private String truststorePassword = null;
    private String keystorePassword = null;
    private MultipartFile truststore = null;
    private MultipartFile keystore = null;
    private MultipartFile cloudSecureConnectBundle = null;
    private File cloudSecureConnectBundleFile = null;
    private String hostname = null;
    private Integer port = 9042;
    private String datacenter = null;

    public MultiValueMap<String,Object> asMap(String prefix) {
        MultiValueMap<String,Object> map = new LinkedMultiValueMap<String,Object>();
        if (null != username) map.add(prefix + "Username", username);
        if (null != password) map.add(prefix + "Password", password);
        if (null != truststorePassword) map.add(prefix + "TruststorePwd", truststorePassword);
        if (null != keystorePassword) map.add(prefix + "KeystorePwd", keystorePassword);
        if (null != hostname) map.add(prefix + "Hostname", hostname);
        if (null != port) map.add(prefix + "Port", port);
        if (null != datacenter) map.add(prefix + "Datacenter", datacenter);
        if (null != truststore) map.add(prefix + "Truststore", truststore);
        if (null != keystore) map.add(prefix + "Keystore", keystore);
        if (null != cloudSecureConnectBundle) map.add(prefix + "Bundle", cloudSecureConnectBundle);

        return map;
    }

    public void validateOptions() {
        username = (null == username) ? "" : username;
        password = (null == password) ? "" : password;
        truststorePassword = (null == truststorePassword) ? "" : truststorePassword;
        keystorePassword = (null == keystorePassword) ? "" : keystorePassword;
        hostname = (null == hostname) ? "" : hostname;
        datacenter = (null == datacenter) ? "" : datacenter;

        if ((!hostname.isEmpty()) && (null != cloudSecureConnectBundle))
            throw new IllegalArgumentException("Cannot set both host and cloudSecureConnectBundle");
        if ((null != truststore) && (null != cloudSecureConnectBundle))
            throw new IllegalArgumentException("Cannot set both truststore and cloudSecureConnectBundle");
        if ((null != keystore) && (null != cloudSecureConnectBundle))
            throw new IllegalArgumentException("Cannot set both keystore and cloudSecureConnectBundle");

        if ((username.isEmpty()) && (null != cloudSecureConnectBundle))
            throw new IllegalArgumentException("Must specify username with cloudSecureConnectBundle");
        if ((password.isEmpty()) && (null != cloudSecureConnectBundle))
            throw new IllegalArgumentException("Must specify password with cloudSecureConnectBundle");

        if ((hostname.isEmpty()) && (null == cloudSecureConnectBundle))
            throw new IllegalArgumentException("Must specify either hostname or cloudSecureConnectBundle");

        if ((!password.isEmpty()) && (username.isEmpty()))
            throw new IllegalArgumentException("Cannot set password without setting username");
        if ((!truststorePassword.isEmpty()) && (null == truststore))
            throw new IllegalArgumentException("Cannot set truststore password without setting truststore");
        if ((!keystorePassword.isEmpty()) && (null == keystore))
            throw new IllegalArgumentException("Cannot set keystore password without setting keystore");

        if (datacenter.isEmpty())
            throw new IllegalArgumentException("Must specify datacenter");

    }

    public DseSessionBuilder builder() {
        return configure(DseSession.builder());
    }

    public DseSessionBuilder configure(DseSessionBuilder builder) {
        validateOptions();
        if (null != cloudSecureConnectBundle) {
            builder.withCloudSecureConnectBundle(cloudSecureConnectBundleFile.getAbsolutePath());
        }
        else {
            // Keystore/Truststore
            if ((null != keystore) || (null != truststore)) {
                try {
                    builder.withSslContext(getSSLContext());
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            // Contact points
            if (!hostname.isEmpty()) {
                if (null == port)
                    port = 9042;
                for (String host : hostname.split(",")) {
                    builder.addContactPoint(new InetSocketAddress(host, port));
                }
            }
            builder.withLocalDatacenter(datacenter);
        }
        // Credentials
        if ((!username.isEmpty()) && (!password.isEmpty())) {
            builder.withAuthCredentials(username, password);
        }

        return builder;
    }

    private  SSLContext getSSLContext() throws Exception {
        TrustManagerFactory tmf;
        SSLContext context = SSLContext.getInstance("SSL");
        InputStream trustStoreStream = this.truststore.getInputStream();
        try {
            KeyStore ts = KeyStore.getInstance("JKS");
            char[] trustPassword = truststorePassword.toCharArray();
            ts.load(trustStoreStream, trustPassword);
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);
        } finally {
            trustStoreStream.close();
        }

        // initialize keystore.
        KeyManagerFactory kmf;
        InputStream keyStoreStream = this.keystore.getInputStream();
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            char[] keyStorePassword = keystorePassword.toCharArray();
            ks.load(keyStoreStream, keyStorePassword);
            kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, keyStorePassword);
        } finally {
            keyStoreStream.close();
        }

        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        return context;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }

    public void setTruststorePassword(String truststorePassword) {
        this.truststorePassword = truststorePassword;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public MultipartFile getTruststore() {
        return truststore;
    }

    public void setTruststore(MultipartFile truststore) {
        if (!truststore.isEmpty())
            this.truststore = truststore;
    }

    public MultipartFile getKeystore() {
        return keystore;
    }

    public void setKeystore(MultipartFile keystore) {
        if (!keystore.isEmpty())
            this.keystore = keystore;
    }

    public MultipartFile getCloudSecureConnectBundle() {
        return cloudSecureConnectBundle;
    }

    public void setCloudSecureConnectBundle(MultipartFile cloudSecureConnectBundle) {
        if (!cloudSecureConnectBundle.isEmpty()) {
            this.cloudSecureConnectBundle = cloudSecureConnectBundle;
            try {
                this.cloudSecureConnectBundleFile = File.createTempFile("dsecscb", ".zip");//, new File("/tmp"));
                FileOutputStream fos = new FileOutputStream(this.cloudSecureConnectBundleFile);
                fos.write(this.cloudSecureConnectBundle.getBytes());
                fos.close();
            } catch (IOException ioe) {
                throw new RuntimeException("Could not save cloud secure connect bundle to filesystem ("
                        + this.cloudSecureConnectBundleFile.getAbsolutePath() + ")");
            }
        }
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatacenter() {
        return datacenter;
    }

    public void setDatacenter(String datacenter) {
        this.datacenter = datacenter;
    }
}
