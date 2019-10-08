package hessian.migrate;

import org.springframework.web.multipart.MultipartFile;

public class MigrateOptions {
    private  MultipartFile srcBundle = null;
    private  String srcHostname = null;
    private Integer srcPort = null;
    private MultipartFile srcKeystore = null;
    private String srcKeystorePwd = null;
    private MultipartFile srcTruststore = null;
    private String srcTruststorePwd = null;
    private String srcDatacenter = null;
    private String srcUsername = null;
    private String srcPassword = null;
    private String srcKeyspace = null;
    private String srcTable = null;
    private MultipartFile dstBundle = null;
    private String dstHostname = null;
    private Integer dstPort = null;
    private MultipartFile dstKeystore = null;
    private String dstKeystorePwd = null;
    private MultipartFile dstTruststore = null;
    private String dstTruststorePwd = null;
    private String dstDatacenter = null;
    private String dstUsername = null;
    private String dstPassword = null;
    private String dstKeyspace = null;
    private String dstTable = null;
    private String pagingState = null;
    private Integer pageSize = null;
    private Integer numPages = null;

    public MigrateOptions() {
    }

    public MigrateOptions(MultipartFile srcBundle, String srcHostname, Integer srcPort, MultipartFile srcKeystore, String srcKeystorePwd, MultipartFile srcTruststore, String srcTruststorePwd, String srcDatacenter, String srcUsername, String srcPassword, String srcKeyspace, String srcTable, MultipartFile dstBundle, String dstHostname, Integer dstPort, MultipartFile dstKeystore, String dstKeystorePwd, MultipartFile dstTruststore, String dstTruststorePwd, String dstDatacenter, String dstUsername, String dstPassword, String dstKeyspace, String dstTable, String pagingState, Integer pageSize, Integer numPages) {
        this.srcBundle = srcBundle;
        this.srcHostname = srcHostname;
        this.srcPort = srcPort;
        this.srcKeystore = srcKeystore;
        this.srcKeystorePwd = srcKeystorePwd;
        this.srcTruststore = srcTruststore;
        this.srcTruststorePwd = srcTruststorePwd;
        this.srcDatacenter = srcDatacenter;
        this.srcUsername = srcUsername;
        this.srcPassword = srcPassword;
        this.srcKeyspace = srcKeyspace;
        this.srcTable = srcTable;
        this.dstBundle = dstBundle;
        this.dstHostname = dstHostname;
        this.dstPort = dstPort;
        this.dstKeystore = dstKeystore;
        this.dstKeystorePwd = dstKeystorePwd;
        this.dstTruststore = dstTruststore;
        this.dstTruststorePwd = dstTruststorePwd;
        this.dstDatacenter = dstDatacenter;
        this.dstUsername = dstUsername;
        this.dstPassword = dstPassword;
        this.dstKeyspace = dstKeyspace;
        this.dstTable = dstTable;
        this.pagingState = pagingState;
        this.pageSize = pageSize;
        this.numPages = numPages;
    }

    public DseSessionOptions srcDseSessionOptions() {
        DseSessionOptions srcDseSessionOptions = new DseSessionOptions();
        srcDseSessionOptions.setCloudSecureConnectBundle(srcBundle);
        srcDseSessionOptions.setHostname(srcHostname);
        srcDseSessionOptions.setPort(srcPort);
        srcDseSessionOptions.setKeystore(srcKeystore);
        srcDseSessionOptions.setKeystorePassword(srcKeystorePwd);
        srcDseSessionOptions.setTruststore(srcTruststore);
        srcDseSessionOptions.setTruststorePassword(srcTruststorePwd);
        srcDseSessionOptions.setDatacenter(srcDatacenter);
        srcDseSessionOptions.setUsername(srcUsername);
        srcDseSessionOptions.setPassword(srcPassword);
        return srcDseSessionOptions;
    }

    public DseSessionOptions dstDseSessionOptions() {
        DseSessionOptions dstDseSessionOptions = new DseSessionOptions();
        dstDseSessionOptions.setCloudSecureConnectBundle(dstBundle);
        dstDseSessionOptions.setHostname(dstHostname);
        dstDseSessionOptions.setPort(dstPort);
        dstDseSessionOptions.setKeystore(dstKeystore);
        dstDseSessionOptions.setKeystorePassword(dstKeystorePwd);
        dstDseSessionOptions.setTruststore(dstTruststore);
        dstDseSessionOptions.setTruststorePassword(dstTruststorePwd);
        dstDseSessionOptions.setDatacenter(dstDatacenter);
        dstDseSessionOptions.setUsername(dstUsername);
        dstDseSessionOptions.setPassword(dstPassword);
        return dstDseSessionOptions;
    }


    public MultipartFile getSrcBundle() {
        return srcBundle;
    }

    public void setSrcBundle(MultipartFile srcBundle) {
        this.srcBundle = srcBundle;
    }

    public String getSrcHostname() {
        return srcHostname;
    }

    public void setSrcHostname(String srcHostname) {
        this.srcHostname = srcHostname;
    }

    public Integer getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(Integer srcPort) {
        this.srcPort = srcPort;
    }

    public MultipartFile getSrcKeystore() {
        return srcKeystore;
    }

    public void setSrcKeystore(MultipartFile srcKeystore) {
        this.srcKeystore = srcKeystore;
    }

    public String getSrcKeystorePwd() {
        return srcKeystorePwd;
    }

    public void setSrcKeystorePwd(String srcKeystorePwd) {
        this.srcKeystorePwd = srcKeystorePwd;
    }

    public MultipartFile getSrcTruststore() {
        return srcTruststore;
    }

    public void setSrcTruststore(MultipartFile srcTruststore) {
        this.srcTruststore = srcTruststore;
    }

    public String getSrcTruststorePwd() {
        return srcTruststorePwd;
    }

    public void setSrcTruststorePwd(String srcTruststorePwd) {
        this.srcTruststorePwd = srcTruststorePwd;
    }

    public String getSrcDatacenter() {
        return srcDatacenter;
    }

    public void setSrcDatacenter(String srcDatacenter) {
        this.srcDatacenter = srcDatacenter;
    }

    public String getSrcUsername() {
        return srcUsername;
    }

    public void setSrcUsername(String srcUsername) {
        this.srcUsername = srcUsername;
    }

    public String getSrcPassword() {
        return srcPassword;
    }

    public void setSrcPassword(String srcPassword) {
        this.srcPassword = srcPassword;
    }

    public String getSrcKeyspace() {
        return srcKeyspace;
    }

    public void setSrcKeyspace(String srcKeyspace) {
        this.srcKeyspace = srcKeyspace;
    }

    public String getSrcTable() {
        return srcTable;
    }

    public void setSrcTable(String srcTable) {
        this.srcTable = srcTable;
    }

    public MultipartFile getDstBundle() {
        return dstBundle;
    }

    public void setDstBundle(MultipartFile dstBundle) {
        this.dstBundle = dstBundle;
    }

    public String getDstHostname() {
        return dstHostname;
    }

    public void setDstHostname(String dstHostname) {
        this.dstHostname = dstHostname;
    }

    public Integer getDstPort() {
        return dstPort;
    }

    public void setDstPort(Integer dstPort) {
        this.dstPort = dstPort;
    }

    public MultipartFile getDstKeystore() {
        return dstKeystore;
    }

    public void setDstKeystore(MultipartFile dstKeystore) {
        this.dstKeystore = dstKeystore;
    }

    public String getDstKeystorePwd() {
        return dstKeystorePwd;
    }

    public void setDstKeystorePwd(String dstKeystorePwd) {
        this.dstKeystorePwd = dstKeystorePwd;
    }

    public MultipartFile getDstTruststore() {
        return dstTruststore;
    }

    public void setDstTruststore(MultipartFile dstTruststore) {
        this.dstTruststore = dstTruststore;
    }

    public String getDstTruststorePwd() {
        return dstTruststorePwd;
    }

    public void setDstTruststorePwd(String dstTruststorePwd) {
        this.dstTruststorePwd = dstTruststorePwd;
    }

    public String getDstDatacenter() {
        return dstDatacenter;
    }

    public void setDstDatacenter(String dstDatacenter) {
        this.dstDatacenter = dstDatacenter;
    }

    public String getDstUsername() {
        return dstUsername;
    }

    public void setDstUsername(String dstUsername) {
        this.dstUsername = dstUsername;
    }

    public String getDstPassword() {
        return dstPassword;
    }

    public void setDstPassword(String dstPassword) {
        this.dstPassword = dstPassword;
    }

    public String getDstKeyspace() {
        return dstKeyspace;
    }

    public void setDstKeyspace(String dstKeyspace) {
        this.dstKeyspace = dstKeyspace;
    }

    public String getDstTable() {
        return dstTable;
    }

    public void setDstTable(String dstTable) {
        this.dstTable = dstTable;
    }

    public String getPagingState() {
        return pagingState;
    }

    public void setPagingState(String pagingState) {
        this.pagingState = pagingState;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getNumPages() {
        return numPages;
    }

    public void setNumPages(Integer numPages) {
        this.numPages = numPages;
    }
}