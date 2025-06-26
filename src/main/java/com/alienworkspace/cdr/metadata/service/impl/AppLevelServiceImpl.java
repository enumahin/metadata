package com.alienworkspace.cdr.metadata.service.impl;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.model.mapper.CityMapper;
import com.alienworkspace.cdr.metadata.model.mapper.CommunityMapper;
import com.alienworkspace.cdr.metadata.model.mapper.CountryMapper;
import com.alienworkspace.cdr.metadata.model.mapper.CountyMapper;
import com.alienworkspace.cdr.metadata.model.mapper.LocationMapper;
import com.alienworkspace.cdr.metadata.model.mapper.StateMapper;
import com.alienworkspace.cdr.metadata.repository.CityRepository;
import com.alienworkspace.cdr.metadata.repository.CommunityRepository;
import com.alienworkspace.cdr.metadata.repository.CountryRepository;
import com.alienworkspace.cdr.metadata.repository.CountyRepository;
import com.alienworkspace.cdr.metadata.repository.LocationRepository;
import com.alienworkspace.cdr.metadata.repository.StateRepository;
import com.alienworkspace.cdr.metadata.service.AppLevelService;
import com.alienworkspace.cdr.model.dto.metadata.CityDto;
import com.alienworkspace.cdr.model.dto.metadata.CommunityDto;
import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
import com.alienworkspace.cdr.model.dto.metadata.CountyDto;
import com.alienworkspace.cdr.model.dto.metadata.LocationDto;
import com.alienworkspace.cdr.model.dto.metadata.StateDto;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service implementation for app-level operations.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Service
@AllArgsConstructor
public class AppLevelServiceImpl implements AppLevelService {

    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CountyRepository countyRepository;
    private final CityRepository cityRepository;
    private final CommunityRepository communityRepository;
    private final LocationRepository locationRepository;
    private final CountryMapper countryMapper;
    private final StateMapper stateMapper;
    private final CountyMapper countyMapper;
    private final CityMapper cityMapper;
    private final CommunityMapper communityMapper;
    private final LocationMapper locationMapper;

    /**
     * Get a location by its id.
     *
     * @param countryId   the id of the country
     * @param stateId     the id of the state
     * @param countyId    the id of the county
     * @param cityId      the id of the city
     * @param communityId the id of the community
     * @param locationId  the id of the location
     * @return the location
     */
    @Override
    public CountryDto getPersonLocation(int countryId, Integer stateId, Integer countyId, Integer cityId,
                                        Integer communityId, Integer locationId) {
        CountryDto country = countryRepository.findById(countryId)
                .map(countryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));
        if (stateId != null) {
            StateDto state = stateRepository.findById(stateId)
                    .map(stateMapper::toDto)
                    .orElseThrow(() -> new ResourceNotFoundException("State not found"));
            if (countyId != null) {
                CountyDto county = countyRepository.findById(countyId)
                        .map(countyMapper::toDto)
                        .orElseThrow(() -> new ResourceNotFoundException("County not found"));
                state.setCounties(ImmutableSet.of(county));
                if (cityId != null) {
                    CityDto city = cityRepository.findById(cityId)
                            .map(cityMapper::toDto)
                            .orElseThrow(() -> new ResourceNotFoundException("City not found"));
                    county.setCities(ImmutableSet.of(city));
                    if (communityId != null) {
                        CommunityDto community = communityRepository.findById(communityId)
                                .map(communityMapper::toDto)
                                .orElseThrow(() -> new ResourceNotFoundException("Community not found"));
                        city.setCommunities(ImmutableSet.of(community));
                        if (locationId != null) {
                            LocationDto location = locationRepository.findById(locationId)
                                    .map(locationMapper::toDto)
                                    .orElseThrow(() -> new ResourceNotFoundException("Location not found"));
                            community.setLocations(ImmutableSet.of(location));
                        }
                    }
                }
            }
            country.setStates(ImmutableSet.of(state));
        }

        return country;
    }
}
