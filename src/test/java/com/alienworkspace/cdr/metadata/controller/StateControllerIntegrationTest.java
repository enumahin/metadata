package com.alienworkspace.cdr.metadata.controller;

import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.Country;
import com.alienworkspace.cdr.metadata.model.State;
import com.alienworkspace.cdr.metadata.model.mapper.CountryMapper;
import com.alienworkspace.cdr.metadata.repository.*;
import com.alienworkspace.cdr.model.dto.metadata.StateDto;
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

import static com.alienworkspace.cdr.metadata.helpers.Constants.STATE_BASE_URL;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StateControllerIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountyRepository countyRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private LocationRepository locationRepository;

    Country country;

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
    }

    @DisplayName("Test Create State")
    @Test
    public void testCreateState() throws Exception {
        // Arrange
        StateDto stateDto = StateDto.builder()
                .stateName("Test State")
                .locale("en_US")
                .country(CountryMapper.INSTANCE.toDto(country))
                .localePreferred(true)
                .stateCode("ST123")
                .stateGeoCode("GEO123")
                .statePhoneCode(234)
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                post(STATE_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(stateDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.stateId").value(greaterThan(0)))
                .andExpect(jsonPath("$.stateName").value(stateDto.getStateName()))
                .andExpect(jsonPath("$.locale").value(stateDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(stateDto.isLocalePreferred()))
                .andExpect(jsonPath("$.stateCode").value(stateDto.getStateCode()))
                .andExpect(jsonPath("$.stateGeoCode").value(stateDto.getStateGeoCode()))
                .andExpect(jsonPath("$.statePhoneCode").value(stateDto.getStatePhoneCode()))
                .andExpect(jsonPath("$.createdAt").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.createdBy").value(CoreMatchers.notNullValue()));
    }

    @DisplayName("Test Create State with no state name throws exception")
    @Test
    public void testCreateStateWithNoStateName() throws Exception {
        // Arrange
        StateDto stateDto = StateDto.builder()
                .locale("en_US")
                .country(CountryMapper.INSTANCE.toDto(country))
                .localePreferred(true)
                .stateCode("ST123")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                post(STATE_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(stateDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorCode").value(500))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Failed to create")));
    }

    @DisplayName("Test Update State")
    @Test
    public void testUpdateState() throws Exception {
        // Arrange
        StateDto stateDto = StateDto.builder()
                .stateName("Updated State")
                .locale("fr_FR")
                .country(CountryMapper.INSTANCE.toDto(country))
                .localePreferred(false)
                .stateCode("ST456")
                .stateGeoCode("GEO456")
                .statePhoneCode(567)
                .build();

        State state = State.builder()
                .stateName("Test State")
                .locale("en_US")
                .country(country)
                .localePreferred(true)
                .stateCode("ST123")
                .stateGeoCode("GEO123")
                .statePhoneCode(234)
                .build();

        // Act
        State savedState = stateRepository.save(state);

        ResultActions result = mockMvc.perform(
                put(STATE_BASE_URL + "/{id}", savedState.getStateId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(stateDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stateId").value(savedState.getStateId()))
                .andExpect(jsonPath("$.stateName").value(stateDto.getStateName()))
                .andExpect(jsonPath("$.locale").value(stateDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(stateDto.isLocalePreferred()))
                .andExpect(jsonPath("$.stateCode").value(stateDto.getStateCode()))
                .andExpect(jsonPath("$.stateGeoCode").value(stateDto.getStateGeoCode()))
                .andExpect(jsonPath("$.statePhoneCode").value(stateDto.getStatePhoneCode()))
                .andExpect(jsonPath("$.lastModifiedAt").value(notNullValue()))
                .andExpect(jsonPath("$.lastModifiedBy").value(notNullValue()));
    }

    @DisplayName("Test Update non existing State")
    @Test
    public void testUpdateNonExistingState() throws Exception {
        // Arrange
        StateDto stateDto = StateDto.builder()
                .stateName("Updated State")
                .locale("fr_FR")
                .country(CountryMapper.INSTANCE.toDto(country))
                .localePreferred(false)
                .stateCode("ST456")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                put(STATE_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(stateDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("State not found")));
    }

    @DisplayName("Test Get State")
    @Test
    public void testGetState() throws Exception {
        // Arrange
        State state = State.builder()
                .stateName("Test State")
                .locale("en_US")
                .country(country)
                .localePreferred(true)
                .stateCode("ST123")
                .stateGeoCode("GEO123")
                .statePhoneCode(234)
                .build();

        // Act
        State saved = stateRepository.save(state);

        ResultActions result = mockMvc.perform(
                get(STATE_BASE_URL + "/{id}", saved.getStateId())
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stateId").value(saved.getStateId()))
                .andExpect(jsonPath("$.stateName").value(state.getStateName()))
                .andExpect(jsonPath("$.locale").value(state.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(state.isLocalePreferred()))
                .andExpect(jsonPath("$.stateCode").value(state.getStateCode()))
                .andExpect(jsonPath("$.stateGeoCode").value(state.getStateGeoCode()))
                .andExpect(jsonPath("$.statePhoneCode").value(state.getStatePhoneCode()));
    }

    @DisplayName("Test Get All States")
    @Test
    public void testGetAllStates() throws Exception {
        // Arrange
        List<State> states = List.of(
                State.builder()
                        .stateName("Test State 1")
                        .locale("en_US")
                        .localePreferred(true)
                        .stateCode("ST123")
                        .country(country)
                        .stateGeoCode("GEO123")
                        .statePhoneCode(234)
                        .build(),
                State.builder()
                        .stateName("Test State 2")
                        .locale("fr_FR")
                        .localePreferred(false)
                        .stateCode("ST456")
                        .country(country)
                        .stateGeoCode("GEO456")
                        .statePhoneCode(567)
                        .build());

        stateRepository.saveAll(states);

        // Act
        ResultActions result = mockMvc.perform(
                get(STATE_BASE_URL)
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].stateName").value(states.get(0).getStateName()))
                .andExpect(jsonPath("$.[0].locale").value(states.get(0).getLocale()))
                .andExpect(jsonPath("$.[0].localePreferred").value(states.get(0).isLocalePreferred()))
                .andExpect(jsonPath("$.[0].stateCode").value(states.get(0).getStateCode()))
                .andExpect(jsonPath("$.[0].stateGeoCode").value(states.get(0).getStateGeoCode()))
                .andExpect(jsonPath("$.[0].statePhoneCode").value(states.get(0).getStatePhoneCode()))
                .andExpect(jsonPath("$.[1].stateName").value(states.get(1).getStateName()))
                .andExpect(jsonPath("$.[1].locale").value(states.get(1).getLocale()))
                .andExpect(jsonPath("$.[1].localePreferred").value(states.get(1).isLocalePreferred()))
                .andExpect(jsonPath("$.[1].stateCode").value(states.get(1).getStateCode()))
                .andExpect(jsonPath("$.[1].stateGeoCode").value(states.get(1).getStateGeoCode()))
                .andExpect(jsonPath("$.[1].statePhoneCode").value(states.get(1).getStatePhoneCode()));
    }

    @DisplayName("Test Delete State")
    @Test
    public void testDeleteState() throws Exception {
        // Arrange
        State state = State.builder()
                .stateName("Test State")
                .locale("en_US")
                .localePreferred(true)
                .stateCode("ST123")
                .country(country)
                .stateGeoCode("GEO123")
                .statePhoneCode(234)
                .build();

        State saved = stateRepository.save(state);

        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                delete(STATE_BASE_URL + "/{id}", saved.getStateId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk());

        // Verify the state is voided
        State voidedState = stateRepository.findById(saved.getStateId()).orElseThrow();
        assert voidedState.isVoided();
        assert voidedState.getVoidReason().equals(recordVoidRequest.getVoidReason());
    }

    @DisplayName("Test Delete non existing State")
    @Test
    public void testDeleteNonExistingState() throws Exception {
        // Arrange
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                delete(STATE_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("State not found")));
    }
} 