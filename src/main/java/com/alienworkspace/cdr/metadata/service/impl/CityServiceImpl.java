package com.alienworkspace.cdr.metadata.service.impl;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.helpers.CurrentUser;
import com.alienworkspace.cdr.metadata.model.mapper.CityMapper;
import com.alienworkspace.cdr.metadata.repository.CityRepository;
import com.alienworkspace.cdr.metadata.service.CityService;
import com.alienworkspace.cdr.model.dto.metadata.CityDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the CityService interface.
 *
 * @author Ikenumah
 * @version 1.0
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CityServiceImpl.class);
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    /**
     * Create a new city.
     *
     * @param cityDto the city to create
     * @return the created city
     */
    @Transactional
    @Override
    public CityDto createCity(CityDto cityDto) {
        try {
            return cityMapper.toDto(cityRepository.save(cityMapper.toEntity(cityDto)));
        } catch (Exception e) {
            LOGGER.error("Failed to create city", e);
            throw new IllegalArgumentException("Failed to create city", e);
        }
    }

    /**
     * Update an existing city.
     *
     * @param cityId the id of the city to update
     * @param cityDto the city to update
     * @return the updated city
     */
    @Transactional
    @Override
    public CityDto updateCity(int cityId, CityDto cityDto) {
        return cityRepository.findById(cityId)
                .map(city -> {
                    try {
                        city.setCityName(cityDto.getCityName());
                        city.setLocale(cityDto.getLocale());
                        city.setLocalePreferred(cityDto.isLocalePreferred());
                        city.setCityCode(cityDto.getCityCode());
                        city.setCityGeoCode(cityDto.getCityGeoCode());
                        city.setCityPhoneCode(cityDto.getCityPhoneCode());
                        city.setLastModifiedBy(CurrentUser.getCurrentUser().getPersonId());
                        city.setLastModifiedAt(LocalDateTime.now());
                        return cityMapper.toDto(cityRepository.save(city));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to update city: " + e.getMessage(), e);
                    }
                }).orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + cityId));
    }

    /**
     * Delete a city.
     *
     * @param id the id of the city to delete
     * @param recordVoidRequest the record void request
     */
    @Transactional
    @Override
    public void deleteCity(int id, RecordVoidRequest recordVoidRequest) {
        cityRepository.findById(id)
                .map(city -> {
                    try {
                        city.setVoided(true);
                        city.setVoidedAt(LocalDateTime.now());
                        city.setVoidedBy(CurrentUser.getCurrentUser().getPersonId());
                        city.setVoidReason(recordVoidRequest.getVoidReason());
                        return cityMapper.toDto(cityRepository.save(city));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to delete city: " + e.getMessage(), e);
                    }
                }).orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
    }

    /**
     * Get a city by its id.
     *
     * @param id the id of the city to get
     * @return the city
     */
    @Transactional
    @Override
    public CityDto getCity(int id) {
        return cityMapper.toDto(cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id)));
    }

    /**
     * Get all cities.
     *
     * @return a list of cities
     */
    @Transactional
    @Override
    public List<CityDto> getAllCities() {
        return cityRepository.findAll().stream().map(cityMapper::toDto).toList();
    }
} 