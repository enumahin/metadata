package com.alienworkspace.cdr.metadata.controller;

import static com.alienworkspace.cdr.metadata.helpers.Constants.LOCATION_BASE_URL;

import com.alienworkspace.cdr.metadata.service.LocationService;
import com.alienworkspace.cdr.model.dto.metadata.LocationDto;
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
 * Location Controller.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Tag(name = "Location", description = "Location API")
@RestController
@RequestMapping(LOCATION_BASE_URL)
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class LocationController {

    private final LocationService locationService;

    /**
     * Creates a new location.
     *
     * @param locationDto the location to create
     * @return the created location
     */
    @Operation(summary = "Create a new location", description = "Creates a new location")
    @ApiResponse(responseCode = "201", description = "Location created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LocationDto.class)))
    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@RequestBody LocationDto locationDto) {
        return new ResponseEntity<>(locationService.createLocation(locationDto), HttpStatus.CREATED);
    }

    /**
     * Updates an existing location.
     *
     * @param id Location ID
     * @param locationDto the location to update
     * @return the updated location
     */
    @Operation(summary = "Update an existing location", description = "Updates an existing location")
    @ApiResponse(responseCode = "200", description = "Location updated successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LocationDto.class)))
    @ApiResponse(responseCode = "404", description = "Location not found")
    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable int id, @RequestBody LocationDto locationDto) {
        return ResponseEntity.ok(locationService.updateLocation(id, locationDto));
    }

    /**
     * Deletes a location.
     *
     * @param recordVoidRequest the request object containing the voided by user
     */
    @Operation(summary = "Delete a location", description = "Deletes a location")
    @ApiResponse(responseCode = "200", description = "Location deleted successfully")
    @ApiResponse(responseCode = "404", description = "Location not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable int id, @RequestBody RecordVoidRequest recordVoidRequest) {
        locationService.deleteLocation(id, recordVoidRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves a location by its ID.
     *
     * @param id the ID of the location to retrieve
     * @return the location
     */
    @Operation(summary = "Get a location by ID", description = "Retrieves a location by its ID")
    @ApiResponse(responseCode = "200", description = "Location retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LocationDto.class)))
    @ApiResponse(responseCode = "404", description = "Location not found")
    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getLocation(@PathVariable int id) {
        return ResponseEntity.ok(locationService.getLocation(id));
    }

    /**
     * Retrieves all locations.
     *
     * @return a list of locations
     */
    @Operation(summary = "Get all locations", description = "Retrieves all locations")
    @ApiResponse(responseCode = "200", description = "Locations retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LocationDto.class)))
    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }
} 