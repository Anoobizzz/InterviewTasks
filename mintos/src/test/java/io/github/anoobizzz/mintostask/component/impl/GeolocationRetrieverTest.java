package io.github.anoobizzz.mintostask.component.impl;

import io.github.anoobizzz.mintostask.component.IGeolocationRetriever;
import io.github.anoobizzz.mintostask.configuration.CacheConfiguration;
import io.github.anoobizzz.mintostask.domain.GeolocationApiResponse;
import io.github.anoobizzz.mintostask.exception.GeolocationApiException;
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
@ContextConfiguration(classes = {GeolocationRetriever.class, CacheConfiguration.class, GeolocationRetrieverTest.MockConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource("classpath:application.yml")
public class GeolocationRetrieverTest {
    private static final String GEO_API_URL = "https://test.com/{ip}/json/";
    private static final String IP = "8.8.8.8";
    public static final String EXCEPTION_MESSAGE = "Failed to retrieve geolocation information from external api";

    @Autowired
    private IGeolocationRetriever retriever;
    @Autowired
    private RestTemplate client;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getGeolocation() {
        final Map<String, Object> params = new HashMap<>();
        params.put("ip", IP);
        final GeolocationApiResponse expected = new GeolocationApiResponse();
        expected.setLatitude(123.0);
        expected.setLongitude(321.0);
        final ResponseEntity<GeolocationApiResponse> responseEntity = new ResponseEntity<>(expected, HttpStatus.OK);
        when(client.getForEntity(GEO_API_URL, GeolocationApiResponse.class, params)).thenReturn(responseEntity);

        assertEquals(expected, retriever.getGeolocation(IP));
        verify(client).getForEntity(GEO_API_URL, GeolocationApiResponse.class, params);
    }

    @Test
    public void getGeolocationClientRequestException() {
        exceptionRule.expect(GeolocationApiException.class);
        exceptionRule.expectCause(Is.isA(RestClientException.class));
        exceptionRule.expectMessage(EXCEPTION_MESSAGE);
        final Map<String, Object> params = new HashMap<>();
        params.put("ip", IP);
        final GeolocationApiResponse expected = new GeolocationApiResponse();
        expected.setLatitude(123.0);
        expected.setLongitude(321.0);
        when(client.getForEntity(GEO_API_URL, GeolocationApiResponse.class, params)).thenThrow(RestClientException.class);

        retriever.getGeolocation(IP);
    }

    @Test
    public void getGeolocationRequestStatusException() {
        exceptionRule.expect(GeolocationApiException.class);
        exceptionRule.expectCause(IsNull.nullValue(null));
        exceptionRule.expectMessage(EXCEPTION_MESSAGE);
        final Map<String, Object> params = new HashMap<>();
        params.put("ip", IP);
        final GeolocationApiResponse expected = new GeolocationApiResponse();
        expected.setLatitude(123.0);
        expected.setLongitude(321.0);
        final ResponseEntity<GeolocationApiResponse> responseEntity = new ResponseEntity<>(expected, HttpStatus.BAD_REQUEST);
        when(client.getForEntity(GEO_API_URL, GeolocationApiResponse.class, params)).thenReturn(responseEntity);

        retriever.getGeolocation(IP);
    }

    @Test
    public void getGeolocationCacheVerification() {
        final Map<String, Object> params = new HashMap<>();
        params.put("ip", IP);
        final GeolocationApiResponse expected = new GeolocationApiResponse();
        expected.setLatitude(123.0);
        expected.setLongitude(321.0);
        final ResponseEntity<GeolocationApiResponse> responseEntity = new ResponseEntity<>(expected, HttpStatus.OK);
        when(client.getForEntity(GEO_API_URL, GeolocationApiResponse.class, params)).thenReturn(responseEntity);

        retriever.getGeolocation(IP);
        retriever.getGeolocation(IP);
        verify(client).getForEntity(GEO_API_URL, GeolocationApiResponse.class, params);

    }


    static class MockConfig {
        @Bean
        public RestTemplate client() {
            return mock(RestTemplate.class);
        }
    }
}
