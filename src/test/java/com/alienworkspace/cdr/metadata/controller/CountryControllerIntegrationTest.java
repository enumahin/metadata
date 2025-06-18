package com.alienworkspace.cdr.metadata.controller;

import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.Country;
import com.alienworkspace.cdr.metadata.repository.*;
import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.alienworkspace.cdr.metadata.helpers.Constants.COUNTRY_BASE_URL;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CountryControllerIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

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
    public void testCreateCountry() throws Exception {
        // Arrange
        CountryDto countryDto = CountryDto.builder()
                .countryName("Test Country")
                .locale("en_US")
                .localePreferred(true)
                .countryCode("CN123")
                .countryPhoneCode(234)
                .currencyCode("USD")
                .currencyName("US Dollar")
                .currencySymbol("$")
                .countryGeoCode("GEO123")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                post(COUNTRY_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(countryDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countryId").value(greaterThan(0)))
                .andExpect(jsonPath("$.countryName").value(countryDto.getCountryName()))
                .andExpect(jsonPath("$.locale").value(countryDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(countryDto.isLocalePreferred()))
                .andExpect(jsonPath("$.countryCode").value(countryDto.getCountryCode()))
                .andExpect(jsonPath("$.countryPhoneCode").value(countryDto.getCountryPhoneCode()))
                .andExpect(jsonPath("$.currencyCode").value(countryDto.getCurrencyCode()))
                .andExpect(jsonPath("$.currencyName").value(countryDto.getCurrencyName()))
                .andExpect(jsonPath("$.currencySymbol").value(countryDto.getCurrencySymbol()))
                .andExpect(jsonPath("$.countryGeoCode").value(countryDto.getCountryGeoCode()))
                .andExpect(jsonPath("$.createdAt").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.createdBy").value(CoreMatchers.notNullValue()));
    }

    @DisplayName("Test Create Country with no country name throws exception")
    @Test
    public void testCreateCountryWithNoCountryName() throws Exception {
        // Arrange
        CountryDto countryDto = CountryDto.builder()
                .locale("en_US")
                .localePreferred(true)
                .countryCode("CN123")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                post(COUNTRY_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(countryDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorCode").value(500))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString(
                        "Column 'country_name' cannot be null")));
    }

    @DisplayName("Test Update Country")
    @Test
    public void testUpdateCountry() throws Exception {
        // Arrange
        CountryDto countryDto = CountryDto.builder()
                .countryName("Updated Country")
                .locale("fr_FR")
                .localePreferred(false)
                .countryCode("CN456")
                .countryPhoneCode(567)
                .currencyCode("EUR")
                .currencyName("Euro")
                .currencySymbol("€")
                .countryGeoCode("GEO456")
                .build();

        Country country = Country.builder()
                .countryName("Test Country")
                .locale("en_US")
                .localePreferred(true)
                .countryCode("CN123")
                .countryPhoneCode(234)
                .currencyCode("USD")
                .currencyName("US Dollar")
                .currencySymbol("$")
                .countryGeoCode("GEO123")
                .build();

        // Act
        Country savedCountry = countryRepository.save(country);

        ResultActions result = mockMvc.perform(
                put(COUNTRY_BASE_URL + "/{id}", savedCountry.getCountryId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(countryDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryId").value(savedCountry.getCountryId()))
                .andExpect(jsonPath("$.countryName").value(countryDto.getCountryName()))
                .andExpect(jsonPath("$.locale").value(countryDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(countryDto.isLocalePreferred()))
                .andExpect(jsonPath("$.countryCode").value(countryDto.getCountryCode()))
                .andExpect(jsonPath("$.countryPhoneCode").value(countryDto.getCountryPhoneCode()))
                .andExpect(jsonPath("$.currencyCode").value(countryDto.getCurrencyCode()))
                .andExpect(jsonPath("$.currencyName").value(countryDto.getCurrencyName()))
                .andExpect(jsonPath("$.currencySymbol").value(countryDto.getCurrencySymbol()))
                .andExpect(jsonPath("$.countryGeoCode").value(countryDto.getCountryGeoCode()))
                .andExpect(jsonPath("$.lastModifiedAt").value(notNullValue()))
                .andExpect(jsonPath("$.lastModifiedBy").value(notNullValue()));
    }

    @DisplayName("Test Update non existing Country")
    @Test
    public void testUpdateNonExistingCountry() throws Exception {
        // Arrange
        CountryDto countryDto = CountryDto.builder()
                .countryName("Updated Country")
                .locale("fr_FR")
                .localePreferred(false)
                .countryCode("CN456")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                put(COUNTRY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(countryDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Country not found")));
    }

    @DisplayName("Test Get Country")
    @Test
    public void testGetCountry() throws Exception {
        // Arrange
        Country country = Country.builder()
                .countryName("Test Country")
                .locale("en_US")
                .localePreferred(true)
                .countryCode("CN123")
                .countryPhoneCode(234)
                .currencyCode("USD")
                .currencyName("US Dollar")
                .currencySymbol("$")
                .countryGeoCode("GEO123")
                .build();

        // Act
        Country saved = countryRepository.save(country);

        ResultActions result = mockMvc.perform(
                get(COUNTRY_BASE_URL + "/{id}", saved.getCountryId())
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryId").value(saved.getCountryId()))
                .andExpect(jsonPath("$.countryName").value(country.getCountryName()))
                .andExpect(jsonPath("$.locale").value(country.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(country.isLocalePreferred()))
                .andExpect(jsonPath("$.countryCode").value(country.getCountryCode()))
                .andExpect(jsonPath("$.countryPhoneCode").value(country.getCountryPhoneCode()))
                .andExpect(jsonPath("$.currencyCode").value(country.getCurrencyCode()))
                .andExpect(jsonPath("$.currencyName").value(country.getCurrencyName()))
                .andExpect(jsonPath("$.currencySymbol").value(country.getCurrencySymbol()))
                .andExpect(jsonPath("$.countryGeoCode").value(country.getCountryGeoCode()));
    }

    @DisplayName("Test Get All Countries")
    @Test
    public void testGetAllCountries() throws Exception {
        // Arrange
        List<Country> countries = List.of(
                Country.builder()
                        .countryName("Test Country 1")
                        .locale("en_US")
                        .localePreferred(true)
                        .countryCode("CN123")
                        .countryPhoneCode(234)
                        .currencyCode("USD")
                        .currencyName("US Dollar")
                        .currencySymbol("$")
                        .countryGeoCode("GEO123")
                        .build(),
                Country.builder()
                        .countryName("Test Country 2")
                        .locale("fr_FR")
                        .localePreferred(false)
                        .countryCode("CN456")
                        .countryPhoneCode(567)
                        .currencyCode("EUR")
                        .currencyName("Euro")
                        .currencySymbol("€")
                        .countryGeoCode("GEO456")
                        .build());

        countryRepository.saveAll(countries);

        // Act
        ResultActions result = mockMvc.perform(
                get(COUNTRY_BASE_URL)
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].countryName").value(countries.get(0).getCountryName()))
                .andExpect(jsonPath("$.[0].locale").value(countries.get(0).getLocale()))
                .andExpect(jsonPath("$.[0].localePreferred").value(countries.get(0).isLocalePreferred()))
                .andExpect(jsonPath("$.[0].countryCode").value(countries.get(0).getCountryCode()))
                .andExpect(jsonPath("$.[0].countryPhoneCode").value(countries.get(0).getCountryPhoneCode()))
                .andExpect(jsonPath("$.[0].currencyCode").value(countries.get(0).getCurrencyCode()))
                .andExpect(jsonPath("$.[0].currencyName").value(countries.get(0).getCurrencyName()))
                .andExpect(jsonPath("$.[0].currencySymbol").value(countries.get(0).getCurrencySymbol()))
                .andExpect(jsonPath("$.[0].countryGeoCode").value(countries.get(0).getCountryGeoCode()))
                .andExpect(jsonPath("$.[1].countryName").value(countries.get(1).getCountryName()))
                .andExpect(jsonPath("$.[1].locale").value(countries.get(1).getLocale()))
                .andExpect(jsonPath("$.[1].localePreferred").value(countries.get(1).isLocalePreferred()))
                .andExpect(jsonPath("$.[1].countryCode").value(countries.get(1).getCountryCode()))
                .andExpect(jsonPath("$.[1].countryPhoneCode").value(countries.get(1).getCountryPhoneCode()))
                .andExpect(jsonPath("$.[1].currencyCode").value(countries.get(1).getCurrencyCode()))
                .andExpect(jsonPath("$.[1].currencyName").value(countries.get(1).getCurrencyName()))
                .andExpect(jsonPath("$.[1].currencySymbol").value(countries.get(1).getCurrencySymbol()))
                .andExpect(jsonPath("$.[1].countryGeoCode").value(countries.get(1).getCountryGeoCode()));
    }

    @DisplayName("Test Delete Country")
    @Test
    public void testDeleteCountry() throws Exception {
        // Arrange
        Country country = Country.builder()
                .countryName("Test Country")
                .locale("en_US")
                .localePreferred(true)
                .countryCode("CN123")
                .countryPhoneCode(234)
                .currencyCode("USD")
                .currencyName("US Dollar")
                .currencySymbol("$")
                .countryGeoCode("GEO123")
                .build();

        Country saved = countryRepository.save(country);

        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                delete(COUNTRY_BASE_URL + "/{id}", saved.getCountryId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk());

        // Verify the country is voided
        Country voidedCountry = countryRepository.findById(saved.getCountryId()).orElseThrow();
        assert voidedCountry.isVoided();
        assert voidedCountry.getVoidReason().equals(recordVoidRequest.getVoidReason());
    }

    @DisplayName("Test Delete non existing Country")
    @Test
    public void testDeleteNonExistingCountry() throws Exception {
        // Arrange
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                delete(COUNTRY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Country not found")));
    }

    @DisplayName("Test Get Country by Country Code")
    @Test
    public void testGetCountryByCountryCode() throws Exception {
        // Arrange
        Country country = Country.builder()
                .countryName("Test Country")
                .locale("en_US")
                .localePreferred(true)
                .countryCode("CN123")
                .countryPhoneCode(234)
                .currencyCode("USD")
                .currencyName("US Dollar")
                .currencySymbol("$")
                .countryGeoCode("GEO123")
                .build();

        // Act
        countryRepository.save(country);

        ResultActions result = mockMvc.perform(
                get(COUNTRY_BASE_URL + "/code/{code}", country.getCountryCode())
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryName").value(country.getCountryName()))
                .andExpect(jsonPath("$.locale").value(country.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(country.isLocalePreferred()))
                .andExpect(jsonPath("$.countryCode").value(country.getCountryCode()))
                .andExpect(jsonPath("$.countryPhoneCode").value(country.getCountryPhoneCode()))
                .andExpect(jsonPath("$.currencyCode").value(country.getCurrencyCode()))
                .andExpect(jsonPath("$.currencyName").value(country.getCurrencyName()))
                .andExpect(jsonPath("$.currencySymbol").value(country.getCurrencySymbol()))
                .andExpect(jsonPath("$.countryGeoCode").value(country.getCountryGeoCode()));
    }

    @DisplayName("Test Get Country by non existing Country Code")
    @Test
    public void testGetCountryByNonExistingCountryCode() throws Exception {
        // Act
        ResultActions result = mockMvc.perform(
                get(COUNTRY_BASE_URL + "/code/{code}", "NONEXISTENT")
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Country not found")));
    }
} 