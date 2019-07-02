package io.github.anoobizzz.mintostask.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfiguration {
    @Bean
    public RestTemplate client() {
        return new RestTemplate();
    }
}
