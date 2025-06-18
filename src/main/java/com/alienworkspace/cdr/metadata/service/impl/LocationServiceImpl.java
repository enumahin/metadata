package com.alienworkspace.cdr.metadata.service.impl;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.helpers.CurrentUser;
import com.alienworkspace.cdr.metadata.model.mapper.LocationMapper;
import com.alienworkspace.cdr.metadata.repository.LocationRepository;
import com.alienworkspace.cdr.metadata.service.LocationService;
import com.alienworkspace.cdr.model.dto.metadata.LocationDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of the LocationService interface.
 *
 * @author Ikenumah
 * @version 1.0
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationServiceImpl.class);
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    /**
     * Create a new location.
     *
     * @param locationDto the location to create
     * @return the created location
     */
    @Override
    public LocationDto createLocation(LocationDto locationDto) {
        try {
            return locationMapper.toDto(locationRepository.save(locationMapper.toEntity(locationDto)));
        } catch (Exception e) {
            LOGGER.error("Failed to create location", e);
            throw new IllegalArgumentException("Failed to create location", e);
        }
    }

    /**
     * Update an existing location.
     *
     * @param locationId the id of the location to update
     * @param locationDto the location to update
     * @return the updated location
     */
    @Override
    public LocationDto updateLocation(int locationId, LocationDto locationDto) {
        return locationRepository.findById(locationId)
                .map(location -> {
                    try {
                        location.setLocationName(locationDto.getLocationName());
                        location.setLocale(locationDto.getLocale());
                        location.setLocalePreferred(locationDto.isLocalePreferred());
                        location.setLocationCode(locationDto.getLocationCode());
                        location.setLocationGeoCode(locationDto.getLocationGeoCode());
                        location.setLocationPhoneCode(locationDto.getLocationPhoneCode());
                        location.setLastModifiedBy(CurrentUser.getCurrentUser().getPersonId());
                        location.setLastModifiedAt(LocalDateTime.now());
                        return locationMapper.toDto(locationRepository.save(location));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to update location: " + e.getMessage(), e);
                    }
                }).orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + locationId));
    }

    /**
     * Delete a location.
     *
     * @param id the id of the location to delete
     * @param recordVoidRequest the record void request
     */
    @Override
    public void deleteLocation(int id, RecordVoidRequest recordVoidRequest) {
        locationRepository.findById(id)
                .map(location -> {
                    try {
                        location.setVoided(true);
                        location.setVoidedAt(LocalDateTime.now());
                        location.setVoidedBy(CurrentUser.getCurrentUser().getPersonId());
                        location.setVoidReason(recordVoidRequest.getVoidReason());
                        return locationMapper.toDto(locationRepository.save(location));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to delete location: " + e.getMessage(), e);
                    }
                }).orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
    }

    /**
     * Get a location by its id.
     *
     * @param id the id of the location to get
     * @return the location
     */
    @Override
    public LocationDto getLocation(int id) {
        return locationMapper.toDto(locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id)));
    }

    /**
     * Get all locations.   
     *
     * @return a list of locations
     */
    @Override
    public List<LocationDto> getAllLocations() {
        return locationRepository.findAll().stream().map(locationMapper::toDto).toList();
    }
} 