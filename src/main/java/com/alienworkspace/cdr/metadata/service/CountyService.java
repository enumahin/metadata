package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.model.dto.metadata.CountyDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.util.List;

/**
 * Service interface for County operations.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
public interface CountyService {

    /**
     * Creates a new county.
     *
     * @param countyDto the county to create
     * @return the created county
     */
    CountyDto createCounty(CountyDto countyDto);

    /**
     * Updates an existing county.
     *
     * @param countyId the ID of the county to update
     * @param countyDto the county to update
     * @return the updated county
     */
    CountyDto updateCounty(int countyId, CountyDto countyDto);

    /**
     * Deletes a county.
     *
     * @param id the ID of the county to delete
     * @param request the request object containing the voided by user
     */
    void deleteCounty(int id, RecordVoidRequest request);

    /**
     * Retrieves a county by its ID.
     *
     * @param id the ID of the county to retrieve
     * @return the county
     */
    CountyDto getCounty(int id);

    /**
     * Retrieves all counties.
     *
     * @return a list of counties
     */
    List<CountyDto> getAllCounties();
} 