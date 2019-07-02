package io.github.anoobizzz.mintostask.component;

import io.github.anoobizzz.mintostask.domain.GeolocationApiResponse;

public interface IGeolocationRetriever {
    GeolocationApiResponse getGeolocation(final String ip);
}
