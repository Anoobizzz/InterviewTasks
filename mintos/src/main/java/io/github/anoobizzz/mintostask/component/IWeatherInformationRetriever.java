package io.github.anoobizzz.mintostask.component;

import io.github.anoobizzz.mintostask.domain.WeatherApiResponse;

public interface IWeatherInformationRetriever {
    WeatherApiResponse getWeather(final double latitude, final double longtitude);
}
