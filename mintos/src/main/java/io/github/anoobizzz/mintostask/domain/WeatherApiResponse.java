package io.github.anoobizzz.mintostask.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherApiResponse {
    private String weatherMain;
    private Double mainTemperature;
    private Integer mainPressure;
    private Integer mainHumidity;
    private Double mainTempMin;
    private Double mainTempMax;
    private Double windSpeed;

    @JsonProperty("weather")
    private void unnestWeather(List<Map<String, Object>> weather) {
        this.weatherMain = (String) weather.get(0).get("main");
    }

    @JsonProperty("main")
    private void unnestMain(Map<String, Object> main) {
        //NOTE: Weather API inconsistently provided both decimal and integer values hence the solution...
        this.mainTemperature = Double.valueOf(String.valueOf(main.get("temp")));
        this.mainPressure = Integer.valueOf(String.valueOf(main.get("pressure")));
        this.mainHumidity = Integer.valueOf(String.valueOf(main.get("humidity")));
        this.mainTempMin = Double.valueOf(String.valueOf(main.get("temp_min")));
        this.mainTempMax = Double.valueOf(String.valueOf(main.get("temp_max")));
    }

    @JsonProperty("wind")
    private void unnestWind(Map<String, Object> wind) {
        this.windSpeed = (Double) wind.get("speed");
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public Double getMainTemperature() {
        return mainTemperature;
    }

    public Integer getMainPressure() {
        return mainPressure;
    }

    public Integer getMainHumidity() {
        return mainHumidity;
    }

    public Double getMainTempMin() {
        return mainTempMin;
    }

    public Double getMainTempMax() {
        return mainTempMax;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }
}
