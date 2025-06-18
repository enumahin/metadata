package com.alienworkspace.cdr.metadata.repository;

import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
public class LocationRepositoryIntegrationTest extends AbstractionContainerBaseTest {

    private Location.LocationBuilder locationBuilder = Location.builder();

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
    City city;
    Community community;

    @BeforeEach
    public void setUp() {
        locationRepository.deleteAll();
        communityRepository.deleteAll();
        cityRepository.deleteAll();
        countyRepository.deleteAll();
        stateRepository.deleteAll();
        countryRepository.deleteAll();

        locationBuilder
                .locationName("Test Location")
                .locale("en_US")
                .localePreferred(true)
                .locationCode("LOC123")
                .locationGeoCode("GEO123")
                .locationPhoneCode(234);
        country = countryRepository.save(Country.builder().countryName("Test Country").countryCode("CN123")
                .currencyCode("USD").currencySymbol("$").build());
        state = stateRepository.save(State.builder().stateName("Test State").stateCode("ST123").country(country).build());
        county = countyRepository.save(County.builder().countyName("Test County").countyCode("COU123").state(state).build());
        city = cityRepository.save(City.builder().cityName("Test City").cityCode("CIT123").cityGeoCode("GEO123").county(county).build());
        community = communityRepository.save(Community.builder().communityName("Test Community").city(city).communityCode("COM123").build());
    }

    @Test
    @DisplayName("Test Create Location")
    public void testCreateLocation() {
        // Arrange
        Location location = locationBuilder.build();
        location.setCreatedBy(1L);
        location.setCreatedAt(LocalDateTime.now());
        location.setVoided(false);
        location.setCommunity(community);

        // Act
        Location actualLocation = locationRepository.save(location);

        // Assert
        assertNotNull(actualLocation);
        assertNotNull(actualLocation.getLocationId());
        assertEquals("Test Location", actualLocation.getLocationName());
    }

    @Test
    @DisplayName("Test findAllByVoidedIsFalse returns only non-voided locations")
    public void testFindAllByVoidedIsFalse() {
        // Arrange
        Location location1 = locationBuilder.locationName("Location 1").locationCode("2-LOC123").build();
        location1.setCreatedBy(1L);
        location1.setCreatedAt(LocalDateTime.now());
        location1.setVoided(false);
        location1.setCommunity(community);

        Location location2 = locationBuilder.locationName("Location 2").locationCode("3-LOC101").build();
        location2.setCreatedBy(1L);
        location2.setCreatedAt(LocalDateTime.now());
        location2.setVoided(true);
        location2.setCommunity(community);

        locationRepository.save(location1);
        locationRepository.save(location2);

        // Act
        List<Location> nonVoidedLocations = locationRepository.findAllByVoidedIsFalse();

        // Assert
        assertNotNull(nonVoidedLocations);
        assertEquals(1, nonVoidedLocations.size());
        assertEquals("Location 1", nonVoidedLocations.get(0).getLocationName());
    }
} 