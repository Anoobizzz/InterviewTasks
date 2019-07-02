package io.github.anoobizzz.mintostask.persistance;

import io.github.anoobizzz.mintostask.domain.WeatherInfo;
import io.github.anoobizzz.mintostask.domain.WeatherRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.concurrent.CompletableFuture.runAsync;

@Component
public class RequestRepositoryManager {
    private static final Logger LOG = LoggerFactory.getLogger(RequestRepositoryManager.class);
    private final RequestRepository repository;

    @Autowired
    public RequestRepositoryManager(final RequestRepository repository) {
        this.repository = repository;
    }

    public void saveRequestInfo(final String ip, Double latitude, Double longitude, final WeatherInfo weatherInfo) {
        runAsync(() -> {
            final WeatherRequestEntity entity = new WeatherRequestEntity(System.currentTimeMillis(), ip, latitude, longitude, weatherInfo);
            try {
                repository.saveAndFlush(entity);
            } catch (final Exception e) {
                LOG.error("Failed to store request entity: {}, with exception: {}", entity, e);
            }
        });
    }
}
