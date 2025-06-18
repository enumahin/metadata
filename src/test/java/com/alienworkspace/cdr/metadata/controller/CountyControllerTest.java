package com.alienworkspace.cdr.metadata.controller;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.service.CountyService;
import com.alienworkspace.cdr.model.dto.metadata.CountyDto;
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

import static com.alienworkspace.cdr.metadata.helpers.Constants.COUNTY_BASE_URL;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CountyController.class)
public class CountyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CountyService countyService;

    private CountyDto.CountyDtoBuilder countyDtoBuilder = CountyDto.builder();

    @BeforeEach
    public void setUp() {
        countyDtoBuilder
                .countyId(1)
                .countyName("Test County")
                .locale("en_US")
                .localePreferred(true)
                .countyCode("CNTY123")
                .countyGeoCode("GEO123")
                .countyPhoneCode(234);
    }

    @DisplayName("Test Create County")
    @Test
    public void testCreateCounty() throws Exception {
        CountyDto countyDto = countyDtoBuilder.build();
        when(countyService.createCounty(countyDto)).thenReturn(countyDtoBuilder.countyId(1).build());
        ResultActions result = mockMvc.perform(
                post(COUNTY_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(countyDto))
        );
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countyId").value(1))
                .andExpect(jsonPath("$.countyName").value(countyDto.getCountyName()))
                .andExpect(jsonPath("$.locale").value(countyDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(countyDto.isLocalePreferred()));
    }

    @DisplayName("Test Update County")
    @Test
    public void testUpdateCounty() throws Exception {
        CountyDto.CountyDtoBuilder updatedCountyDto = countyDtoBuilder
                .countyName("Updated County")
                .locale("fr_FR")
                .localePreferred(false);
        CountyDto updatedCounty = updatedCountyDto.countyId(1).build();
        when(countyService.updateCounty(1, updatedCountyDto.build())).thenReturn(updatedCounty);
        ResultActions result = mockMvc.perform(
                put(COUNTY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedCountyDto.build()))
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countyId").value(1))
                .andExpect(jsonPath("$.countyName").value(updatedCounty.getCountyName()))
                .andExpect(jsonPath("$.locale").value(updatedCounty.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(updatedCounty.isLocalePreferred()));
    }

    @DisplayName("Test Update Non Existing County")
    @Test
    public void testUpdateNonExistingCounty() throws Exception {
        CountyDto countyDto = countyDtoBuilder.build();
        when(countyService.updateCounty(1, countyDto)).thenThrow(new ResourceNotFoundException("County not found"));
        ResultActions result = mockMvc.perform(
                put(COUNTY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(countyDto))
        );
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("County not found")));
    }

    @DisplayName("Test Get County")
    @Test
    public void testGetCounty() throws Exception {
        CountyDto countyDto = countyDtoBuilder.build();
        when(countyService.getCounty(1)).thenReturn(countyDtoBuilder.countyId(1).build());
        ResultActions result = mockMvc.perform(
                get(COUNTY_BASE_URL + "/{id}", 1)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countyId").value(1))
                .andExpect(jsonPath("$.countyName").value(countyDto.getCountyName()))
                .andExpect(jsonPath("$.locale").value(countyDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(countyDto.isLocalePreferred()));
    }

    @DisplayName("Test Get All Counties")
    @Test
    public void testGetAllCounties() throws Exception {
        List<CountyDto> counties = List.of(countyDtoBuilder.countyId(1).build());
        when(countyService.getAllCounties()).thenReturn(counties);
        ResultActions result = mockMvc.perform(
                get(COUNTY_BASE_URL)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].countyId").value(1))
                .andExpect(jsonPath("$.[0].countyName").value(counties.get(0).getCountyName()))
                .andExpect(jsonPath("$.[0].locale").value(counties.get(0).getLocale()))
                .andExpect(jsonPath("$.[0].localePreferred").value(counties.get(0).isLocalePreferred()));
    }

    @DisplayName("Test Delete County")
    @Test
    public void testDeleteCounty() throws Exception {
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        doNothing().when(countyService).deleteCounty(1, recordVoidRequest);
        ResultActions result = mockMvc.perform(
                delete(COUNTY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );
        result.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Test Delete NonExisting County")
    @Test
    public void testDeleteNonExistingCounty() throws Exception {
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        doThrow(new ResourceNotFoundException("County not found")).when(countyService).deleteCounty(1, recordVoidRequest);
        ResultActions result = mockMvc.perform(
                delete(COUNTY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("County not found")));
    }
} 