package com.alienworkspace.cdr.metadata.controller;

import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.City;
import com.alienworkspace.cdr.metadata.model.Country;
import com.alienworkspace.cdr.metadata.model.County;
import com.alienworkspace.cdr.metadata.model.State;
import com.alienworkspace.cdr.metadata.model.mapper.CountyMapper;
import com.alienworkspace.cdr.metadata.repository.*;
import com.alienworkspace.cdr.metadata.service.CityService;
import com.alienworkspace.cdr.model.dto.metadata.CityDto;
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

import static com.alienworkspace.cdr.metadata.helpers.Constants.CITY_BASE_URL;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CityControllerIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountyRepository countyRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private LocationRepository locationRepository;

    Country country;
    State state;
    County county;

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
        state = stateRepository.save(State.builder().stateName("Test State").stateCode("ST123")
                .country(country).build());
        county = countyRepository.save(County.builder().countyName("Test County")
                .countyCode("COU123").state(state).build());
    }

    @DisplayName("Test Create City")
    @Test
    public void testCreateCity() throws Exception {
        // Arrange
        CityDto cityDto = CityDto.builder()
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .county(CountyMapper.INSTANCE.toDto(county))
                .cityCode("CITY123")
                .cityGeoCode("GEO123")
                .cityPhoneCode(234)
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                post(CITY_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cityDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cityId").value(greaterThan(0)))
                .andExpect(jsonPath("$.cityName").value(cityDto.getCityName()))
                .andExpect(jsonPath("$.locale").value(cityDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(cityDto.isLocalePreferred()))
                .andExpect(jsonPath("$.cityCode").value(cityDto.getCityCode()))
                .andExpect(jsonPath("$.cityGeoCode").value(cityDto.getCityGeoCode()))
                .andExpect(jsonPath("$.cityPhoneCode").value(cityDto.getCityPhoneCode()))
                .andExpect(jsonPath("$.createdAt").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.createdBy").value(CoreMatchers.notNullValue()));
    }

    @DisplayName("Test Create City with no city name throws exception")
    @Test
    public void testCreateCityWithNoCityName() throws Exception {
        // Arrange
        CityDto cityDto = CityDto.builder()
                .locale("en_US")
                .county(CountyMapper.INSTANCE.toDto(county))
                .localePreferred(true)
                .cityCode("CITY123")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                post(CITY_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cityDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorCode").value(500))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Failed to create")));
    }

    @DisplayName("Test Update City")
    @Test
    public void testUpdateCity() throws Exception {
        // Arrange
        CityDto cityDto = CityDto.builder()
                .cityName("Updated City")
                .locale("fr_FR")
                .localePreferred(false)
                .cityCode("CITY456")
                .county(CountyMapper.INSTANCE.toDto(county))
                .cityGeoCode("GEO456")
                .cityPhoneCode(567)
                .build();

        City city = City.builder()
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .cityCode("CITY123")
                .county(county)
                .cityGeoCode("GEO123")
                .cityPhoneCode(234)
                .build();

        // Act
        City savedCity = cityRepository.save(city);

        ResultActions result = mockMvc.perform(
                put(CITY_BASE_URL + "/{id}", savedCity.getCityId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cityDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityId").value(savedCity.getCityId()))
                .andExpect(jsonPath("$.cityName").value(cityDto.getCityName()))
                .andExpect(jsonPath("$.locale").value(cityDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(cityDto.isLocalePreferred()))
                .andExpect(jsonPath("$.cityCode").value(cityDto.getCityCode()))
                .andExpect(jsonPath("$.cityGeoCode").value(cityDto.getCityGeoCode()))
                .andExpect(jsonPath("$.cityPhoneCode").value(cityDto.getCityPhoneCode()))
                .andExpect(jsonPath("$.lastModifiedAt").value(notNullValue()))
                .andExpect(jsonPath("$.lastModifiedBy").value(notNullValue()));
    }

    @DisplayName("Test Update non existing City")
    @Test
    public void testUpdateNonExistingCity() throws Exception {
        // Arrange
        CityDto cityDto = CityDto.builder()
                .cityName("Updated City")
                .locale("fr_FR")
                .localePreferred(false)
                .cityCode("CITY456")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                put(CITY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cityDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("City not found")));
    }

    @DisplayName("Test Get City")
    @Test
    public void testGetCity() throws Exception {
        // Arrange
        City city = City.builder()
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .cityCode("CITY123")
                .county(county)
                .cityGeoCode("GEO123")
                .cityPhoneCode(234)
                .build();

        // Act
        City saved = cityRepository.save(city);

        ResultActions result = mockMvc.perform(
                get(CITY_BASE_URL + "/{id}", saved.getCityId())
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityId").value(saved.getCityId()))
                .andExpect(jsonPath("$.cityName").value(city.getCityName()))
                .andExpect(jsonPath("$.locale").value(city.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(city.isLocalePreferred()))
                .andExpect(jsonPath("$.cityCode").value(city.getCityCode()))
                .andExpect(jsonPath("$.cityGeoCode").value(city.getCityGeoCode()))
                .andExpect(jsonPath("$.cityPhoneCode").value(city.getCityPhoneCode()));
    }

    @DisplayName("Test Get All Cities")
    @Test
    public void testGetAllCities() throws Exception {
        // Arrange
        List<City> cities = List.of(
                City.builder()
                        .cityName("Test City 1")
                        .locale("en_US")
                        .localePreferred(true)
                        .county(county)
                        .cityCode("CITY123")
                        .cityGeoCode("GEO123")
                        .cityPhoneCode(234)
                        .build(),
                City.builder()
                        .cityName("Test City 2")
                        .locale("fr_FR")
                        .county(county)
                        .localePreferred(false)
                        .cityCode("CITY456")
                        .cityGeoCode("GEO456")
                        .cityPhoneCode(567)
                        .build());

        cityRepository.saveAll(cities);

        // Act
        ResultActions result = mockMvc.perform(
                get(CITY_BASE_URL)
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].cityName").value(cities.get(0).getCityName()))
                .andExpect(jsonPath("$.[0].locale").value(cities.get(0).getLocale()))
                .andExpect(jsonPath("$.[0].localePreferred").value(cities.get(0).isLocalePreferred()))
                .andExpect(jsonPath("$.[0].cityCode").value(cities.get(0).getCityCode()))
                .andExpect(jsonPath("$.[0].cityGeoCode").value(cities.get(0).getCityGeoCode()))
                .andExpect(jsonPath("$.[0].cityPhoneCode").value(cities.get(0).getCityPhoneCode()))
                .andExpect(jsonPath("$.[1].cityName").value(cities.get(1).getCityName()))
                .andExpect(jsonPath("$.[1].locale").value(cities.get(1).getLocale()))
                .andExpect(jsonPath("$.[1].localePreferred").value(cities.get(1).isLocalePreferred()))
                .andExpect(jsonPath("$.[1].cityCode").value(cities.get(1).getCityCode()))
                .andExpect(jsonPath("$.[1].cityGeoCode").value(cities.get(1).getCityGeoCode()))
                .andExpect(jsonPath("$.[1].cityPhoneCode").value(cities.get(1).getCityPhoneCode()));
    }

    @DisplayName("Test Delete City")
    @Test
    public void testDeleteCity() throws Exception {
        // Arrange
        City city = City.builder()
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .county(county)
                .cityCode("CITY123")
                .cityGeoCode("GEO123")
                .cityPhoneCode(234)
                .build();

        City saved = cityRepository.save(city);

        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                delete(CITY_BASE_URL + "/{id}", saved.getCityId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk());

        // Verify the city is voided
        City voidedCity = cityRepository.findById(saved.getCityId()).orElseThrow();
        assert voidedCity.isVoided();
        assert voidedCity.getVoidReason().equals(recordVoidRequest.getVoidReason());
    }

    @DisplayName("Test Delete non existing City")
    @Test
    public void testDeleteNonExistingCity() throws Exception {
        // Arrange
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                delete(CITY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("City not found")));
    }
} 