package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.metadata.model.State;
import com.alienworkspace.cdr.metadata.model.mapper.StateMapper;
import com.alienworkspace.cdr.metadata.repository.StateRepository;
import com.alienworkspace.cdr.metadata.service.impl.StateServiceImpl;
import com.alienworkspace.cdr.model.dto.metadata.StateDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StateServiceTest {
    @Mock
    StateRepository stateRepository;
    @Mock
    StateMapper stateMapper;
    @InjectMocks
    StateServiceImpl stateService;

    private final StateDto.StateDtoBuilder stateDtoBuilder = StateDto.builder();
    private final State.StateBuilder stateBuilder = State.builder();

    @BeforeEach
    public void setUp() {
        stateDtoBuilder
                .stateId(1)
                .stateName("Test State")
                .locale("en_US")
                .localePreferred(true)
                .stateCode("STA123")
                .stateGeoCode("GEO123")
                .statePhoneCode(234);
        stateBuilder
                .stateId(1)
                .stateName("Test State")
                .locale("en_US")
                .localePreferred(true)
                .stateCode("STA123")
                .stateGeoCode("GEO123")
                .statePhoneCode(234);
    }

    @DisplayName("Test Create State")
    @Test
    public void testCreateState() {
        StateDto stateDto = stateDtoBuilder.build();
        State state = stateBuilder.build();
        State savedState = stateBuilder.build();
        when(stateMapper.toEntity(stateDto)).thenReturn(state);
        when(stateRepository.save(state)).thenReturn(savedState);
        when(stateMapper.toDto(savedState)).thenReturn(stateDto);
        StateDto actual = stateService.createState(stateDto);
        assertNotNull(actual);
        assertEquals(stateDto.getStateId(), actual.getStateId());
        verify(stateRepository).save(stateMapper.toEntity(stateDto));
        verifyNoMoreInteractions(stateRepository);
    }

    @DisplayName("Test Update State")
    @Test
    public void testUpdateState() {
        StateDto stateDto = stateDtoBuilder.build();
        State existingState = stateBuilder.build();
        State updatedState = stateBuilder.build();
        updatedState.setStateName("Updated");
        StateDto expectedDto = stateDtoBuilder.stateName("Updated").build();
        when(stateRepository.findById(stateDto.getStateId())).thenReturn(Optional.of(existingState));
        when(stateRepository.save(existingState)).thenReturn(updatedState);
        when(stateMapper.toDto(updatedState)).thenReturn(expectedDto);
        StateDto actual = stateService.updateState(stateDto.getStateId(), stateDtoBuilder.stateName("Updated").build());
        assertNotNull(actual);
        assertEquals("Updated", actual.getStateName());
        verify(stateRepository).save(existingState);
        verifyNoMoreInteractions(stateRepository);
    }

    @DisplayName("Test Delete State")
    @Test
    public void testDeleteState() {
        RecordVoidRequest request = new RecordVoidRequest();
        request.setVoidReason("Test");
        State existingState = stateBuilder.build();
        State voidedState = stateBuilder.build();
        voidedState.setVoided(true);
        voidedState.setVoidedBy(3L);
        voidedState.setVoidedAt(LocalDateTime.now());
        when(stateRepository.findById(1)).thenReturn(Optional.of(existingState));
        when(stateRepository.save(existingState)).thenReturn(voidedState);
        when(stateMapper.toDto(voidedState)).thenReturn(stateDtoBuilder.build());
        stateService.deleteState(1, request);
        verify(stateRepository).save(existingState);
        verifyNoMoreInteractions(stateRepository);
        assertTrue(existingState.isVoided());
        assertNotNull(existingState.getVoidedAt());
    }

    @DisplayName("Test Get State")
    @Test
    public void testGetState() {
        int id = 1;
        State existingState = stateBuilder.build();
        StateDto expectedDto = stateDtoBuilder.build();
        when(stateRepository.findById(id)).thenReturn(Optional.of(existingState));
        when(stateMapper.toDto(existingState)).thenReturn(expectedDto);
        StateDto actual = stateService.getState(id);
        assertNotNull(actual);
        assertEquals(expectedDto, actual);
        verify(stateRepository).findById(id);
        verifyNoMoreInteractions(stateRepository);
    }

    @DisplayName("Test Get All States")
    @Test
    public void testGetAllStates() {
        List<State> states = List.of(stateBuilder.build());
        List<StateDto> expectedDtos = List.of(stateDtoBuilder.build());
        when(stateRepository.findAll()).thenReturn(states);
        when(stateMapper.toDto(any(State.class))).thenReturn(stateDtoBuilder.build());
        List<StateDto> actual = stateService.getAllStates();
        assertNotNull(actual);
        assertEquals(expectedDtos.size(), actual.size());
        verify(stateRepository).findAll();
        verifyNoMoreInteractions(stateRepository);
    }
} 