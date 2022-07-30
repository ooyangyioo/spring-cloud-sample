package org.yangyi.project.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.yangyi.project.oauth.config.KeyStoreConfig;

@SpringBootApplication
@EnableConfigurationProperties({
        KeyStoreConfig.class
})
public class AuthServerMain {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerMain.class, args);
    }
}
