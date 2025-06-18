package com.alienworkspace.cdr.metadata.controller;

import static com.alienworkspace.cdr.metadata.helpers.Constants.COUNTY_BASE_URL;

import com.alienworkspace.cdr.metadata.service.CountyService;
import com.alienworkspace.cdr.model.dto.metadata.CountyDto;
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
 * County Controller.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Tag(name = "County", description = "County API")
@RestController
@RequestMapping(COUNTY_BASE_URL)
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class CountyController {

    private final CountyService countyService;

    /**
     * Creates a new county.
     *
     * @param countyDto the county to create
     * @return the created county
     */
    @Operation(summary = "Create a new county", description = "Creates a new county")
    @ApiResponse(responseCode = "201", description = "County created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CountyDto.class)))
    @PostMapping
    public ResponseEntity<CountyDto> createCounty(@RequestBody CountyDto countyDto) {
        return new ResponseEntity<>(countyService.createCounty(countyDto), HttpStatus.CREATED);
    }

    /**
     * Updates an existing county.
     *
     * @param id County ID
     * @param countyDto the county to update
     * @return the updated county
     */
    @Operation(summary = "Update an existing county", description = "Updates an existing county")
    @ApiResponse(responseCode = "200", description = "County updated successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CountyDto.class)))
    @ApiResponse(responseCode = "404", description = "County not found")
    @PutMapping("/{id}")
    public ResponseEntity<CountyDto> updateCounty(@PathVariable int id, @RequestBody CountyDto countyDto) {
        return ResponseEntity.ok(countyService.updateCounty(id, countyDto));
    }

    /**
     * Deletes a county.
     *
     * @param recordVoidRequest the request object containing the voided by user
     */
    @Operation(summary = "Delete a county", description = "Deletes a county")
    @ApiResponse(responseCode = "200", description = "County deleted successfully")
    @ApiResponse(responseCode = "404", description = "County not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCounty(@PathVariable int id, @RequestBody RecordVoidRequest recordVoidRequest) {
        countyService.deleteCounty(id, recordVoidRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves a county by its ID.
     *
     * @param id the ID of the county to retrieve
     * @return the county
     */
    @Operation(summary = "Get a county by ID", description = "Retrieves a county by its ID")
    @ApiResponse(responseCode = "200", description = "County retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CountyDto.class)))
    @ApiResponse(responseCode = "404", description = "County not found")
    @GetMapping("/{id}")
    public ResponseEntity<CountyDto> getCounty(@PathVariable int id) {
        return ResponseEntity.ok(countyService.getCounty(id));
    }

    /**
     * Retrieves all counties.
     *
     * @return a list of counties
     */
    @Operation(summary = "Get all counties", description = "Retrieves all counties")
    @ApiResponse(responseCode = "200", description = "Counties retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CountyDto.class)))
    @GetMapping
    public ResponseEntity<List<CountyDto>> getAllCounties() {
        return ResponseEntity.ok(countyService.getAllCounties());
    }
} 