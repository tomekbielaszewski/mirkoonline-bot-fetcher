package org.grizz.model.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "keeper")
public class KeeperProperties {
    private String url;
    private String username;
    private String password;
}
