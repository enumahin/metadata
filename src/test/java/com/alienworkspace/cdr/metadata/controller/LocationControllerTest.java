package com.alienworkspace.cdr.metadata.controller;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.service.LocationService;
import com.alienworkspace.cdr.model.dto.metadata.LocationDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.alienworkspace.cdr.metadata.helpers.Constants.LOCATION_BASE_URL;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LocationService locationService;

    private LocationDto.LocationDtoBuilder locationDtoBuilder = LocationDto.builder();

    @BeforeEach
    public void setUp() {
        locationDtoBuilder
                .locationId(1)
                .locationName("Test Location")
                .locale("en_US")
                .localePreferred(true)
                .locationCode("LOC123")
                .locationGeoCode("GEO123")
                .locationPhoneCode(234);
    }

    @DisplayName("Test Create Location")
    @Test
    public void testCreateLocation() throws Exception {
        LocationDto locationDto = locationDtoBuilder.build();
        when(locationService.createLocation(locationDto)).thenReturn(locationDtoBuilder.locationId(1).build());
        ResultActions result = mockMvc.perform(
                post(LOCATION_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(locationDto))
        );
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.locationId").value(1))
                .andExpect(jsonPath("$.locationName").value(locationDto.getLocationName()))
                .andExpect(jsonPath("$.locale").value(locationDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(locationDto.isLocalePreferred()));
    }

    @DisplayName("Test Update Location")
    @Test
    public void testUpdateLocation() throws Exception {
        LocationDto.LocationDtoBuilder updatedLocationDto = locationDtoBuilder
                .locationName("Updated Location")
                .locale("fr_FR")
                .localePreferred(false);
        LocationDto updatedLocation = updatedLocationDto.locationId(1).build();
        when(locationService.updateLocation(1, updatedLocationDto.build())).thenReturn(updatedLocation);
        ResultActions result = mockMvc.perform(
                put(LOCATION_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedLocationDto.build()))
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationId").value(1))
                .andExpect(jsonPath("$.locationName").value(updatedLocation.getLocationName()))
                .andExpect(jsonPath("$.locale").value(updatedLocation.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(updatedLocation.isLocalePreferred()));
    }

    @DisplayName("Test Update Non Existing Location")
    @Test
    public void testUpdateNonExistingLocation() throws Exception {
        LocationDto locationDto = locationDtoBuilder.build();
        when(locationService.updateLocation(1, locationDto)).thenThrow(new ResourceNotFoundException("Location not found"));
        ResultActions result = mockMvc.perform(
                put(LOCATION_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(locationDto))
        );
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Location not found")));
    }

    @DisplayName("Test Get Location")
    @Test
    public void testGetLocation() throws Exception {
        LocationDto locationDto = locationDtoBuilder.build();
        when(locationService.getLocation(1)).thenReturn(locationDtoBuilder.locationId(1).build());
        ResultActions result = mockMvc.perform(
                get(LOCATION_BASE_URL + "/{id}", 1)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationId").value(1))
                .andExpect(jsonPath("$.locationName").value(locationDto.getLocationName()))
                .andExpect(jsonPath("$.locale").value(locationDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(locationDto.isLocalePreferred()));
    }

    @DisplayName("Test Get All Locations")
    @Test
    public void testGetAllLocations() throws Exception {
        List<LocationDto> locations = List.of(locationDtoBuilder.locationId(1).build());
        when(locationService.getAllLocations()).thenReturn(locations);
        ResultActions result = mockMvc.perform(
                get(LOCATION_BASE_URL)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].locationId").value(1))
                .andExpect(jsonPath("$.[0].locationName").value(locations.get(0).getLocationName()))
                .andExpect(jsonPath("$.[0].locale").value(locations.get(0).getLocale()))
                .andExpect(jsonPath("$.[0].localePreferred").value(locations.get(0).isLocalePreferred()));
    }

    @DisplayName("Test Delete Location")
    @Test
    public void testDeleteLocation() throws Exception {
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        doNothing().when(locationService).deleteLocation(1, recordVoidRequest);
        ResultActions result = mockMvc.perform(
                delete(LOCATION_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );
        result.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Test Delete NonExisting Location")
    @Test
    public void testDeleteNonExistingLocation() throws Exception {
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        doThrow(new ResourceNotFoundException("Location not found")).when(locationService).deleteLocation(1, recordVoidRequest);
        ResultActions result = mockMvc.perform(
                delete(LOCATION_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Location not found")));
    }
} 