package hessian.migrate;

import com.datastax.dse.driver.api.core.DseSession;
import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.core.cql.reactive.ReactiveRow;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.metadata.schema.ColumnMetadata;
import com.datastax.oss.driver.api.core.metadata.schema.KeyspaceMetadata;
import com.datastax.oss.driver.api.core.metadata.schema.TableMetadata;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@RestController
public class MigrateController {
    private int some = 5;

    @RequestMapping("/migrate")
    @ResponseBody
    public String index() {
        return webPage(new MigrateOptions());
    }

    public String ifNotNull(Object str) {
        return (null != str) ? "value=\"" + str.toString() + "\"" : "";
    }

    public String ifNotEmpty(MultipartFile file) {
        if (null == file)
            return "";
        return (file.isEmpty()) ? "" : " required";
    }

    public String webPage(MigrateOptions options) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html>\n");
        sb.append("  <head>\n" +
                "      <meta charset=\"utf-8\">\n" +
                "      <title>Migrate It</title>\n" +
                "    </head>");
        sb.append("<body>\n" +
                "<hr>");

        sb.append("  <h4>Migrate</h4>\n");
        sb.append("  <form action=\"/migrate\" method=\"post\" enctype=\"multipart/form-data\">\n");

        sb.append("    <fieldset>\n");
        sb.append("    <legend>Source Cluster Information</legend>\n");
        sb.append("    <table>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"srcBundle\">Source Cloud Secure Connect Bundle:</label></td>");
        sb.append("      <td><input type=\"file\" id=\"srcBundle\" name=\"srcBundle\"" + ifNotEmpty(options.getSrcBundle()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"srcKeyspace\">Source Keyspace:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"srcKeyspace\" name=\"srcKeyspace\"" + ifNotNull(options.getSrcKeyspace()) + " required></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"srcTable\">Source Table:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"srcTable\" name=\"srcTable\"" + ifNotNull(options.getSrcTable()) + " required></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"srcKeystorePwd\">Source Keystore Password:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"srcKeystorePwd\" name=\"srcKeystorePwd\""  + ifNotNull(options.getSrcKeystorePwd()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"srcKeystore\">Source Keystore:</label></td>");
        sb.append("      <td><input type=\"file\" id=\"srcKeystore\" name=\"srcKeystore\"" + ifNotEmpty(options.getSrcKeystore()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"srcTruststorePwd\">Source Truststore Password:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"srcTruststorePwd\" name=\"srcTruststorePwd\""  + ifNotNull(options.getSrcTruststorePwd()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"srcTruststore\">Source Truststore:</label></td>");
        sb.append("      <td><input type=\"file\" id=\"srcTruststore\" name=\"srcTruststore\"" + ifNotEmpty(options.getSrcTruststore()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"srcHostname\">Source Hosts (comma-separated):</label></td>");
        sb.append("      <td><input type=\"text\" id=\"srcHostname\" name=\"srcHostname\"" + ifNotNull(options.getSrcHostname()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"srcPort\">Source Port:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"srcPort\" name=\"srcPort\"" + ifNotNull(options.getSrcPort()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"srcDatacenter\">Source Data Center:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"srcDatacenter\" name=\"srcDatacenter\"" + ifNotNull(options.getSrcDatacenter()) + " required></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"srcUsername\">Source Username:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"srcUsername\" name=\"srcUsername\"" + ifNotNull(options.getSrcUsername()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"srcPassword\">Source Password:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"srcPassword\" name=\"srcPassword\"" + ifNotNull(options.getSrcPassword()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    </table>\n");
        sb.append("    </fieldset>\n");

        sb.append("    <fieldset>\n");
        sb.append("    <legend>Destination Cluster Information</legend>\n");
        sb.append("    <table>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"dstBundle\">Destination Cloud Secure Connect Bundle:</label></td>");
        sb.append("      <td><input type=\"file\" id=\"dstBundle\" name=\"dstBundle\"" + ifNotEmpty(options.getDstBundle()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"dstKeyspace\">Destination Keyspace:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"dstKeyspace\" name=\"dstKeyspace\"" + ifNotNull(options.getDstKeyspace()) + " required></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"dstTable\">Destination Table:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"dstTable\" name=\"dstTable\"" + ifNotNull(options.getDstTable()) + " required></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"dstKeystorePwd\">Destination Keystore Password:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"dstKeystorePwd\" name=\"dstKeystorePwd\"" + ifNotNull(options.getDstKeystorePwd()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"dstKeystore\">Destination Keystore:</label></td>");
        sb.append("      <td><input type=\"file\" id=\"dstKeystore\" name=\"dstKeystore\"" + ifNotEmpty(options.getDstKeystore()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"dstTruststorePwd\">Destination Truststore Password:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"dstTruststorePwd\" name=\"dstTruststorePwd\"" + ifNotNull(options.getDstTruststorePwd()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"dstTruststore\">Destination Truststore:</label></td>");
        sb.append("      <td><input type=\"file\" id=\"dstTruststore\" name=\"dstTruststore\"" + ifNotEmpty(options.getDstTruststore()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"dstHostname\">Destination Hosts (comma-separated):</label></td>");
        sb.append("      <td><input type=\"text\" id=\"dstHostname\" name=\"dstHostname\"" + ifNotNull(options.getDstHostname()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"dstPort\">Destination Port:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"dstPort\" name=\"dstPort\"" + ifNotNull(options.getDstPort()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"dstDatacenter\">Destination Data Center:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"dstDatacenter\" name=\"dstDatacenter\"" + ifNotNull(options.getDstDatacenter()) + " required></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"dstUsername\">Destination Username:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"dstUsername\" name=\"dstUsername\"" + ifNotNull(options.getDstUsername()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"dstPassword\">Destination Password:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"dstPassword\" name=\"dstPassword\"" + ifNotNull(options.getDstPassword()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    </table>\n");
        sb.append("    </fieldset>\n");

        sb.append("    <fieldset>\n");
        sb.append("    <legend>Configuration</legend>\n");
        sb.append("    <table>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"pageSize\">Number of rows/page:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"pageSize\" name=\"pageSize\"" + ifNotNull(options.getPageSize()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"numPages\">Number of Pages:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"numPages\" name=\"numPages\"" + ifNotNull(options.getNumPages()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    <tr>\n");
        sb.append("      <td><label for=\"numPages\">Paging State:</label></td>");
        sb.append("      <td><input type=\"text\" id=\"pagingState\" name=\"pagingState\"" + ifNotNull(options.getPagingState()) + "></td>");
        sb.append("    </tr>\n");
        sb.append("    </table>\n");
        sb.append("    </fieldset>\n");

        sb.append("    <div class=\"button\"><button type=\"submit\">Migrate It!</button></div>\n");
        sb.append("  </form>\n");

        sb.append("</body>\n</html>\n");

        return sb.toString();
    }

    @RequestMapping(value = "/migrate", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public String handleFileUpload(@RequestParam(required = false) MultipartFile srcBundle,
                                   @RequestParam(required = false) String srcHostname,
                                   @RequestParam(required = false) Integer srcPort,
                                   @RequestParam(required = false) MultipartFile srcKeystore,
                                   @RequestParam(required = false) String srcKeystorePwd,
                                   @RequestParam(required = false) MultipartFile srcTruststore,
                                   @RequestParam(required = false) String srcTruststorePwd,
                                   @RequestParam(required = true)  String srcDatacenter,
                                   @RequestParam(required = false) String srcUsername,
                                   @RequestParam(required = false) String srcPassword,
                                   @RequestParam(required = true)  String srcKeyspace,
                                   @RequestParam(required = true)  String srcTable,
                                   @RequestParam(required = false) MultipartFile dstBundle,
                                   @RequestParam(required = false) String dstHostname,
                                   @RequestParam(required = false) Integer dstPort,
                                   @RequestParam(required = false) MultipartFile dstKeystore,
                                   @RequestParam(required = false) String dstKeystorePwd,
                                   @RequestParam(required = false) MultipartFile dstTruststore,
                                   @RequestParam(required = false) String dstTruststorePwd,
                                   @RequestParam(required = true)  String dstDatacenter,
                                   @RequestParam(required = false) String dstUsername,
                                   @RequestParam(required = false) String dstPassword,
                                   @RequestParam(required = true)  String dstKeyspace,
                                   @RequestParam(required = true)  String dstTable,
                                   @RequestParam(required = false) String pagingState,
                                   @RequestParam(required = false) Integer pageSize,
                                   @RequestParam(required = false) Integer numPages
    ) {
        MigrateOptions options = new MigrateOptions(
                srcBundle, srcHostname, srcPort, srcKeystore, srcKeystorePwd, srcTruststore, srcTruststorePwd,
                srcDatacenter, srcUsername, srcPassword, srcKeyspace, srcTable,
                dstBundle, dstHostname, dstPort, dstKeystore, dstKeystorePwd, dstTruststore, dstTruststorePwd,
                dstDatacenter, dstUsername, dstPassword, dstKeyspace, dstTable,
                pagingState, pageSize, numPages);
        DseSession srcSession = options.srcDseSessionOptions().builder().build();
        DseSession dstSession = options.dstDseSessionOptions().builder().build();
        if (null != options.getNumPages())
            some = options.getNumPages();
        else
            options.setNumPages(some);
        ByteBuffer pagingStateBytes = null;
        if ((null != options.getPagingState()) && (!options.getPagingState().isEmpty()))
            pagingStateBytes = ByteBuffer.wrap(Base64.getDecoder().decode(options.getPagingState()));

        // Check Src and Dst tables
        checkTable(srcSession, options.getSrcKeyspace(), options.getSrcTable(),
                dstSession, options.getDstKeyspace(), options.getDstTable());

        // Prepare Select statement
        Select select = QueryBuilder.selectFrom(options.getSrcKeyspace(), options.getSrcTable()).all();
        PreparedStatement srcPs = srcSession.prepare(select.build());

        // Prepare Insert statement
        ColumnDefinitions cdefs = srcPs.getResultSetDefinitions();
        RegularInsert insert = QueryBuilder.insertInto(options.getDstKeyspace(), options.getDstTable())
                .value(cdefs.get(0).getName(), QueryBuilder.bindMarker(cdefs.get(0).getName()));
        for (int i = 1; i < cdefs.size(); i++) {
            insert = insert.value(cdefs.get(i).getName(), QueryBuilder.bindMarker(cdefs.get(i).getName()));
        }
        PreparedStatement dstPs = dstSession.prepare(insert.build());

        ByteBuffer newPagingState = executeSome(srcSession, srcPs, dstSession, dstPs, options.getPageSize(), some, pagingStateBytes);
        if (null != newPagingState) {
            options.setPagingState(Base64.getEncoder().encodeToString(newPagingState.array()));
            return webPage(options);
        }
        return "Done!";
    }

    private void submitForNext(DseSessionOptions srcDseSessionOptions, DseSessionOptions dstDseSessionOptions,
                               int pageSize, int numPages, ByteBuffer newPagingState) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<String,Object>();
        body.addAll(srcDseSessionOptions.asMap("src"));
        body.addAll(dstDseSessionOptions.asMap("dst"));
        body.add("pageSize", pageSize);
        body.add("numPages", numPages);
        body.add("pagingState", Base64.getEncoder().encodeToString(newPagingState.array()));

        RedirectView redirect = new RedirectView("/model");
        redirect.setContextRelative(true);
        redirect.setPropagateQueryParams(true);
        redirect.addStaticAttribute("pagingState", Base64.getEncoder().encodeToString(newPagingState.array()));
        //return redirect;
        /*
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String serverUrl = "migrate/";
        WebClient webClient = WebClient.create();
        WebClient.RequestHeadersSpec requestSpec = webClient.post().uri(serverUrl).body(BodyInserters.fromMultipartData(body));
        requestSpec.retrieve().bodyToMono(String.class).block();
        System.err.println("Hiya");
        */
    }

    private ByteBuffer executeSome(DseSession srcSession, PreparedStatement srcPs,
                                   DseSession dstSession, PreparedStatement dstPs,
                                   Integer pageSize, int some, ByteBuffer pagingState) {
        BoundStatement srcBs = srcPs.bind();
        if (null != pageSize)
            srcBs = srcBs.setPageSize(pageSize);
        if (null != pagingState)
            srcBs = srcBs.setPagingState(pagingState);
        ResultSet rs = srcSession.execute(srcBs);
        Flux<ReactiveRow> flux = null;
        for (Row r : rs) {
            BoundStatement dstBs = dstPs.bind();
            for (ColumnDefinition cdef : r.getColumnDefinitions())
                dstBs = dstBs.set(cdef.getName(),
                        r.get(cdef.getName(), r.codecRegistry().codecFor(cdef.getType())),
                        dstBs.codecRegistry().codecFor(cdef.getType()));
            ReactiveResultSet rrs = dstSession.executeReactive(dstBs);
            if (null == flux)
                flux = Flux.from(rrs);
            else
                flux = flux.concatWith(rrs);

            if (0 == rs.getAvailableWithoutFetching()) {
                some--;
                if (0 >= some) {
                    // All done with this request
                    if (null != flux)
                        flux.blockLast();
                    if (rs.isFullyFetched())
                        return null;
                    else
                        return rs.getExecutionInfo().getPagingState();
                }
            }
        }
        if (null != flux)
            flux.blockLast();
        return null;
    }

    private void checkTable(DseSession srcSession, String srcKeyspace, String srcTable,
                           DseSession dstSession, String dstKeyspace, String dstTable) {
        Optional<KeyspaceMetadata> srcKmo = srcSession.getMetadata().getKeyspace(srcKeyspace);
        if (!srcKmo.isPresent())
            throw new IllegalArgumentException("Could not find keyspace on source cluster: " + srcKeyspace);
        KeyspaceMetadata srcKm = srcKmo.get();
        Optional<TableMetadata> srcTmo = srcKm.getTable(srcTable);
        if (!srcTmo.isPresent())
            throw new IllegalArgumentException("Could not find table on source cluster: " + srcKeyspace + "." + srcTable);
        TableMetadata srcTm = srcTmo.get();

        Optional<KeyspaceMetadata> dstKmo = dstSession.getMetadata().getKeyspace(dstKeyspace);
        if (!dstKmo.isPresent())
            throw new IllegalArgumentException("Could not find keyspace on destination cluster: " + dstKeyspace);
        KeyspaceMetadata dstKm = dstKmo.get();
        Optional<TableMetadata> dstTmo = dstKm.getTable(dstTable);
        if (!dstTmo.isPresent())
            throw new IllegalArgumentException("Could not find table on destination cluster: " + dstKeyspace + "." + dstTable);
        TableMetadata dstTm = dstTmo.get();

        for (Map.Entry<CqlIdentifier, ColumnMetadata> cmeta : srcTm.getColumns().entrySet()) {
            if (!dstTm.getColumn(cmeta.getKey()).isPresent()) {
                throw new IllegalStateException("Columnn " + cmeta.getKey().asInternal() + " not found in destination table");
            }
            if (!dstTm.getColumn(cmeta.getKey()).get().getType().equals(cmeta.getValue().getType())) {
                throw new IllegalStateException("Columnn " + cmeta.getKey().asInternal() + " has different type in destination table"
                        + "(" + cmeta.getValue().getType().asCql(true, true) + " vs "
                        + dstTm.getColumn(cmeta.getKey()).get().getType().asCql(true, true) + ")");
            }
        }
    }
}
