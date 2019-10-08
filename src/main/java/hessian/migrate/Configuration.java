package hessian.migrate;

import com.datastax.dse.driver.api.core.DseSession;
import com.datastax.dse.driver.api.core.DseSessionBuilder;

public class Configuration {
    public static DseSession dseSession(DseSessionOptions options) {
        DseSessionBuilder builder = DseSession.builder();
        options.configure(builder);
        return builder.build();
    }
}
