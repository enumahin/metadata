package com.alienworkspace.cdr.metadata.controller;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.service.CityService;
import com.alienworkspace.cdr.model.dto.metadata.CityDto;
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

import static com.alienworkspace.cdr.metadata.helpers.Constants.CITY_BASE_URL;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CityController.class)
public class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CityService cityService;

    private CityDto.CityDtoBuilder cityDtoBuilder = CityDto.builder();

    @BeforeEach
    public void setUp() {
        cityDtoBuilder
                .cityId(1)
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .cityCode("CITY123")
                .cityGeoCode("GEO123")
                .cityPhoneCode(234);
    }

    @DisplayName("Test Create City")
    @Test
    public void testCreateCity() throws Exception {
        CityDto cityDto = cityDtoBuilder.build();
        when(cityService.createCity(cityDto)).thenReturn(cityDtoBuilder.cityId(1).build());
        ResultActions result = mockMvc.perform(
                post(CITY_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cityDto))
        );
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cityId").value(1))
                .andExpect(jsonPath("$.cityName").value(cityDto.getCityName()))
                .andExpect(jsonPath("$.locale").value(cityDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(cityDto.isLocalePreferred()));
    }

    @DisplayName("Test Update City")
    @Test
    public void testUpdateCity() throws Exception {
        CityDto.CityDtoBuilder updatedCityDto = cityDtoBuilder
                .cityName("Updated City")
                .locale("fr_FR")
                .localePreferred(false);
        CityDto updatedCity = updatedCityDto.cityId(1).build();
        when(cityService.updateCity(1, updatedCityDto.build())).thenReturn(updatedCity);
        ResultActions result = mockMvc.perform(
                put(CITY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedCityDto.build()))
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityId").value(1))
                .andExpect(jsonPath("$.cityName").value(updatedCity.getCityName()))
                .andExpect(jsonPath("$.locale").value(updatedCity.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(updatedCity.isLocalePreferred()));
    }

    @DisplayName("Test Update Non Existing City")
    @Test
    public void testUpdateNonExistingCity() throws Exception {
        CityDto cityDto = cityDtoBuilder.build();
        when(cityService.updateCity(1, cityDto)).thenThrow(new ResourceNotFoundException("City not found"));
        ResultActions result = mockMvc.perform(
                put(CITY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cityDto))
        );
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("City not found")));
    }

    @DisplayName("Test Get City")
    @Test
    public void testGetCity() throws Exception {
        CityDto cityDto = cityDtoBuilder.build();
        when(cityService.getCity(1)).thenReturn(cityDtoBuilder.cityId(1).build());
        ResultActions result = mockMvc.perform(
                get(CITY_BASE_URL + "/{id}", 1)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityId").value(1))
                .andExpect(jsonPath("$.cityName").value(cityDto.getCityName()))
                .andExpect(jsonPath("$.locale").value(cityDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(cityDto.isLocalePreferred()));
    }

    @DisplayName("Test Get All Cities")
    @Test
    public void testGetAllCities() throws Exception {
        List<CityDto> cities = List.of(cityDtoBuilder.cityId(1).build());
        when(cityService.getAllCities()).thenReturn(cities);
        ResultActions result = mockMvc.perform(
                get(CITY_BASE_URL)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].cityId").value(1))
                .andExpect(jsonPath("$.[0].cityName").value(cities.get(0).getCityName()))
                .andExpect(jsonPath("$.[0].locale").value(cities.get(0).getLocale()))
                .andExpect(jsonPath("$.[0].localePreferred").value(cities.get(0).isLocalePreferred()));
    }

    @DisplayName("Test Delete City")
    @Test
    public void testDeleteCity() throws Exception {
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        doNothing().when(cityService).deleteCity(1, recordVoidRequest);
        ResultActions result = mockMvc.perform(
                delete(CITY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );
        result.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Test Delete NonExisting City")
    @Test
    public void testDeleteNonExistingCity() throws Exception {
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        doThrow(new ResourceNotFoundException("City not found")).when(cityService).deleteCity(1, recordVoidRequest);
        ResultActions result = mockMvc.perform(
                delete(CITY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("City not found")));
    }
} 