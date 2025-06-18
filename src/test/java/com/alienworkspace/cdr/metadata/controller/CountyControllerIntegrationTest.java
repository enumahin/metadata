package com.alienworkspace.cdr.metadata.controller;

import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.Country;
import com.alienworkspace.cdr.metadata.model.County;
import com.alienworkspace.cdr.metadata.model.State;
import com.alienworkspace.cdr.metadata.model.mapper.StateMapper;
import com.alienworkspace.cdr.metadata.repository.*;
import com.alienworkspace.cdr.model.dto.metadata.CountyDto;
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

import static com.alienworkspace.cdr.metadata.helpers.Constants.COUNTY_BASE_URL;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CountyControllerIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CountyRepository countyRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private LocationRepository locationRepository;

    Country country;
    State state;

    @BeforeEach
    void setUp() {
        locationRepository.deleteAllInBatch();
        communityRepository.deleteAllInBatch();
        cityRepository.deleteAllInBatch();
        countyRepository.deleteAllInBatch();
        stateRepository.deleteAllInBatch();
        countryRepository.deleteAllInBatch();

        country = countryRepository.save(Country.builder().countryName("Test Country").countryCode("CN123")
                .currencyCode("USD").currencySymbol("$").build());
        state = stateRepository.save(State.builder().stateName("Test State").stateCode("ST123").country(country).build());
    }

    @DisplayName("Test Create County")
    @Test
    public void testCreateCounty() throws Exception {
        // Arrange
        CountyDto countyDto = CountyDto.builder()
                .countyName("Test County")
                .locale("en_US")
                .state(StateMapper.INSTANCE.toDto(state))
                .localePreferred(true)
                .countyCode("CNTY123")
                .countyGeoCode("GEO123")
                .countyPhoneCode(234)
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                post(COUNTY_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(countyDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countyId").value(greaterThan(0)))
                .andExpect(jsonPath("$.countyName").value(countyDto.getCountyName()))
                .andExpect(jsonPath("$.locale").value(countyDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(countyDto.isLocalePreferred()))
                .andExpect(jsonPath("$.countyCode").value(countyDto.getCountyCode()))
                .andExpect(jsonPath("$.countyGeoCode").value(countyDto.getCountyGeoCode()))
                .andExpect(jsonPath("$.countyPhoneCode").value(countyDto.getCountyPhoneCode()))
                .andExpect(jsonPath("$.createdAt").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.createdBy").value(CoreMatchers.notNullValue()));
    }

    @DisplayName("Test Create County with no county name throws exception")
    @Test
    public void testCreateCountyWithNoCountyName() throws Exception {
        // Arrange
        CountyDto countyDto = CountyDto.builder()
                .locale("en_US")
                .state(StateMapper.INSTANCE.toDto(state))
                .localePreferred(true)
                .countyCode("CNTY123")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                post(COUNTY_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(countyDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorCode").value(500))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Failed to create")));
    }

    @DisplayName("Test Update County")
    @Test
    public void testUpdateCounty() throws Exception {
        // Arrange
        CountyDto countyDto = CountyDto.builder()
                .countyName("Updated County")
                .locale("fr_FR")
                .localePreferred(false)
                .countyCode("CNTY456")
                .state(StateMapper.INSTANCE.toDto(state))
                .countyGeoCode("GEO456")
                .countyPhoneCode(567)
                .build();

        County county = County.builder()
                .countyName("Test County")
                .locale("en_US")
                .state(state)
                .localePreferred(true)
                .countyCode("CNTY123")
                .countyGeoCode("GEO123")
                .countyPhoneCode(234)
                .build();

        // Act
        County savedCounty = countyRepository.save(county);

        ResultActions result = mockMvc.perform(
                put(COUNTY_BASE_URL + "/{id}", savedCounty.getCountyId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(countyDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countyId").value(savedCounty.getCountyId()))
                .andExpect(jsonPath("$.countyName").value(countyDto.getCountyName()))
                .andExpect(jsonPath("$.locale").value(countyDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(countyDto.isLocalePreferred()))
                .andExpect(jsonPath("$.countyCode").value(countyDto.getCountyCode()))
                .andExpect(jsonPath("$.countyGeoCode").value(countyDto.getCountyGeoCode()))
                .andExpect(jsonPath("$.countyPhoneCode").value(countyDto.getCountyPhoneCode()))
                .andExpect(jsonPath("$.lastModifiedAt").value(notNullValue()))
                .andExpect(jsonPath("$.lastModifiedBy").value(notNullValue()));
    }

    @DisplayName("Test Update non existing County")
    @Test
    public void testUpdateNonExistingCounty() throws Exception {
        // Arrange
        CountyDto countyDto = CountyDto.builder()
                .countyName("Updated County")
                .locale("fr_FR")
                .state(StateMapper.INSTANCE.toDto(state))
                .localePreferred(false)
                .countyCode("CNTY456")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                put(COUNTY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(countyDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("County not found")));
    }

    @DisplayName("Test Get County")
    @Test
    public void testGetCounty() throws Exception {
        // Arrange
        County county = County.builder()
                .countyName("Test County")
                .locale("en_US")
                .localePreferred(true)
                .state(state)
                .countyCode("CNTY123")
                .countyGeoCode("GEO123")
                .countyPhoneCode(234)
                .build();

        // Act
        County saved = countyRepository.save(county);

        ResultActions result = mockMvc.perform(
                get(COUNTY_BASE_URL + "/{id}", saved.getCountyId())
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countyId").value(saved.getCountyId()))
                .andExpect(jsonPath("$.countyName").value(county.getCountyName()))
                .andExpect(jsonPath("$.locale").value(county.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(county.isLocalePreferred()))
                .andExpect(jsonPath("$.countyCode").value(county.getCountyCode()))
                .andExpect(jsonPath("$.countyGeoCode").value(county.getCountyGeoCode()))
                .andExpect(jsonPath("$.countyPhoneCode").value(county.getCountyPhoneCode()));
    }

    @DisplayName("Test Get All Counties")
    @Test
    public void testGetAllCounties() throws Exception {
        // Arrange
        List<County> counties = List.of(
                County.builder()
                        .countyName("Test County 1")
                        .locale("en_US")
                        .localePreferred(true)
                        .state(state)
                        .countyCode("CNTY123")
                        .countyGeoCode("GEO123")
                        .countyPhoneCode(234)
                        .build(),
                County.builder()
                        .countyName("Test County 2")
                        .locale("fr_FR")
                        .state(state)
                        .localePreferred(false)
                        .countyCode("CNTY456")
                        .countyGeoCode("GEO456")
                        .countyPhoneCode(567)
                        .build());

        countyRepository.saveAll(counties);

        // Act
        ResultActions result = mockMvc.perform(
                get(COUNTY_BASE_URL)
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].countyName").value(counties.get(0).getCountyName()))
                .andExpect(jsonPath("$.[0].locale").value(counties.get(0).getLocale()))
                .andExpect(jsonPath("$.[0].localePreferred").value(counties.get(0).isLocalePreferred()))
                .andExpect(jsonPath("$.[0].countyCode").value(counties.get(0).getCountyCode()))
                .andExpect(jsonPath("$.[0].countyGeoCode").value(counties.get(0).getCountyGeoCode()))
                .andExpect(jsonPath("$.[0].countyPhoneCode").value(counties.get(0).getCountyPhoneCode()))
                .andExpect(jsonPath("$.[1].countyName").value(counties.get(1).getCountyName()))
                .andExpect(jsonPath("$.[1].locale").value(counties.get(1).getLocale()))
                .andExpect(jsonPath("$.[1].localePreferred").value(counties.get(1).isLocalePreferred()))
                .andExpect(jsonPath("$.[1].countyCode").value(counties.get(1).getCountyCode()))
                .andExpect(jsonPath("$.[1].countyGeoCode").value(counties.get(1).getCountyGeoCode()))
                .andExpect(jsonPath("$.[1].countyPhoneCode").value(counties.get(1).getCountyPhoneCode()));
    }

    @DisplayName("Test Delete County")
    @Test
    public void testDeleteCounty() throws Exception {
        // Arrange
        County county = County.builder()
                .countyName("Test County")
                .locale("en_US")
                .state(state)
                .localePreferred(true)
                .countyCode("CNTY123")
                .countyGeoCode("GEO123")
                .countyPhoneCode(234)
                .build();

        County saved = countyRepository.save(county);

        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                delete(COUNTY_BASE_URL + "/{id}", saved.getCountyId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk());

        // Verify the county is voided
        County voidedCounty = countyRepository.findById(saved.getCountyId()).orElseThrow();
        assert voidedCounty.isVoided();
        assert voidedCounty.getVoidReason().equals(recordVoidRequest.getVoidReason());
    }

    @DisplayName("Test Delete non existing County")
    @Test
    public void testDeleteNonExistingCounty() throws Exception {
        // Arrange
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                delete(COUNTY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("County not found")));
    }
} 