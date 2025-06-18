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
public class CountyRepositoryIntegrationTest extends AbstractionContainerBaseTest {

    private County.CountyBuilder countyBuilder = County.builder();

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

    @BeforeEach
    public void setUp() {
        locationRepository.deleteAll();
        communityRepository.deleteAll();
        cityRepository.deleteAll();
        countyRepository.deleteAll();
        stateRepository.deleteAll();
        countryRepository.deleteAll();

        country = countryRepository.save(Country.builder().countryName("Test Country").countryCode("CN123")
                .currencyCode("USD").currencySymbol("$").build());
        state = stateRepository.save(State.builder().stateName("Test State").stateCode("ST123").country(country).build());

        countyBuilder
                .countyName("Test County")
                .locale("en_US")
                .localePreferred(true)
                .countyCode("COU123")
                .countyGeoCode("GEO123")
                .countyPhoneCode(234);

        state = stateRepository.save(State.builder().stateName("Test State").build());
    }

    @Test
    @DisplayName("Test Create County")
    public void testCreateCounty() {
        // Arrange
        County county = countyBuilder.build();
        county.setCreatedBy(1L);
        county.setCreatedAt(LocalDateTime.now());
        county.setState(state);

        // Act
        County actualCounty = countyRepository.save(county);

        // Assert
        assertNotNull(actualCounty);
        assertNotNull(actualCounty.getCountyId());
        assertEquals("Test County", actualCounty.getCountyName());
    }

    @Test
    @DisplayName("Test findAllByVoidedIsFalse returns only non-voided counties")
    public void testFindAllByVoidedIsFalse() {
        // Arrange
        County county1 = countyBuilder.countyName("County 1").build();
        county1.setCreatedBy(1L);
        county1.setCreatedAt(LocalDateTime.now());
        county1.setVoided(false);
        county1.setState(state);

        County county2 = countyBuilder.countyName("County 2").countyCode("COU124").build();
        county2.setCreatedBy(1L);
        county2.setCreatedAt(LocalDateTime.now());
        county2.setVoided(true);
        county2.setState(state);

        countyRepository.save(county1);
        countyRepository.save(county2);

        // Act
        List<County> nonVoidedCounties = countyRepository.findAllByVoidedIsFalse();

        // Assert
        assertNotNull(nonVoidedCounties);
        assertEquals(1, nonVoidedCounties.size());
        assertEquals("County 1", nonVoidedCounties.get(0).getCountyName());
    }
} 