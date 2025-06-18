package com.alienworkspace.cdr.metadata.controller;

import static com.alienworkspace.cdr.metadata.helpers.Constants.STATE_BASE_URL;

import com.alienworkspace.cdr.metadata.service.StateService;
import com.alienworkspace.cdr.model.dto.metadata.StateDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * State Controller.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Tag(name = "State", description = "State API")
@RestController
@RequestMapping(STATE_BASE_URL)
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class StateController {

    private final StateService stateService;

    /**
     * Creates a new state.
     *
     * @param stateDto the state to create
     * @return the created state
     */
    @Operation(summary = "Create a new state", description = "Creates a new state")
    @ApiResponse(responseCode = "201", description = "State created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StateDto.class)))
    @PostMapping
    public ResponseEntity<StateDto> createState(@RequestBody StateDto stateDto) {
        return new ResponseEntity<>(stateService.createState(stateDto), HttpStatus.CREATED);
    }

    /**
     * Updates an existing state.
     *
     * @param id State ID
     * @param stateDto the state to update
     * @return the updated state
     */
    @Operation(summary = "Update an existing state", description = "Updates an existing state")
    @ApiResponse(responseCode = "200", description = "State updated successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StateDto.class)))
    @ApiResponse(responseCode = "404", description = "State not found")
    @PutMapping("/{id}")
    public ResponseEntity<StateDto> updateState(@PathVariable int id, @RequestBody StateDto stateDto) {
        return ResponseEntity.ok(stateService.updateState(id, stateDto));
    }

    /**
     * Deletes a state.
     *
     * @param recordVoidRequest the request object containing the voided by user
     */
    @Operation(summary = "Delete a state", description = "Deletes a state")
    @ApiResponse(responseCode = "200", description = "State deleted successfully")
    @ApiResponse(responseCode = "404", description = "State not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteState(@PathVariable int id, @RequestBody RecordVoidRequest recordVoidRequest) {
        stateService.deleteState(id, recordVoidRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves a state by its ID.
     *
     * @param id the ID of the state to retrieve
     * @return the state
     */
    @Operation(summary = "Get a state by ID", description = "Retrieves a state by its ID")
    @ApiResponse(responseCode = "200", description = "State retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StateDto.class)))
    @ApiResponse(responseCode = "404", description = "State not found")
    @GetMapping("/{id}")
    public ResponseEntity<StateDto> getState(@PathVariable int id) {
        return ResponseEntity.ok(stateService.getState(id));
    }

    /**
     * Retrieves all states.
     *
     * @return a list of states
     */
    @Operation(summary = "Get all states", description = "Retrieves all states")
    @ApiResponse(responseCode = "200", description = "States retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StateDto.class)))
    @GetMapping
    public ResponseEntity<List<StateDto>> getAllStates() {
        return ResponseEntity.ok(stateService.getAllStates());
    }
} 