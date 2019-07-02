package io.github.anoobizzz.mintostask.controller;

import io.github.anoobizzz.mintostask.component.IWeatherRequestHandler;
import io.github.anoobizzz.mintostask.domain.WeatherInfo;
import io.github.anoobizzz.mintostask.exception.GeolocationApiException;
import io.github.anoobizzz.mintostask.exception.InsufficientInformationException;
import io.github.anoobizzz.mintostask.exception.RestExceptionHandler;
import io.github.anoobizzz.mintostask.exception.WeatherApiException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {WebController.class, RestExceptionHandler.class, WebControllerTest.MockConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class WebControllerTest {
    private static final String ENDPOINT = "/weather";
    private static final String X_FORWARDED_FOR = "x-forwarded-for";
    private static final String IP = "8.8.8.8";
    private static final String POSITIVE_RESPONSE = "{\"overallDescription\":\"\",\"averageTemperature\":0.0,\"minTemperature\":0.0,\"maxTemperature\":0.0,\"pressure\":1,\"humidity\":1,\"windSpeed\":0.0}";
    private static final String WEATHER_API_EXCEPTION_RESPONSE = "{\"status\":\"I_AM_A_TEAPOT\",\"message\":\"Failed to retrieve weather information from external api\"}";
    private static final String GENERIC_ERROR_RESPONSE = "{\"status\":\"INTERNAL_SERVER_ERROR\",\"message\":\"Service failed to process the request.\"}";
    private static final String GEOLOCATION_API_EXCEPTION_RESPONSE = "{\"status\":\"I_AM_A_TEAPOT\",\"message\":\"Failed to retrieve geolocation information from external api\"}";
    private static final String INSUFFICIENT_EXCEPTION = "{\"status\":\"INTERNAL_SERVER_ERROR\",\"message\":\"Failed to retrieve geolocation information, possibly due to IP detection failure. Resolved IP: 127.0.0.1\"}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IWeatherRequestHandler requestHandler;

    @Test
    public void getWeather() throws Exception {
        final WeatherInfo weatherInfo = new WeatherInfo("", 0.0, 0.0, 0.0, 1, 1, 0.0);
        when(requestHandler.getWeather(IP)).thenReturn(weatherInfo);

        mockMvc.perform(get(ENDPOINT).accept(MediaType.APPLICATION_JSON)
                .header(X_FORWARDED_FOR, IP))
                .andExpect(status().isOk())
                .andExpect(content().json(POSITIVE_RESPONSE))
                .andDo(print());
    }

    @Test
    public void getWeatherGeolocationApiException() throws Exception {
        when(requestHandler.getWeather(IP)).thenThrow(new GeolocationApiException(HttpStatus.I_AM_A_TEAPOT));

        mockMvc.perform(get(ENDPOINT).accept(MediaType.APPLICATION_JSON)
                .header(X_FORWARDED_FOR, IP))
                .andExpect(status().is(418))
                .andExpect(content().json(GEOLOCATION_API_EXCEPTION_RESPONSE))
                .andDo(print());
    }

    @Test
    public void getWeatherWeatherApiException() throws Exception {
        when(requestHandler.getWeather(IP)).thenThrow(new WeatherApiException(HttpStatus.I_AM_A_TEAPOT));

        mockMvc.perform(get(ENDPOINT).accept(MediaType.APPLICATION_JSON)
                .header(X_FORWARDED_FOR, IP))
                .andExpect(status().is(418))
                .andExpect(content().json(WEATHER_API_EXCEPTION_RESPONSE))
                .andDo(print());
    }

    @Test
    public void getWeatherInsufficientInformationException() throws Exception {
        when(requestHandler.getWeather(IP)).thenThrow(new InsufficientInformationException());

        mockMvc.perform(get(ENDPOINT).accept(MediaType.APPLICATION_JSON)
                .header(X_FORWARDED_FOR, IP))
                .andExpect(status().is(500))
                .andExpect(content().json(INSUFFICIENT_EXCEPTION))
                .andDo(print());
    }

    @Test
    public void getWeatherUnexpectedException() throws Exception {
        when(requestHandler.getWeather(IP)).thenThrow(new RuntimeException());

        mockMvc.perform(get(ENDPOINT).accept(MediaType.APPLICATION_JSON)
                .header(X_FORWARDED_FOR, IP))
                .andExpect(status().is(500))
                .andExpect(content().json(GENERIC_ERROR_RESPONSE))
                .andDo(print());
    }

    @TestConfiguration
    @EnableWebMvc
    static class MockConfig {
        @Bean
        public IWeatherRequestHandler requestHandler() {
            return mock(IWeatherRequestHandler.class);
        }
    }
}
