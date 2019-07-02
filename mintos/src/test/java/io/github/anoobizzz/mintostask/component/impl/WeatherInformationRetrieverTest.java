package io.github.anoobizzz.mintostask.component.impl;

import io.github.anoobizzz.mintostask.component.IWeatherInformationRetriever;
import io.github.anoobizzz.mintostask.configuration.CacheConfiguration;
import io.github.anoobizzz.mintostask.domain.WeatherApiResponse;
import io.github.anoobizzz.mintostask.exception.WeatherApiException;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WeatherInformationRetriever.class, CacheConfiguration.class, WeatherInformationRetrieverTest.MockConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource("classpath:application.yml")
public class WeatherInformationRetrieverTest {
    private static final String WEATHER_API_URL = "https://test.com/weather?units=metric&APPID=123&lat={latitude}&lon={longitude}";
    private static final double LATITUDE = 123.0;
    private static final double LONGITUDE = 321.0;
    public static final String EXCEPTION_MESSAGE = "Failed to retrieve weather information from external api";

    @Autowired
    private IWeatherInformationRetriever retriever;
    @Autowired
    private RestTemplate client;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getWeather() {
        final Map<String, Object> params = new HashMap<>();
        params.put("latitude", LATITUDE);
        params.put("longitude", LONGITUDE);
        final WeatherApiResponse expected = new WeatherApiResponse();
        final ResponseEntity<WeatherApiResponse> responseEntity = new ResponseEntity<>(expected, HttpStatus.OK);
        when(client.getForEntity(WEATHER_API_URL, WeatherApiResponse.class, params)).thenReturn(responseEntity);

        assertEquals(expected, retriever.getWeather(LATITUDE, LONGITUDE));
        verify(client).getForEntity(WEATHER_API_URL, WeatherApiResponse.class, params);
    }

    @Test
    public void getWeatherClientRequestException() {
        exceptionRule.expect(WeatherApiException.class);
        exceptionRule.expectCause(Is.isA(RestClientException.class));
        exceptionRule.expectMessage(EXCEPTION_MESSAGE);
        final Map<String, Object> params = new HashMap<>();
        params.put("latitude", LATITUDE);
        params.put("longitude", LONGITUDE);
        when(client.getForEntity(WEATHER_API_URL, WeatherApiResponse.class, params)).thenThrow(RestClientException.class);

        retriever.getWeather(LATITUDE, LONGITUDE);
    }

    @Test
    public void getGeolocationRequestStatusException() {
        exceptionRule.expect(WeatherApiException.class);
        exceptionRule.expectCause(IsNull.nullValue(null));
        exceptionRule.expectMessage(EXCEPTION_MESSAGE);
        final Map<String, Object> params = new HashMap<>();
        params.put("latitude", LATITUDE);
        params.put("longitude", LONGITUDE);
        final WeatherApiResponse expected = new WeatherApiResponse();
        final ResponseEntity<WeatherApiResponse> responseEntity = new ResponseEntity<>(expected, HttpStatus.BAD_REQUEST);
        when(client.getForEntity(WEATHER_API_URL, WeatherApiResponse.class, params)).thenReturn(responseEntity);

        retriever.getWeather(LATITUDE, LONGITUDE);
    }

    @Test
    public void getGeolocationCacheVerification() {
        final Map<String, Object> params = new HashMap<>();
        params.put("latitude", LATITUDE);
        params.put("longitude", LONGITUDE);
        final WeatherApiResponse expected = new WeatherApiResponse();
        final ResponseEntity<WeatherApiResponse> responseEntity = new ResponseEntity<>(expected, HttpStatus.OK);
        when(client.getForEntity(WEATHER_API_URL, WeatherApiResponse.class, params)).thenReturn(responseEntity);

        retriever.getWeather(LATITUDE, LONGITUDE);
        retriever.getWeather(LATITUDE, LONGITUDE);
        verify(client).getForEntity(WEATHER_API_URL, WeatherApiResponse.class, params);
    }


    static class MockConfig {
        @Bean
        public RestTemplate client() {
            return mock(RestTemplate.class);
        }
    }
}
