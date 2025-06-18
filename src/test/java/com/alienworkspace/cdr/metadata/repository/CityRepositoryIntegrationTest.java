package com.alienworkspace.cdr.metadata.repository;

import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityRepositoryIntegrationTest extends AbstractionContainerBaseTest {

    private City.CityBuilder cityBuilder = City.builder();

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountyRepository countyRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private LocationRepository locationRepository;

    Country country;
    State state;
    County county;

    @BeforeEach
    public void setUp() {
        locationRepository.deleteAll();
        communityRepository.deleteAllInBatch();
        cityRepository.deleteAllInBatch();
        countyRepository.deleteAllInBatch();
        stateRepository.deleteAllInBatch();
        countryRepository.deleteAllInBatch();

        country = countryRepository.save(Country.builder().countryName("Test Country").countryCode("CN123")
                .currencyCode("USD").currencySymbol("$").build());
        state = stateRepository.save(State.builder().stateName("Test State").stateCode("ST123").country(country).build());
        county = countyRepository.save(County.builder().countyName("Test County").countyCode("COU123").state(state).build());

        cityBuilder
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .cityCode("CIT123")
                .cityGeoCode("GEO123")
                .cityPhoneCode(234);
    }

    @Test
    @DisplayName("Test Create City")
    public void testCreateCity() {
        // Arrange
        City city = cityBuilder.build();
        city.setCreatedBy(1L);
        city.setCreatedAt(LocalDateTime.now());
        city.setCounty(county);

        // Act
        City actualCity = cityRepository.save(city);

        // Assert
        assertNotNull(actualCity);
        assertNotNull(actualCity.getCityId());
        assertEquals("Test City", actualCity.getCityName());
    }

    @Test
    @DisplayName("Test findAllByVoidedIsFalse returns only non-voided cities")
    public void testFindAllByVoidedIsFalse() {
        // Arrange
        City city1 = cityBuilder.cityName("City 1").cityCode("CIT126").build();
        city1.setCreatedBy(1L);
        city1.setCreatedAt(LocalDateTime.now());
        city1.setVoided(false);
        city1.setCounty(county);

        City city2 = cityBuilder.cityName("City 2").cityCode("CIT125").build();
        city2.setCreatedBy(1L);
        city2.setCreatedAt(LocalDateTime.now());
        city2.setVoided(true);
        city2.setCounty(county);

        cityRepository.save(city1);
        cityRepository.save(city2);

        // Act
        List<City> nonVoidedCities = cityRepository.findAllByVoidedIsFalse();

        // Assert
        assertNotNull(nonVoidedCities);
        assertEquals(1, nonVoidedCities.size());
        assertEquals("City 1", nonVoidedCities.get(0).getCityName());
    }
} 