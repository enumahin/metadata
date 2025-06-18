package com.alienworkspace.cdr.metadata.controller;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.service.StateService;
import com.alienworkspace.cdr.model.dto.metadata.StateDto;
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

import static com.alienworkspace.cdr.metadata.helpers.Constants.STATE_BASE_URL;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StateController.class)
public class StateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StateService stateService;

    private StateDto.StateDtoBuilder stateDtoBuilder = StateDto.builder();

    @BeforeEach
    public void setUp() {
        stateDtoBuilder
                .stateId(1)
                .stateName("Test State")
                .locale("en_US")
                .localePreferred(true)
                .stateCode("ST123")
                .stateGeoCode("GEO123")
                .statePhoneCode(234);
    }

    @DisplayName("Test Create State")
    @Test
    public void testCreateState() throws Exception {
        StateDto stateDto = stateDtoBuilder.build();
        when(stateService.createState(stateDto)).thenReturn(stateDtoBuilder.stateId(1).build());
        ResultActions result = mockMvc.perform(
                post(STATE_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(stateDto))
        );
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.stateId").value(1))
                .andExpect(jsonPath("$.stateName").value(stateDto.getStateName()))
                .andExpect(jsonPath("$.locale").value(stateDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(stateDto.isLocalePreferred()));
    }

    @DisplayName("Test Update State")
    @Test
    public void testUpdateState() throws Exception {
        StateDto.StateDtoBuilder updatedStateDto = stateDtoBuilder
                .stateName("Updated State")
                .locale("fr_FR")
                .localePreferred(false);
        StateDto updatedState = updatedStateDto.stateId(1).build();
        when(stateService.updateState(1, updatedStateDto.build())).thenReturn(updatedState);
        ResultActions result = mockMvc.perform(
                put(STATE_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedStateDto.build()))
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stateId").value(1))
                .andExpect(jsonPath("$.stateName").value(updatedState.getStateName()))
                .andExpect(jsonPath("$.locale").value(updatedState.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(updatedState.isLocalePreferred()));
    }

    @DisplayName("Test Update Non Existing State")
    @Test
    public void testUpdateNonExistingState() throws Exception {
        StateDto stateDto = stateDtoBuilder.build();
        when(stateService.updateState(1, stateDto)).thenThrow(new ResourceNotFoundException("State not found"));
        ResultActions result = mockMvc.perform(
                put(STATE_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(stateDto))
        );
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("State not found")));
    }

    @DisplayName("Test Get State")
    @Test
    public void testGetState() throws Exception {
        StateDto stateDto = stateDtoBuilder.build();
        when(stateService.getState(1)).thenReturn(stateDtoBuilder.stateId(1).build());
        ResultActions result = mockMvc.perform(
                get(STATE_BASE_URL + "/{id}", 1)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stateId").value(1))
                .andExpect(jsonPath("$.stateName").value(stateDto.getStateName()))
                .andExpect(jsonPath("$.locale").value(stateDto.getLocale()))
                .andExpect(jsonPath("$.localePreferred").value(stateDto.isLocalePreferred()));
    }

    @DisplayName("Test Get All States")
    @Test
    public void testGetAllStates() throws Exception {
        List<StateDto> states = List.of(stateDtoBuilder.stateId(1).build());
        when(stateService.getAllStates()).thenReturn(states);
        ResultActions result = mockMvc.perform(
                get(STATE_BASE_URL)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].stateId").value(1))
                .andExpect(jsonPath("$.[0].stateName").value(states.get(0).getStateName()))
                .andExpect(jsonPath("$.[0].locale").value(states.get(0).getLocale()))
                .andExpect(jsonPath("$.[0].localePreferred").value(states.get(0).isLocalePreferred()));
    }

    @DisplayName("Test Delete State")
    @Test
    public void testDeleteState() throws Exception {
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        doNothing().when(stateService).deleteState(1, recordVoidRequest);
        ResultActions result = mockMvc.perform(
                delete(STATE_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );
        result.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Test Delete NonExisting State")
    @Test
    public void testDeleteNonExistingState() throws Exception {
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        doThrow(new ResourceNotFoundException("State not found")).when(stateService).deleteState(1, recordVoidRequest);
        ResultActions result = mockMvc.perform(
                delete(STATE_BASE_URL + "/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recordVoidRequest))
        );
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("State not found")));
    }
} 