package io.github.anoobizzz.mintostask.persistance;

import io.github.anoobizzz.mintostask.domain.WeatherRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RequestRepository extends JpaRepository<WeatherRequestEntity, Long> {
}
