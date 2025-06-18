package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.model.dto.metadata.CityDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.util.List;

/**
 * Service interface for City operations.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
public interface CityService {

    /**
     * Creates a new city.
     *
     * @param cityDto the city to create
     * @return the created city
     */
    CityDto createCity(CityDto cityDto);

    /**
     * Updates an existing city.
     *
     * @param cityId the ID of the city to update
     * @param cityDto the city to update
     * @return the updated city
     */
    CityDto updateCity(int cityId, CityDto cityDto);

    /**
     * Deletes a city.
     *
     * @param id the ID of the city to delete
     * @param request the request object containing the voided by user
     */
    void deleteCity(int id, RecordVoidRequest request);

    /**
     * Retrieves a city by its ID.
     *
     * @param id the ID of the city to retrieve
     * @return the city
     */
    CityDto getCity(int id);

    /**
     * Retrieves all cities.
     *
     * @return a list of cities
     */
    List<CityDto> getAllCities();
} 