package com.alienworkspace.cdr.metadata.controller;


import static com.alienworkspace.cdr.metadata.helpers.Constants.CITY_BASE_URL;

import com.alienworkspace.cdr.metadata.service.CityService;
import com.alienworkspace.cdr.model.dto.metadata.CityDto;
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
 * City Controller.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Tag(name = "City", description = "City API")
@RestController
@RequestMapping(CITY_BASE_URL)
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class CityController {

    private final CityService cityService;

    /**
     * Creates a new city.
     *
     * @param cityDto the city to create
     * @return the created city
     */
    @Operation(summary = "Create a new city", description = "Creates a new city")
    @ApiResponse(responseCode = "201", description = "City created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CityDto.class)))
    @PostMapping
    public ResponseEntity<CityDto> createCity(@RequestBody CityDto cityDto) {
        return new ResponseEntity<>(cityService.createCity(cityDto), HttpStatus.CREATED);
    }

    /**
     * Updates an existing city.
     *
     * @param id City ID
     * @param cityDto the city to update
     * @return the updated city
     */
    @Operation(summary = "Update an existing city", description = "Updates an existing city")
    @ApiResponse(responseCode = "200", description = "City updated successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CityDto.class)))
    @ApiResponse(responseCode = "404", description = "City not found")
    @PutMapping("/{id}")
    public ResponseEntity<CityDto> updateCity(@PathVariable int id, @RequestBody CityDto cityDto) {
        return ResponseEntity.ok(cityService.updateCity(id, cityDto));
    }

    /**
     * Deletes a city.
     *
     * @param recordVoidRequest the request object containing the voided by user
     */
    @Operation(summary = "Delete a city", description = "Deletes a city")
    @ApiResponse(responseCode = "200", description = "City deleted successfully")
    @ApiResponse(responseCode = "404", description = "City not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable int id, @RequestBody RecordVoidRequest recordVoidRequest) {
        cityService.deleteCity(id, recordVoidRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves a city by its ID.
     *
     * @param id the ID of the city to retrieve
     * @return the city
     */
    @Operation(summary = "Get a city by ID", description = "Retrieves a city by its ID")
    @ApiResponse(responseCode = "200", description = "City retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CityDto.class)))
    @ApiResponse(responseCode = "404", description = "City not found")
    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getCity(@PathVariable int id) {
        return ResponseEntity.ok(cityService.getCity(id));
    }

    /**
     * Retrieves all cities.
     *
     * @return a list of cities
     */
    @Operation(summary = "Get all cities", description = "Retrieves all cities")
    @ApiResponse(responseCode = "200", description = "Cities retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CityDto.class)))
    @GetMapping
    public ResponseEntity<List<CityDto>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }
} 