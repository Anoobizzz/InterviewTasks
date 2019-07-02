package io.github.anoobizzz.mintostask.component.impl;

import io.github.anoobizzz.mintostask.component.IGeolocationRetriever;
import io.github.anoobizzz.mintostask.component.IWeatherInformationRetriever;
import io.github.anoobizzz.mintostask.component.IWeatherRequestHandler;
import io.github.anoobizzz.mintostask.domain.GeolocationApiResponse;
import io.github.anoobizzz.mintostask.domain.WeatherApiResponse;
import io.github.anoobizzz.mintostask.domain.WeatherInfo;
import io.github.anoobizzz.mintostask.exception.InsufficientInformationException;
import io.github.anoobizzz.mintostask.persistance.RequestRepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class WeatherRequestHandler implements IWeatherRequestHandler {
    private final IGeolocationRetriever geolocationRetriever;
    private final IWeatherInformationRetriever weatherInformationRetriever;
    private final RequestRepositoryManager repositoryManager;

    @Autowired
    public WeatherRequestHandler(@NonNull final IGeolocationRetriever geolocationRetriever,
                                 @NonNull final IWeatherInformationRetriever weatherInformationRetriever,
                                 @NonNull final RequestRepositoryManager repositoryManager) {
        this.geolocationRetriever = geolocationRetriever;
        this.weatherInformationRetriever = weatherInformationRetriever;
        this.repositoryManager = repositoryManager;
    }

    @Override
    public WeatherInfo getWeather(final String ip) {
        final GeolocationApiResponse geolocationApiResponse = geolocationRetriever.getGeolocation(ip);
        final Double latitude = geolocationApiResponse.getLatitude();
        final Double longitude = geolocationApiResponse.getLongitude();
        if (latitude == null || longitude == null) {
            throw new InsufficientInformationException();
        }
        final WeatherApiResponse weatherApiResponse = weatherInformationRetriever.getWeather(latitude, longitude);
        final WeatherInfo weatherInfo = WeatherInfo.fromExternalApiResponse(weatherApiResponse);
        repositoryManager.saveRequestInfo(ip, latitude, longitude, weatherInfo);
        return weatherInfo;
    }
}
