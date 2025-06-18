package com.alienworkspace.cdr.metadata.controller;

import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.Country;
import com.alienworkspace.cdr.metadata.model.County;
import com.alienworkspace.cdr.metadata.model.Location;
import com.alienworkspace.cdr.metadata.model.State;
import com.alienworkspace.cdr.metadata.model.City;
import com.alienworkspace.cdr.metadata.model.Community;
import com.alienworkspace.cdr.metadata.model.mapper.CommunityMapper;
import com.alienworkspace.cdr.metadata.repository.CountryRepository;
import com.alienworkspace.cdr.metadata.repository.CountyRepository;
import com.alienworkspace.cdr.metadata.repository.LocationRepository;
import com.alienworkspace.cdr.metadata.repository.StateRepository;
import com.alienworkspace.cdr.metadata.repository.CityRepository;
import com.alienworkspace.cdr.metadata.repository.CommunityRepository;
import com.alienworkspace.cdr.model.dto.metadata.LocationDto;
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

import static com.alienworkspace.cdr.metadata.helpers.Constants.LOCATION_BASE_URL;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LocationControllerIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

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

    Country country;
    State state;
    County county;
    City city;
    Community community;

    @BeforeEach
    void setUp() {
        locationRepository.deleteAll();
        communityRepository.deleteAll();
        cityRepository.deleteAll();
        countyRepository.deleteAll();
        stateRepository.deleteAll();
        countryRepository.deleteAll();

        // Create required dependencies
        country = createCountry();
        state = createState(country);
        county = createCounty(state);
        city = createCity(county);
        community = createCommunity(city);
    }

    @DisplayName("Test Create Location")
    @Test
    public void testCreateLocation() throws Exception {
        // Arrange
        LocationDto locationDto = LocationDto.builder()
                .locationName("Test Location")
                .locale("en_US")
                .localePreferred(true)
                .locationCode("LOC123")
                .locationGeoCode("GEO123")
                .locationPhoneCode(234)
                .community(CommunityMapper.INSTANCE.toDto(community))
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                post(LOCATION_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(locationDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.locationId").value(greaterThan(0)))
                .andExpect(jsonPath("$.locationName").value(locationDto.getLocationName()))
                .andExpect(jsonPath("$.locale").value(locationDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(locationDto.isLocalePreferred()))
                .andExpect(jsonPath("$.locationCode").value(locationDto.getLocationCode()))
                .andExpect(jsonPath("$.locationGeoCode").value(locationDto.getLocationGeoCode()))
                .andExpect(jsonPath("$.locationPhoneCode").value(locationDto.getLocationPhoneCode()))
                .andExpect(jsonPath("$.createdAt").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.createdBy").value(CoreMatchers.notNullValue()));
    }

    @DisplayName("Test Create Location with no location name throws exception")
    @Test
    public void testCreateLocationWithNoLocationName() throws Exception {
        // Arrange
        LocationDto locationDto = LocationDto.builder()
                .locale("en_US")
                .localePreferred(true)
                .community(CommunityMapper.INSTANCE.toDto(community))
                .locationCode("LOC123")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                post(LOCATION_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(locationDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorCode").value(500))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Failed to create")));
    }

    @DisplayName("Test Update Location")
    @Test
    public void testUpdateLocation() throws Exception {
        // Arrange
        LocationDto locationDto = LocationDto.builder()
                .locationName("Updated Location")
                .locale("fr_FR")
                .localePreferred(false)
                .community(CommunityMapper.INSTANCE.toDto(community))
                .locationCode("LOC456")
                .locationGeoCode("GEO456")
                .locationPhoneCode(567)
                .build();

        Location location = Location.builder()
                .locationName("Test Location")
                .locale("en_US")
                .localePreferred(true)
                .locationCode("LOC123")
                .locationGeoCode("GEO123")
                .locationPhoneCode(234)
                .community(community)
                .build();

        // Act
        Location savedLocation = locationRepository.save(location);

        ResultActions result = mockMvc.perform(
                put(LOCATION_BASE_URL + "/{id}", savedLocation.getLocationId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(locationDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationId").value(savedLocation.getLocationId()))
                .andExpect(jsonPath("$.locationName").value(locationDto.getLocationName()))
                .andExpect(jsonPath("$.locale").value(locationDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(locationDto.isLocalePreferred()))
                .andExpect(jsonPath("$.locationCode").value(locationDto.getLocationCode()))
                .andExpect(jsonPath("$.locationGeoCode").value(locationDto.getLocationGeoCode()))
                .andExpect(jsonPath("$.locationPhoneCode").value(locationDto.getLocationPhoneCode()))
                .andExpect(jsonPath("$.lastModifiedAt").value(notNullValue()))
                .andExpect(jsonPath("$.lastModifiedBy").value(notNullValue()));
    }

    @DisplayName("Test Update non existing Location")
    @Test
    public void testUpdateNonExistingLocation() throws Exception {
        // Arrange
        LocationDto locationDto = LocationDto.builder()
                .locationName("Updated Location")
                .locale("fr_FR")
                .community(CommunityMapper.INSTANCE.toDto(community))
                .localePreferred(false)
                .locationCode("LOC456")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                put(LOCATION_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(locationDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Location not found")));
    }

    @DisplayName("Test Get Location")
    @Test
    public void testGetLocation() throws Exception {
        // Arrange
        Location location = Location.builder()
                .locationName("Test Location")
                .locale("en_US")
                .localePreferred(true)
                .locationCode("LOC123")
                .locationGeoCode("GEO123")
                .locationPhoneCode(234)
                .community(community)
                .build();

        // Act
        Location saved = locationRepository.save(location);

        ResultActions result = mockMvc.perform(
                get(LOCATION_BASE_URL + "/{id}", saved.getLocationId())
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationId").value(saved.getLocationId()))
                .andExpect(jsonPath("$.locationName").value(location.getLocationName()))
                .andExpect(jsonPath("$.locale").value(location.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(location.isLocalePreferred()))
                .andExpect(jsonPath("$.locationCode").value(location.getLocationCode()))
                .andExpect(jsonPath("$.locationGeoCode").value(location.getLocationGeoCode()))
                .andExpect(jsonPath("$.locationPhoneCode").value(location.getLocationPhoneCode()));
    }

    @DisplayName("Test Get All Locations")
    @Test
    public void testGetAllLocations() throws Exception {
        // Arrange
        List<Location> locations = List.of(
                Location.builder()
                        .locationName("Test Location 1")
                        .locale("en_US")
                        .localePreferred(true)
                        .locationCode("LOC123")
                        .locationGeoCode("GEO123")
                        .locationPhoneCode(234)
                        .community(community)
                        .build(),
                Location.builder()
                        .locationName("Test Location 2")
                        .locale("fr_FR")
                        .localePreferred(false)
                        .locationCode("LOC456")
                        .locationGeoCode("GEO456")
                        .locationPhoneCode(567)
                        .community(community)
                        .build());

        locationRepository.saveAll(locations);

        // Act
        ResultActions result = mockMvc.perform(
                get(LOCATION_BASE_URL)
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].locationName").value(locations.get(0).getLocationName()))
                .andExpect(jsonPath("$.[0].locale").value(locations.get(0).getLocale()))
                .andExpect(jsonPath("$.[0].localePreferred").value(locations.get(0).isLocalePreferred()))
                .andExpect(jsonPath("$.[0].locationCode").value(locations.get(0).getLocationCode()))
                .andExpect(jsonPath("$.[0].locationGeoCode").value(locations.get(0).getLocationGeoCode()))
                .andExpect(jsonPath("$.[0].locationPhoneCode").value(locations.get(0).getLocationPhoneCode()))
                .andExpect(jsonPath("$.[1].locationName").value(locations.get(1).getLocationName()))
                .andExpect(jsonPath("$.[1].locale").value(locations.get(1).getLocale()))
                .andExpect(jsonPath("$.[1].localePreferred").value(locations.get(1).isLocalePreferred()))
                .andExpect(jsonPath("$.[1].locationCode").value(locations.get(1).getLocationCode()))
                .andExpect(jsonPath("$.[1].locationGeoCode").value(locations.get(1).getLocationGeoCode()))
                .andExpect(jsonPath("$.[1].locationPhoneCode").value(locations.get(1).getLocationPhoneCode()));
    }

    @DisplayName("Test Delete Location")
    @Test
    public void testDeleteLocation() throws Exception {
        // Arrange
        Location location = Location.builder()
                .locationName("Test Location")
                .locale("en_US")
                .localePreferred(true)
                .locationCode("LOC123")
                .locationGeoCode("GEO123")
                .locationPhoneCode(234)
                .community(community)
                .build();

        Location saved = locationRepository.save(location);

        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                delete(LOCATION_BASE_URL + "/{id}", saved.getLocationId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk());

        // Verify the location is voided
        Location voidedLocation = locationRepository.findById(saved.getLocationId()).orElseThrow();
        assert voidedLocation.isVoided();
        assert voidedLocation.getVoidReason().equals(recordVoidRequest.getVoidReason());
    }

    @DisplayName("Test Delete non existing Location")
    @Test
    public void testDeleteNonExistingLocation() throws Exception {
        // Arrange
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                delete(LOCATION_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Location not found")));
    }

    private Country createCountry() {
        Country country = Country.builder()
                .countryName("Test Country")
                .countryCode("TC")
                .currencySymbol("$")
                .countryPhoneCode(123)
                .locale("en")
                .localePreferred(true)
                .build();
        return countryRepository.save(country);
    }

    private State createState(Country country) {
        State state = State.builder()
                .stateName("Test State")
                .stateCode("TS")
                .statePhoneCode(123)
                .locale("en")
                .localePreferred(true)
                .country(country)
                .build();
        return stateRepository.save(state);
    }

    private County createCounty(State state) {
        County county = County.builder()
                .countyName("Test County")
                .countyCode("TC")
                .countyPhoneCode(123)
                .locale("en")
                .localePreferred(true)
                .state(state)
                .build();
        return countyRepository.save(county);
    }

    private City createCity(County county) {
        City city = City.builder()
                .cityName("Test City")
                .cityCode("TC")
                .cityPhoneCode(123)
                .locale("en")
                .localePreferred(true)
                .county(county)
                .build();
        return cityRepository.save(city);
    }

    private Community createCommunity(City city) {
        Community community = Community.builder()
                .communityName("Test Community")
                .communityCode("TC")
                .communityPhoneCode(123)
                .locale("en")
                .localePreferred(true)
                .city(city)
                .build();
        return communityRepository.save(community);
    }
} 