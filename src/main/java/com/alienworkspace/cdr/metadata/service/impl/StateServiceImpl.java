package com.alienworkspace.cdr.metadata.service.impl;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.helpers.CurrentUser;
import com.alienworkspace.cdr.metadata.model.mapper.StateMapper;
import com.alienworkspace.cdr.metadata.repository.StateRepository;
import com.alienworkspace.cdr.metadata.service.StateService;
import com.alienworkspace.cdr.model.dto.metadata.StateDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the StateService interface.
 *
 * @author Ikenumah
 * @version 1.0
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class StateServiceImpl implements StateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateServiceImpl.class);
    private final StateRepository stateRepository;
    private final StateMapper stateMapper;

    /**
     * Create a new state.
     *
     * @param stateDto the state to create
     * @return the created state
     */
    @Transactional
    @Override
    public StateDto createState(StateDto stateDto) {
        try {
            return stateMapper.toDto(stateRepository.save(stateMapper.toEntity(stateDto)));
        } catch (Exception e) {
            LOGGER.error("Failed to create state", e);
            throw new IllegalArgumentException("Failed to create state", e);
        }
    }

    /**
     * Update an existing state.
     *
     * @param stateId the id of the state to update
     * @param stateDto the state to update
     * @return the updated state
     */
    @Transactional
    @Override
    public StateDto updateState(int stateId, StateDto stateDto) {
        return stateRepository.findById(stateId)
                .map(state -> {
                    try {
                        state.setStateName(stateDto.getStateName());
                        state.setLocale(stateDto.getLocale());
                        state.setLocalePreferred(stateDto.isLocalePreferred());
                        state.setStateCode(stateDto.getStateCode());
                        state.setStateGeoCode(stateDto.getStateGeoCode());
                        state.setStatePhoneCode(stateDto.getStatePhoneCode());
                        state.setLastModifiedBy(CurrentUser.getCurrentUser().getPersonId());
                        state.setLastModifiedAt(LocalDateTime.now());
                        return stateMapper.toDto(stateRepository.save(state));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to update state: " + e.getMessage(), e);
                    }
                }).orElseThrow(() -> new ResourceNotFoundException("State not found with id: " + stateId));
    }

    /**
     * Delete a state.
     *
     * @param id the id of the state to delete
     * @param recordVoidRequest the record void request
     */
    @Transactional
    @Override
    public void deleteState(int id, RecordVoidRequest recordVoidRequest) {
        stateRepository.findById(id)
                .map(state -> {
                    try {
                        state.setVoided(true);
                        state.setVoidedAt(LocalDateTime.now());
                        state.setVoidedBy(CurrentUser.getCurrentUser().getPersonId());
                        state.setVoidReason(recordVoidRequest.getVoidReason());
                        return stateMapper.toDto(stateRepository.save(state));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to delete state: " + e.getMessage(), e);
                    }
                }).orElseThrow(() -> new ResourceNotFoundException("State not found with id: " + id));
    }

    /**
     * Get a state by its id.
     *
     * @param id the id of the state to get
     * @return the state
     */
    @Transactional
    @Override
    public StateDto getState(int id) {
        return stateMapper.toDto(stateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State not found with id: " + id)));
    }

    /**
     * Get all states.
     *
     * @return a list of states
     */
    @Transactional
    @Override
    public List<StateDto> getAllStates() {
        return stateRepository.findAll().stream().map(stateMapper::toDto).toList();
    }
} 