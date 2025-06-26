package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.model.dto.metadata.CountryDto;

/**
 * Service interface for app-level operations.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
public interface AppLevelService {

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
    CountryDto getPersonLocation(int countryId, Integer stateId, Integer countyId, Integer cityId, Integer communityId,
                                 Integer locationId);
}
