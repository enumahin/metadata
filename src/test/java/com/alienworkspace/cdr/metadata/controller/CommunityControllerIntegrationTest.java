package com.alienworkspace.cdr.metadata.controller;

import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.*;
import com.alienworkspace.cdr.metadata.model.mapper.CityMapper;
import com.alienworkspace.cdr.metadata.repository.*;
import com.alienworkspace.cdr.metadata.service.CommunityService;
import com.alienworkspace.cdr.model.dto.metadata.CommunityDto;
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

import static com.alienworkspace.cdr.metadata.helpers.Constants.COMMUNITY_BASE_URL;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CommunityControllerIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private CountryRepository countryRepository;

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

        country = countryRepository.save(Country.builder().countryName("Test Country").countryCode("CN123")
                .currencyCode("USD").currencySymbol("$").build());
        state = stateRepository.save(State.builder().stateName("Test State").stateCode("ST123")
                .country(country).build());
        county = countyRepository.save(County.builder().countyName("Test County")
                .countyCode("COU123").state(state).build());
        city = cityRepository.save(City.builder().cityName("Test City").cityCode("CIT123")
                .cityGeoCode("GEO123").county(county).build());
    }

    @DisplayName("Test Create Community")
    @Test
    public void testCreateCommunity() throws Exception {
        // Arrange
        CommunityDto communityDto = CommunityDto.builder()
                .communityName("Test Community")
                .locale("en_US")
                .localePreferred(true)
                .city(CityMapper.INSTANCE.toDto(city))
                .communityCode("COM123")
                .communityGeoCode("GEO123")
                .communityPhoneCode(234)
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                post(COMMUNITY_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(communityDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.communityId").value(greaterThan(0)))
                .andExpect(jsonPath("$.communityName").value(communityDto.getCommunityName()))
                .andExpect(jsonPath("$.locale").value(communityDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(communityDto.isLocalePreferred()))
                .andExpect(jsonPath("$.communityCode").value(communityDto.getCommunityCode()))
                .andExpect(jsonPath("$.communityGeoCode").value(communityDto.getCommunityGeoCode()))
                .andExpect(jsonPath("$.communityPhoneCode").value(communityDto.getCommunityPhoneCode()))
                .andExpect(jsonPath("$.createdAt").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.createdBy").value(CoreMatchers.notNullValue()));
    }

    @DisplayName("Test Create Community with no community name throws exception")
    @Test
    public void testCreateCommunityWithNoCommunityName() throws Exception {
        // Arrange
        CommunityDto communityDto = CommunityDto.builder()
                .locale("en_US")
                .localePreferred(true)
                .communityCode("COM123")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                post(COMMUNITY_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(communityDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorCode").value(500))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Failed to create")));
    }

    @DisplayName("Test Update Community")
    @Test
    public void testUpdateCommunity() throws Exception {
        // Arrange
        CommunityDto communityDto = CommunityDto.builder()
                .communityName("Updated Community")
                .locale("fr_FR")
                .localePreferred(false)
                .city(CityMapper.INSTANCE.toDto(city))
                .communityCode("COM456")
                .communityGeoCode("GEO456")
                .communityPhoneCode(567)
                .build();

        Community community = Community.builder()
                .communityName("Test Community")
                .locale("en_US")
                .localePreferred(true)
                .city(city)
                .communityCode("COM123")
                .communityGeoCode("GEO123")
                .communityPhoneCode(234)
                .build();

        // Act
        Community savedCommunity = communityRepository.save(community);

        ResultActions result = mockMvc.perform(
                put(COMMUNITY_BASE_URL + "/{id}", savedCommunity.getCommunityId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(communityDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.communityId").value(savedCommunity.getCommunityId()))
                .andExpect(jsonPath("$.communityName").value(communityDto.getCommunityName()))
                .andExpect(jsonPath("$.locale").value(communityDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(communityDto.isLocalePreferred()))
                .andExpect(jsonPath("$.communityCode").value(communityDto.getCommunityCode()))
                .andExpect(jsonPath("$.communityGeoCode").value(communityDto.getCommunityGeoCode()))
                .andExpect(jsonPath("$.communityPhoneCode").value(communityDto.getCommunityPhoneCode()))
                .andExpect(jsonPath("$.lastModifiedAt").value(notNullValue()))
                .andExpect(jsonPath("$.lastModifiedBy").value(notNullValue()));
    }

    @DisplayName("Test Update non existing Community")
    @Test
    public void testUpdateNonExistingCommunity() throws Exception {
        // Arrange
        CommunityDto communityDto = CommunityDto.builder()
                .communityName("Updated Community")
                .locale("fr_FR")
                .localePreferred(false)
                .communityCode("COM456")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                put(COMMUNITY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(communityDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Community not found")));
    }

    @DisplayName("Test Get Community")
    @Test
    public void testGetCommunity() throws Exception {
        // Arrange
        Community community = Community.builder()
                .communityName("Test Community")
                .locale("en_US")
                .localePreferred(true)
                .city(city)
                .communityCode("COM123")
                .communityGeoCode("GEO123")
                .communityPhoneCode(234)
                .build();

        // Act
        Community saved = communityRepository.save(community);

        ResultActions result = mockMvc.perform(
                get(COMMUNITY_BASE_URL + "/{id}", saved.getCommunityId())
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.communityId").value(saved.getCommunityId()))
                .andExpect(jsonPath("$.communityName").value(community.getCommunityName()))
                .andExpect(jsonPath("$.locale").value(community.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(community.isLocalePreferred()))
                .andExpect(jsonPath("$.communityCode").value(community.getCommunityCode()))
                .andExpect(jsonPath("$.communityGeoCode").value(community.getCommunityGeoCode()))
                .andExpect(jsonPath("$.communityPhoneCode").value(community.getCommunityPhoneCode()));
    }

    @DisplayName("Test Get All Communities")
    @Test
    public void testGetAllCommunities() throws Exception {
        // Arrange
        List<Community> communities = List.of(
                Community.builder()
                        .communityName("Test Community 1")
                        .locale("en_US")
                        .localePreferred(true)
                        .city(city)
                        .communityCode("COM123")
                        .communityGeoCode("GEO123")
                        .communityPhoneCode(234)
                        .build(),
                Community.builder()
                        .communityName("Test Community 2")
                        .locale("fr_FR")
                        .localePreferred(false)
                        .city(city)
                        .communityCode("COM456")
                        .communityGeoCode("GEO456")
                        .communityPhoneCode(567)
                        .build());

        communityRepository.saveAll(communities);

        // Act
        ResultActions result = mockMvc.perform(
                get(COMMUNITY_BASE_URL)
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].communityName").value(communities.get(0).getCommunityName()))
                .andExpect(jsonPath("$.[0].locale").value(communities.get(0).getLocale()))
                .andExpect(jsonPath("$.[0].localePreferred").value(communities.get(0).isLocalePreferred()))
                .andExpect(jsonPath("$.[0].communityCode").value(communities.get(0).getCommunityCode()))
                .andExpect(jsonPath("$.[0].communityGeoCode").value(communities.get(0).getCommunityGeoCode()))
                .andExpect(jsonPath("$.[0].communityPhoneCode").value(communities.get(0).getCommunityPhoneCode()))
                .andExpect(jsonPath("$.[1].communityName").value(communities.get(1).getCommunityName()))
                .andExpect(jsonPath("$.[1].locale").value(communities.get(1).getLocale()))
                .andExpect(jsonPath("$.[1].localePreferred").value(communities.get(1).isLocalePreferred()))
                .andExpect(jsonPath("$.[1].communityCode").value(communities.get(1).getCommunityCode()))
                .andExpect(jsonPath("$.[1].communityGeoCode").value(communities.get(1).getCommunityGeoCode()))
                .andExpect(jsonPath("$.[1].communityPhoneCode").value(communities.get(1).getCommunityPhoneCode()));
    }

    @DisplayName("Test Delete Community")
    @Test
    public void testDeleteCommunity() throws Exception {
        // Arrange
        Community community = Community.builder()
                .communityName("Test Community")
                .locale("en_US")
                .localePreferred(true)
                .communityCode("COM123")
                .city(city)
                .communityGeoCode("GEO123")
                .communityPhoneCode(234)
                .build();

        Community saved = communityRepository.save(community);

        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                delete(COMMUNITY_BASE_URL + "/{id}", saved.getCommunityId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk());

        // Verify the community is voided
        Community voidedCommunity = communityRepository.findById(saved.getCommunityId()).orElseThrow();
        assert voidedCommunity.isVoided();
        assert voidedCommunity.getVoidReason().equals(recordVoidRequest.getVoidReason());
    }

    @DisplayName("Test Delete non existing Community")
    @Test
    public void testDeleteNonExistingCommunity() throws Exception {
        // Arrange
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                delete(COMMUNITY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Community not found")));
    }
} 