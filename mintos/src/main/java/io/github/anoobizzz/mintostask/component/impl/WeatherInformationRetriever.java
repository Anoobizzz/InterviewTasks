package io.github.anoobizzz.mintostask.component.impl;

import io.github.anoobizzz.mintostask.component.IWeatherInformationRetriever;
import io.github.anoobizzz.mintostask.domain.WeatherApiResponse;
import io.github.anoobizzz.mintostask.exception.WeatherApiException;
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

import static io.github.anoobizzz.mintostask.configuration.CacheConfiguration.WEATHER_API_CACHE;
import static org.springframework.http.HttpStatus.OK;

@Component
public class WeatherInformationRetriever implements IWeatherInformationRetriever {
    private final RestTemplate client;
    private final String weatherApiUri;

    public WeatherInformationRetriever(@NonNull final RestTemplate client,
                                       @NonNull @Value("${weather.api.url}") final String weatherApiUri) {
        this.client = client;
        this.weatherApiUri = weatherApiUri;
    }

    @Override
    @Cacheable(WEATHER_API_CACHE)
    public WeatherApiResponse getWeather(double latitude, double longitude) {
        try {
            final Map<String, Object> params = new HashMap<>();
            params.put("latitude", latitude);
            params.put("longitude", longitude);
            final ResponseEntity<WeatherApiResponse> responseEntity = client.getForEntity(weatherApiUri, WeatherApiResponse.class, params);
            final HttpStatus statusCode = responseEntity.getStatusCode();
            if (statusCode != OK) {
                throw new WeatherApiException(statusCode);
            }
            return responseEntity.getBody();
        } catch (final RestClientException e) {
            throw new WeatherApiException(e);
        }
    }
}
