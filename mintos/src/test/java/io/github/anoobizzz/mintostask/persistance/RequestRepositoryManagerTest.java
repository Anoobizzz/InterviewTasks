package io.github.anoobizzz.mintostask.persistance;

import io.github.anoobizzz.mintostask.domain.WeatherInfo;
import io.github.anoobizzz.mintostask.domain.WeatherRequestEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RequestRepositoryManager.class, RequestRepositoryManagerTest.MockConfig.class})
public class RequestRepositoryManagerTest {
    private static final String IP = "8.8.8.8";
    private static final Double LATITUDE = 123.0;
    private static final Double LONGITUDE = 321.0;
    @Autowired
    private RequestRepository repository;
    @Autowired
    private RequestRepositoryManager repositoryManager;

    @Test
    public void saveRequestInfo() {
        final WeatherInfo weatherInfo = new WeatherInfo();
        repositoryManager.saveRequestInfo(IP, LATITUDE, LONGITUDE, weatherInfo);

        final ArgumentCaptor<WeatherRequestEntity> captor = ArgumentCaptor.forClass(WeatherRequestEntity.class);
        verify(repository, timeout(1000)).saveAndFlush(captor.capture());
        final WeatherRequestEntity entity = captor.getValue();
        assertEquals(IP, entity.getIp());
        assertEquals(LATITUDE, entity.getLatitude());
        assertEquals(LONGITUDE, entity.getLongitude());
        assertEquals(weatherInfo, entity.getWeatherInfo());
    }

    static class MockConfig {
        @Bean
        private RequestRepository repository() {
            return mock(RequestRepository.class);
        }
    }
}
