package io.github.anoobizzz.mintostask.component.impl;

import io.github.anoobizzz.mintostask.component.IGeolocationRetriever;
import io.github.anoobizzz.mintostask.domain.GeolocationApiResponse;
import io.github.anoobizzz.mintostask.exception.GeolocationApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static io.github.anoobizzz.mintostask.configuration.CacheConfiguration.GEOLOCATION_API_CACHE;
import static org.springframework.http.HttpStatus.OK;

@Component
public class GeolocationRetriever implements IGeolocationRetriever {
    private final RestTemplate client;
    private final String geoApiUrl;

    @Autowired
    public GeolocationRetriever(@NonNull final RestTemplate client,
                                @NonNull @Value("${geo.api.url}") final String geoApiUrl) {
        this.client = client;
        this.geoApiUrl = geoApiUrl;
    }

    @Override
    @Cacheable(GEOLOCATION_API_CACHE)
    public GeolocationApiResponse getGeolocation(final String ip) {
        try {
            final Map<String, Object> params = new HashMap<>();
            params.put("ip", ip);
            final ResponseEntity<GeolocationApiResponse> responseEntity = client.getForEntity(geoApiUrl, GeolocationApiResponse.class, params);
            final HttpStatus statusCode = responseEntity.getStatusCode();
            if (statusCode != OK) {
                throw new GeolocationApiException(statusCode);
            }
            return responseEntity.getBody();
        } catch (final RestClientException e) {
            throw new GeolocationApiException(e);
        }
    }
}
