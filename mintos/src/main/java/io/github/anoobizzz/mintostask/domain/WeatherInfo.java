package io.github.anoobizzz.mintostask.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Embeddable;

@Embeddable
public class WeatherInfo {
    private String overallDescription;
    private Double averageTemperature;
    private Double minTemperature;
    private Double maxTemperature;
    private Integer pressure;
    private Integer humidity;
    private Double windSpeed;

    public WeatherInfo() {
    }

    public WeatherInfo(final String overallDescription, final Double averageTemperature, final Double minTemperature,
                       final Double maxTemperature, final Integer pressure, final Integer humidity, final Double windSpeed) {
        this.overallDescription = overallDescription;
        this.averageTemperature = averageTemperature;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public static WeatherInfo fromExternalApiResponse(final WeatherApiResponse response) {
        return new WeatherInfo(response.getWeatherMain(),
                response.getMainTemperature(),
                response.getMainTempMin(),
                response.getMainTempMax(),
                response.getMainPressure(),
                response.getMainHumidity(),
                response.getWindSpeed());
    }

    public String getOverallDescription() {
        return overallDescription;
    }

    public Double getAverageTemperature() {
        return averageTemperature;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public Integer getPressure() {
        return pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }
}
