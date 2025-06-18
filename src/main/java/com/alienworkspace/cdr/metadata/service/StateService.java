package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.model.dto.metadata.StateDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.util.List;

/**
 * Service interface for State operations.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
public interface StateService {

    /**
     * Creates a new state.
     *
     * @param stateDto the state to create
     * @return the created state
     */
    StateDto createState(StateDto stateDto);

    /**
     * Updates an existing state.
     *
     * @param stateId the ID of the state to update
     * @param stateDto the state to update
     * @return the updated state
     */
    StateDto updateState(int stateId, StateDto stateDto);

    /**
     * Deletes a state.
     *
     * @param id the ID of the state to delete
     * @param request the request object containing the voided by user
     */
    void deleteState(int id, RecordVoidRequest request);

    /**
     * Retrieves a state by its ID.
     *
     * @param id the ID of the state to retrieve
     * @return the state
     */
    StateDto getState(int id);

    /**
     * Retrieves all states.
     *
     * @return a list of states
     */
    List<StateDto> getAllStates();
} 