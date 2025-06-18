package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.util.List;

/**
 * Service interface for Country operations.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
public interface CountryService {

    /**
     * Find a country by its country code.
     *
     * @param countryCode the country code
     * @return the country
     */
    CountryDto findByCountryCode(String countryCode);

    /**
     * Creates a new country.
     *
     * @param countryDto the country to create
     * @return the created country
     */
    CountryDto createCountry(CountryDto countryDto);

    /**
     * Updates an existing country.
     *
     * @param countryId the ID of the country to update
     * @param countryDto the country to update
     * @return the updated country
     */
    CountryDto updateCountry(int countryId, CountryDto countryDto);

    /**
     * Deletes a country.
     *
     * @param id the ID of the country to delete
     * @param request the request object containing the voided by user
     */
    void deleteCountry(int id, RecordVoidRequest request);

    /**
     * Retrieves a country by its ID.
     *
     * @param id the ID of the country to retrieve
     * @return the country
     */
    CountryDto getCountry(int id);

    /**
     * Retrieves all countries.
     *
     * @return a list of countries
     */
    List<CountryDto> getAllCountries();
}
