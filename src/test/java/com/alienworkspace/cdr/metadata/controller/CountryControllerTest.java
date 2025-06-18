package com.alienworkspace.cdr.metadata.controller;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.service.CountryService;
import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
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

import static com.alienworkspace.cdr.metadata.helpers.Constants.COUNTRY_BASE_URL;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CountryController.class)
public class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CountryService countryService;

    private CountryDto.CountryDtoBuilder countryDtoBuilder = CountryDto.builder();

    @BeforeEach
    public void setUp() {
        countryDtoBuilder
                .countryId(1)
                .countryName("Test Country")
                .locale("en_US")
                .localePreferred(true)
                .countryCode("CT123")
                .countryPhoneCode(234)
                .currencyCode("USD")
                .currencySymbol("$")
                .currencyName("Dollar")
                .countryGeoCode("GEO123");
    }

    @DisplayName("Test Create Country")
    @Test
    public void testCreateCountry() throws Exception {
        CountryDto countryDto = countryDtoBuilder.build();
        when(countryService.createCountry(countryDto)).thenReturn(countryDtoBuilder.countryId(1).build());
        ResultActions result = mockMvc.perform(
                post(COUNTRY_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(countryDto))
        );
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countryId").value(1))
                .andExpect(jsonPath("$.countryName").value(countryDto.getCountryName()))
                .andExpect(jsonPath("$.locale").value(countryDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(countryDto.isLocalePreferred()))
                .andExpect(jsonPath("$.countryCode").value(countryDto.getCountryCode()))
                .andExpect(jsonPath("$.countryPhoneCode").value(countryDto.getCountryPhoneCode()))
                .andExpect(jsonPath("$.currencyCode").value(countryDto.getCurrencyCode()))
                .andExpect(jsonPath("$.currencySymbol").value(countryDto.getCurrencySymbol()))
                .andExpect(jsonPath("$.currencyName").value(countryDto.getCurrencyName()))
                .andExpect(jsonPath("$.countryGeoCode").value(countryDto.getCountryGeoCode()));
    }

    @DisplayName("Test Update Country")
    @Test
    public void testUpdateCountry() throws Exception {
        CountryDto countryDto = countryDtoBuilder.build();
        CountryDto updatedCountry = countryDtoBuilder.countryId(1).build();
        when(countryService.updateCountry(1, countryDto)).thenReturn(updatedCountry);
        ResultActions result = mockMvc.perform(
                put(COUNTRY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(countryDto))
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryId").value(1))
                .andExpect(jsonPath("$.countryName").value(updatedCountry.getCountryName()))
                .andExpect(jsonPath("$.locale").value(updatedCountry.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(updatedCountry.isLocalePreferred()))
                .andExpect(jsonPath("$.currencyCode").value(updatedCountry.getCurrencyCode()))
                .andExpect(jsonPath("$.currencySymbol").value(updatedCountry.getCurrencySymbol()))
                .andExpect(jsonPath("$.currencyName").value(updatedCountry.getCurrencyName()));
    }

    @DisplayName("Test Update Non Existing Country")
    @Test
    public void testUpdateNonExistingCountry() throws Exception {
        CountryDto countryDto = countryDtoBuilder.build();
        when(countryService.updateCountry(1, countryDto)).thenThrow(new ResourceNotFoundException("Country not found"));
        ResultActions result = mockMvc.perform(
                put(COUNTRY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(countryDto))
        );
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Country not found")));
    }

    @DisplayName("Test Get Country")
    @Test
    public void testGetCountry() throws Exception {
        CountryDto countryDto = countryDtoBuilder.build();
        when(countryService.getCountry(1)).thenReturn(countryDtoBuilder.countryId(1).build());
        ResultActions result = mockMvc.perform(
                get(COUNTRY_BASE_URL + "/{id}", 1)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryId").value(1))
                .andExpect(jsonPath("$.countryName").value(countryDto.getCountryName()))
                .andExpect(jsonPath("$.locale").value(countryDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(countryDto.isLocalePreferred()))
                .andExpect(jsonPath("$.countryCode").value(countryDto.getCountryCode()))
                .andExpect(jsonPath("$.countryPhoneCode").value(countryDto.getCountryPhoneCode()))
                .andExpect(jsonPath("$.currencyCode").value(countryDto.getCurrencyCode()))
                .andExpect(jsonPath("$.currencySymbol").value(countryDto.getCurrencySymbol()))
                .andExpect(jsonPath("$.currencyName").value(countryDto.getCurrencyName()))
                .andExpect(jsonPath("$.countryGeoCode").value(countryDto.getCountryGeoCode()));
    }

    @DisplayName("Test Get All Countries")
    @Test
    public void testGetAllCountries() throws Exception {
        List<CountryDto> countries = List.of(countryDtoBuilder.countryId(1).build());
        when(countryService.getAllCountries()).thenReturn(countries);
        ResultActions result = mockMvc.perform(
                get(COUNTRY_BASE_URL)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].countryId").value(1))
                .andExpect(jsonPath("$.[0].countryName").value(countries.get(0).getCountryName()))
                .andExpect(jsonPath("$.[0].locale").value(countries.get(0).getLocale()))
                .andExpect(jsonPath("$.[0].localePreferred").value(countries.get(0).isLocalePreferred()))
                .andExpect(jsonPath("$.[0].countryCode").value(countries.get(0).getCountryCode()))
                .andExpect(jsonPath("$.[0].countryPhoneCode").value(countries.get(0).getCountryPhoneCode()))
                .andExpect(jsonPath("$.[0].currencyCode").value(countries.get(0).getCurrencyCode()))
                .andExpect(jsonPath("$.[0].currencySymbol").value(countries.get(0).getCurrencySymbol()))
                .andExpect(jsonPath("$.[0].currencyName").value(countries.get(0).getCurrencyName()))
                .andExpect(jsonPath("$.[0].countryGeoCode").value(countries.get(0).getCountryGeoCode()));
    }

    @DisplayName("Test Delete Country")
    @Test
    public void testDeleteCountry() throws Exception {
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        doNothing().when(countryService).deleteCountry(1, recordVoidRequest);
        ResultActions result = mockMvc.perform(
                delete(COUNTRY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );
        result.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Test Delete NonExisting Country")
    @Test
    public void testDeleteNonExistingCountry() throws Exception {
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        doThrow(new ResourceNotFoundException("Country not found")).when(countryService).deleteCountry(1, recordVoidRequest);
        ResultActions result = mockMvc.perform(
                delete(COUNTRY_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Country not found")));
    }
} 