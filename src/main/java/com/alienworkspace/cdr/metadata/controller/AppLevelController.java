package com.alienworkspace.cdr.metadata.controller;

import static com.alienworkspace.cdr.metadata.helpers.Constants.BASE_URL;

import com.alienworkspace.cdr.metadata.service.AppLevelService;
import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AppLevelController is a REST controller that handles requests related to app-level operations.
 * It is responsible for exposing endpoints under the base URL "/api/metadata".
 *
 * <p>
 * This class defines entry points to interact with app-level operations.
 */
@Tag(name = "AppLevelController", description = "App-level operations")
@SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
@RestController
@RequestMapping(BASE_URL)
public class AppLevelController {

    private static final Logger logger = LoggerFactory.getLogger(AppLevelController.class);

    private final AppLevelService appLevelService;

    /**
     * Constructor for AppLevelController.
     *
     * @param appLevelService The service for app-level operations.
     */
    public AppLevelController(AppLevelService appLevelService) {
        this.appLevelService = appLevelService;
    }

    /**
     * Retrieves the location of a person.
     *
     * @param countryId  The ID of the country.
     * @param stateId    The ID of the state.
     * @param countyId   The ID of the county.
     * @param cityId     The ID of the city.
     * @param communityId The ID of the community.
     * @param locationId The ID of the location.
     * @return The location of the person.
     */
    @Operation(summary = "Get the location of a person", description = "Retrieves the location of a person")
    @ApiResponse(responseCode = "200", description = "Location retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CountryDto.class)))
    @GetMapping("/person-location/{countryId}/{stateId}/{countyId}/{cityId}/{communityId}/{locationId}")
    ResponseEntity<CountryDto> getPersonLocation(
                                                 @RequestHeader("X-cdr-correlation-id") String correlationId,
                                                 @PathVariable int countryId, @PathVariable Integer stateId,
                                                 @PathVariable Integer countyId, @PathVariable Integer cityId,
                                                 @PathVariable Integer communityId, @PathVariable Integer locationId) {
        logger.info("Retrieving location for person with correlationId: {}", correlationId);
        CountryDto countryDto = appLevelService.getPersonLocation(countryId, stateId, countyId, cityId, communityId,
                locationId);
        return ResponseEntity.ok(countryDto);
    }

    /**
     * Retrieves the location of a person.
     *
     * @param countryId  The ID of the country.
     * @param stateId    The ID of the state.
     * @param countyId   The ID of the county.
     * @param cityId     The ID of the city.
     * @param communityId The ID of the community.
     * @return The location of the person.
     */
    @Operation(summary = "Get the location of a person", description = "Retrieves the location of a person")
    @ApiResponse(responseCode = "200", description = "Location retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CountryDto.class)))
    @GetMapping("/person-location/{countryId}/{stateId}/{countyId}/{cityId}/{communityId}")
    ResponseEntity<CountryDto> getPersonLocation(@RequestHeader("X-cdr-correlation-id") String correlationId,
                                                 @PathVariable int countryId, @PathVariable Integer stateId,
                                                 @PathVariable Integer countyId, @PathVariable Integer cityId,
                                                 @PathVariable Integer communityId) {
        logger.info("Retrieving location for person with correlationId: {}", correlationId);
        CountryDto countryDto = appLevelService.getPersonLocation(
                countryId, stateId, countyId, cityId, communityId, null);
        return ResponseEntity.ok(countryDto);
    }
}
