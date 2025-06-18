package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.model.dto.metadata.LocationDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.util.List;

/**
 * Service interface for Location operations.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
public interface LocationService {

    /**
     * Creates a new location.
     *
     * @param locationDto the location to create
     * @return the created location
     */
    LocationDto createLocation(LocationDto locationDto);

    /**
     * Updates an existing location.
     *
     * @param locationId the ID of the location to update
     * @param locationDto the location to update
     * @return the updated location
     */
    LocationDto updateLocation(int locationId, LocationDto locationDto);

    /**
     * Deletes a location.
     *
     * @param id the ID of the location to delete
     * @param request the request object containing the voided by user
     */
    void deleteLocation(int id, RecordVoidRequest request);

    /**
     * Retrieves a location by its ID.
     *
     * @param id the ID of the location to retrieve
     * @return the location
     */
    LocationDto getLocation(int id);

    /**
     * Retrieves all locations.
     *
     * @return a list of locations
     */
    List<LocationDto> getAllLocations();
} 