package io.github.anoobizzz.mintostask.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "WEATHER_REQUESTS")
public class WeatherRequestEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private Long requestTime;
    private String ip;
    private Double latitude;
    private Double longitude;

    @Embedded
    private WeatherInfo weatherInfo;

    public WeatherRequestEntity(Long requestTime, String ip, Double latitude, Double longitude, WeatherInfo weatherInfo) {
        this.requestTime = requestTime;
        this.ip = ip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.weatherInfo = weatherInfo;
    }

    public Long getId() {
        return id;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    public String getIp() {
        return ip;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public WeatherInfo getWeatherInfo() {
        return weatherInfo;
    }
}
