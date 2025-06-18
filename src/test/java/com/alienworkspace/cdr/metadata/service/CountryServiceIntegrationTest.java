package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.City;
import com.alienworkspace.cdr.metadata.model.Country;
import com.alienworkspace.cdr.metadata.model.County;
import com.alienworkspace.cdr.metadata.model.State;
import com.alienworkspace.cdr.metadata.repository.*;
import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CountryServiceIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountyRepository countyRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private LocationRepository locationRepository;

    Country country;
    State state;
    County county;
    City city;

    @BeforeEach
    public void setUp() {
        locationRepository.deleteAll();
        communityRepository.deleteAll();
        cityRepository.deleteAll();
        countyRepository.deleteAll();
        stateRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @DisplayName("Test Create Country")
    @Test
    public void testCreateCountry() {
        // Arrange
        CountryDto countryDto = CountryDto.builder()
                .countryName("Test Country")
                .locale("en_US")
                .localePreferred(true)
                .currencyCode("USD")
                .currencySymbol("$")
                .countryCode("COUNTRY123")
                .countryGeoCode("GEO123")
                .countryPhoneCode(234)
                .build();

        // Act
        CountryDto actualCountryDto = countryService.createCountry(countryDto);

        // Assert
        assertNotNull(actualCountryDto);
        assertEquals(countryDto.getCountryName(), actualCountryDto.getCountryName());
        assertEquals(countryDto.getLocale(), actualCountryDto.getLocale());
        assertEquals(countryDto.isLocalePreferred(), actualCountryDto.isLocalePreferred());
        assertEquals(countryDto.getCountryCode(), actualCountryDto.getCountryCode());
        assertEquals(countryDto.getCountryGeoCode(), actualCountryDto.getCountryGeoCode());
        assertEquals(countryDto.getCountryPhoneCode(), actualCountryDto.getCountryPhoneCode());
        assertNotNull(actualCountryDto.getCreatedAt());
        assertNotNull(actualCountryDto.getCreatedBy());
    }

    @DisplayName("Test Update Country")
    @Test
    public void testUpdateCountry() {
        // Arrange
        CountryDto countryDto = CountryDto.builder()
                .countryName("Test Country")
                .locale("en_US")
                .localePreferred(true)
                .currencyCode("USD")
                .currencySymbol("$")
                .countryCode("COUNTRY1238")
                .countryGeoCode("GEO123")
                .countryPhoneCode(234)
                .build();

        // Act
        CountryDto newCountryDto = countryService.createCountry(countryDto);
        newCountryDto.setCountryName("Updated Country");
        newCountryDto.setLocale("fr_FR");
        newCountryDto.setLocalePreferred(false);
        newCountryDto.setCountryCode("COUNTRY456");
        newCountryDto.setCountryGeoCode("GEO456");
        newCountryDto.setCountryPhoneCode(567);
        newCountryDto.setLastModifiedBy(1L);
        CountryDto actualCountryDto = countryService.updateCountry(newCountryDto.getCountryId(), newCountryDto);

        // Assert
        assertNotNull(actualCountryDto);
        assertEquals("Updated Country", actualCountryDto.getCountryName());
        assertEquals("fr_FR", actualCountryDto.getLocale());
        assertFalse(actualCountryDto.isLocalePreferred());
        assertEquals("COUNTRY456", actualCountryDto.getCountryCode());
        assertEquals("GEO456", actualCountryDto.getCountryGeoCode());
        assertEquals(567, actualCountryDto.getCountryPhoneCode());
        assertNotNull(actualCountryDto.getLastModifiedAt());
        assertNotNull(actualCountryDto.getLastModifiedBy());
    }

    @DisplayName("Test Update Non Existing Country")
    @Test
    public void testUpdateNonExistingCountry() {
        // Arrange
        CountryDto countryDto = CountryDto.builder()
                .countryId(1)
                .countryName("Test Country")
                .locale("en_US")
                .localePreferred(true)
                .countryCode("COUNTRY12378")
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> countryService.updateCountry(countryDto.getCountryId(), countryDto));
    }

    @DisplayName("Test Get Country")
    @Test
    public void testGetCountry() {
        // Arrange
        CountryDto countryDto = CountryDto.builder()
                .countryName("Test Country")
                .locale("en_US")
                .localePreferred(true)
                .currencyCode("USD")
                .currencySymbol("$")
                .countryCode("COUNTRY12334")
                .countryGeoCode("GEO123")
                .countryPhoneCode(234)
                .build();

        // Act
        CountryDto newCountryDto = countryService.createCountry(countryDto);
        CountryDto actualCountryDto = countryService.getCountry(newCountryDto.getCountryId());

        // Assert
        assertNotNull(actualCountryDto);
        assertEquals(newCountryDto.getCountryName(), actualCountryDto.getCountryName());
        assertEquals(newCountryDto.getLocale(), actualCountryDto.getLocale());
        assertEquals(newCountryDto.isLocalePreferred(), actualCountryDto.isLocalePreferred());
        assertEquals(newCountryDto.getCountryCode(), actualCountryDto.getCountryCode());
        assertEquals(newCountryDto.getCountryGeoCode(), actualCountryDto.getCountryGeoCode());
        assertEquals(newCountryDto.getCountryPhoneCode(), actualCountryDto.getCountryPhoneCode());
    }

    @DisplayName("Test Get All Countries")
    @Test
    public void testGetAllCountries() {
        // Arrange
        CountryDto.CountryDtoBuilder countryDtoBuilder = CountryDto.builder()
                .countryName("Test Country")
                .locale("en_US")
                .localePreferred(true)
                .currencyCode("USD")
                .currencySymbol("$")
                .countryCode("COUNTRY123769")
                .countryGeoCode("GEO123")
                .countryPhoneCode(234);
          CountryDto countryDto = countryDtoBuilder.build();

        // Act
        countryService.createCountry(countryDto);
        countryDto.setCountryName("Test Country 2");
        countryService.createCountry(countryDtoBuilder.countryName("Test Country392").countryCode("COUNTRY1253769").build());
        List<CountryDto> actualCountryDtos = countryService.getAllCountries();

        // Assert
        assertNotNull(actualCountryDtos);
        assertEquals(2, actualCountryDtos.size());
    }

    @DisplayName("Test Delete Country")
    @Test
    public void testDeleteCountry() {
        // Arrange
        CountryDto countryDto = CountryDto.builder()
                .countryName("Test Country")
                .locale("en_US")
                .localePreferred(true)
                .countryCode("COUNTRY12306")
                .currencyCode("USD")
                .currencySymbol("$")
                .countryGeoCode("GEO123")
                .countryPhoneCode(234)
                .build();

        // Act
        CountryDto newCountryDto = countryService.createCountry(countryDto);
        RecordVoidRequest request = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        countryService.deleteCountry(newCountryDto.getCountryId(), request);

        // Assert
        CountryDto actualCountryDto = countryService.getCountry(newCountryDto.getCountryId());
        assertTrue(actualCountryDto.getVoided());
    }

    @DisplayName("Test Void Non Existing Country")
    @Test
    public void testVoidNonExistingCountry() {
        // Arrange
        RecordVoidRequest request = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> countryService.deleteCountry(1, request));
    }
} 