package io.github.anoobizzz.mintostask.component.impl;

import io.github.anoobizzz.mintostask.component.IGeolocationRetriever;
import io.github.anoobizzz.mintostask.component.IWeatherInformationRetriever;
import io.github.anoobizzz.mintostask.component.IWeatherRequestHandler;
import io.github.anoobizzz.mintostask.domain.GeolocationApiResponse;
import io.github.anoobizzz.mintostask.domain.WeatherApiResponse;
import io.github.anoobizzz.mintostask.domain.WeatherInfo;
import io.github.anoobizzz.mintostask.exception.InsufficientInformationException;
import io.github.anoobizzz.mintostask.persistance.RequestRepositoryManager;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WeatherRequestHandler.class, WeatherRequestHandlerTest.MockConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource("classpath:application.yml")
public class WeatherRequestHandlerTest {
    private static final String WEATHER_MAIN = "It is rainy, we are in Riga, what did you expect? I hate writing these tests...";
    private static final String EXCEPTION_MESSAGE = "Failed to retrieve geolocation information, possibly due to IP detection failure.";
    private static final String IP = "8.8.8.8";
    private static final double LONGITUDE = 123.0;
    private static final double LATITUDE = 321.0;

    @Autowired
    private IWeatherInformationRetriever weatherInformationRetriever;
    @Autowired
    private IGeolocationRetriever geolocationRetriever;
    @Autowired
    private RequestRepositoryManager repositoryManager;
    @Autowired
    private IWeatherRequestHandler requestHandler;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getWeather() {
        final GeolocationApiResponse geolocationApiResponse = new GeolocationApiResponse();
        geolocationApiResponse.setLongitude(LONGITUDE);
        geolocationApiResponse.setLatitude(LATITUDE);
        when(geolocationRetriever.getGeolocation(IP)).thenReturn(geolocationApiResponse);
        final WeatherApiResponse weatherApiResponse = mock(WeatherApiResponse.class);
        when(weatherApiResponse.getWeatherMain()).thenReturn(WEATHER_MAIN);
        when(weatherInformationRetriever.getWeather(LATITUDE, LONGITUDE)).thenReturn(weatherApiResponse);

        final WeatherInfo expected = WeatherInfo.fromExternalApiResponse(weatherApiResponse);
        assertEquals(expected.getOverallDescription(), requestHandler.getWeather(IP).getOverallDescription());
        verify(geolocationRetriever).getGeolocation(IP);
        verify(weatherInformationRetriever).getWeather(LATITUDE, LONGITUDE);
        verify(repositoryManager).saveRequestInfo(anyString(), anyDouble(), anyDouble(), any(WeatherInfo.class));
    }

    @Test
    public void getWeatherInsufficientInfoException() {
        exceptionRule.expect(InsufficientInformationException.class);
        exceptionRule.expectMessage(EXCEPTION_MESSAGE);
        when(geolocationRetriever.getGeolocation(IP)).thenReturn(new GeolocationApiResponse());

        requestHandler.getWeather(IP);
    }

    static class MockConfig {
        @Bean
        public IWeatherInformationRetriever weather() {
            return mock(IWeatherInformationRetriever.class);
        }

        @Bean
        public IGeolocationRetriever geolocation() {
            return mock(IGeolocationRetriever.class);
        }

        @Bean
        public RequestRepositoryManager repositoryManager() {
            return mock(RequestRepositoryManager.class);
        }
    }
}
