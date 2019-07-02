package io.github.anoobizzz.mintostask.component;

import io.github.anoobizzz.mintostask.domain.WeatherInfo;

public interface IWeatherRequestHandler {
    WeatherInfo getWeather(final String ip);
}
