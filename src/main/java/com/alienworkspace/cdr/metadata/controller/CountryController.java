package com.alienworkspace.cdr.metadata.controller;

import static com.alienworkspace.cdr.metadata.helpers.Constants.COUNTRY_BASE_URL;

import com.alienworkspace.cdr.metadata.service.CountryService;
import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Country Controller.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Tag(name = "Country", description = "Country API")
@RestController
@RequestMapping(COUNTRY_BASE_URL)
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class CountryController {

    private static final Logger log = LoggerFactory.getLogger(CountryController.class);

    private final CountryService countryService;

    /**
     * Creates a new country.
     *
     * @param countryDto the country to create
     * @return the created country
     */
    @Operation(summary = "Create a new country", description = "Creates a new country")
    @ApiResponse(responseCode = "201", description = "Country created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CountryDto.class)))
    @PostMapping
    public ResponseEntity<CountryDto> createCountry(@RequestBody CountryDto countryDto) {
        return new ResponseEntity<>(countryService.createCountry(countryDto), HttpStatus.CREATED);
    }

    /**
     * Updates an existing country.
     *
     * @param id Country ID
     * @param countryDto the country to update
     * @return the updated country
     */
    @Operation(summary = "Update an existing country", description = "Updates an existing country")
    @ApiResponse(responseCode = "200", description = "Country updated successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CountryDto.class)))
    @ApiResponse(responseCode = "404", description = "Country not found")
    @PutMapping("/{id}")
    public ResponseEntity<CountryDto> updateCountry(@PathVariable int id, @RequestBody CountryDto countryDto) {
        return ResponseEntity.ok(countryService.updateCountry(id, countryDto));
    }

    /**
     * Deletes a country.
     *
     * @param recordVoidRequest the request object containing the voided by user
     */
    @Operation(summary = "Delete a country", description = "Deletes a country")
    @ApiResponse(responseCode = "200", description = "Country deleted successfully")
    @ApiResponse(responseCode = "404", description = "Country not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable int id, @RequestBody RecordVoidRequest recordVoidRequest) {
        countryService.deleteCountry(id, recordVoidRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves a country by its ID.
     *
     * @param id the ID of the country to retrieve
     * @return the country
     */
    @Operation(summary = "Get a country by ID", description = "Retrieves a country by its ID")
    @ApiResponse(responseCode = "200", description = "Country retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CountryDto.class)))
    @ApiResponse(responseCode = "404", description = "Country not found")
    @GetMapping("/{id}")
    public ResponseEntity<CountryDto> getCountry(@RequestHeader("X-cdr-correlation-id") String correlationId,
                                                 @PathVariable int id) {
        log.debug("Retrieving country with ID: {} with correlationId: {}", id, correlationId);
        return ResponseEntity.ok(countryService.getCountry(id));
    }

    /**
     * Retrieves a country by its countryCode.
     *
     * @param countryCode the countryCode of the country to retrieve
     * @return the country
     */
    @Operation(summary = "Get a country by ID", description = "Retrieves a country by its ID")
    @ApiResponse(responseCode = "200", description = "Country retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CountryDto.class)))
    @ApiResponse(responseCode = "404", description = "Country not found")
    @GetMapping("/code/{code}")
    public ResponseEntity<CountryDto> getCountryByCode(@PathVariable("code") String countryCode) {
        return ResponseEntity.ok(countryService.findByCountryCode(countryCode));
    }

    /**
     * Retrieves all countries.
     *
     * @return a list of countries
     */
    @Operation(summary = "Get all countries", description = "Retrieves all countries")
    @ApiResponse(responseCode = "200", description = "Countries retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CountryDto.class)))
    @GetMapping
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }
}
