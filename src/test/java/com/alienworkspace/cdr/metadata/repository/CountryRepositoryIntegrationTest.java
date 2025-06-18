package com.alienworkspace.cdr.metadata.repository;

import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CountryRepositoryIntegrationTest extends AbstractionContainerBaseTest {

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

    private Country.CountryBuilder countryBuilder = Country.builder();

    @BeforeEach
    public void setUp() {
        locationRepository.deleteAll();
        communityRepository.deleteAll();
        cityRepository.deleteAll();
        countyRepository.deleteAll();
        stateRepository.deleteAll();
        countryRepository.deleteAll();
        countryBuilder
                .countryName("Test Country")
                .countryCode("CTR123")
                .locale("en_US")
                .localePreferred(true)
                .countryPhoneCode(234)
                .currencyCode("USD")
                .currencySymbol("$")
                .currencyName("Dollar")
                .countryGeoCode("GEO123");
    }

    @Test
    @DisplayName("Test Create Country")
    public void testCreateCountry() {
        // Arrange
        Country country = countryBuilder.build();
        country.setCreatedBy(1L);
        country.setCreatedAt(LocalDateTime.now());

        // Act
        Country actualCountry = countryRepository.save(country);

        // Assert
        assertNotNull(actualCountry);
        assertNotNull(actualCountry.getCountryId());
        assertEquals("Test Country", actualCountry.getCountryName());
    }

    @Test
    @DisplayName("Test findAllByVoidedIsFalse returns only non-voided countries")
    public void testFindAllByVoidedIsFalse() {
        // Arrange
        Country country1 = countryBuilder.countryName("Country 1").countryCode("C1").build();
        country1.setCreatedBy(1L);
        country1.setCreatedAt(LocalDateTime.now());
        country1.setVoided(false);

        Country country2 = countryBuilder.countryName("Country 2").countryCode("C2").build();
        country2.setCreatedBy(1L);
        country2.setCreatedAt(LocalDateTime.now());
        country2.setVoided(true);

        countryRepository.save(country1);
        countryRepository.save(country2);

        // Act
        List<Country> nonVoidedCountries = countryRepository.findAllByVoidedIsFalse();

        // Assert
        assertNotNull(nonVoidedCountries);
        assertEquals(1, nonVoidedCountries.size());
        assertEquals("Country 1", nonVoidedCountries.get(0).getCountryName());
    }

    @Test
    @DisplayName("Test findByCountryCode returns correct country")
    public void testFindByCountryCode() {
        // Arrange
        Country country = countryBuilder.countryName("Special Country").countryCode("SPECIAL").build();
        country.setCreatedBy(1L);
        country.setCreatedAt(LocalDateTime.now());
        country.setVoided(false);
        countryRepository.save(country);

        // Act
        Optional<Country> found = countryRepository.findByCountryCode("SPECIAL");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Special Country", found.get().getCountryName());
    }
} 