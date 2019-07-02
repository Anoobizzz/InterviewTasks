package io.github.anoobizzz.mintostask.controller;

import io.github.anoobizzz.mintostask.component.IWeatherRequestHandler;
import io.github.anoobizzz.mintostask.domain.WeatherInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class WebController {
    private final IWeatherRequestHandler requestHandler;

    @Autowired
    public WebController(final @NonNull IWeatherRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @GetMapping(path = "/weather", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<WeatherInfo> getWeather(@RequestHeader(value = "x-forwarded-for", required = false) final String origin,
                                           final HttpServletRequest request) {
        final String ip = !isBlank(origin) ? origin : request.getRemoteAddr();
        return ResponseEntity.ok(requestHandler.getWeather(ip));
    }
}

