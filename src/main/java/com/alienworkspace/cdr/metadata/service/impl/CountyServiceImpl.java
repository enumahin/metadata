package com.alienworkspace.cdr.metadata.service.impl;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.helpers.CurrentUser;
import com.alienworkspace.cdr.metadata.model.mapper.CountyMapper;
import com.alienworkspace.cdr.metadata.repository.CountyRepository;
import com.alienworkspace.cdr.metadata.service.CountyService;
import com.alienworkspace.cdr.model.dto.metadata.CountyDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the CountyService interface.
 *
 * @author Ikenumah
 * @version 1.0
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class CountyServiceImpl implements CountyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CountyServiceImpl.class);
    private final CountyRepository countyRepository;
    private final CountyMapper countyMapper;

    /**
     * Create a new county.
     *
     * @param countyDto the county to create
     * @return the created county
     */
    @Transactional
    @Override
    public CountyDto createCounty(CountyDto countyDto) {
        try {
            return countyMapper.toDto(countyRepository.save(countyMapper.toEntity(countyDto)));
        } catch (Exception e) {
            LOGGER.error("Failed to create county", e);
            throw new IllegalArgumentException("Failed to create county", e);
        }
    }

    /**
     * Update an existing county.
     *
     * @param countyId the id of the county to update
     * @param countyDto the county to update
     * @return the updated county
     */
    @Transactional
    @Override
    public CountyDto updateCounty(int countyId, CountyDto countyDto) {
        return countyRepository.findById(countyId)
                .map(county -> {
                    try {
                        county.setCountyName(countyDto.getCountyName());
                        county.setLocale(countyDto.getLocale());
                        county.setLocalePreferred(countyDto.isLocalePreferred());
                        county.setCountyCode(countyDto.getCountyCode());
                        county.setCountyGeoCode(countyDto.getCountyGeoCode());
                        county.setCountyPhoneCode(countyDto.getCountyPhoneCode());
                        county.setLastModifiedBy(CurrentUser.getCurrentUser().getPersonId());
                        county.setLastModifiedAt(LocalDateTime.now());
                        return countyMapper.toDto(countyRepository.save(county));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to update county: " + e.getMessage(), e);
                    }
                }).orElseThrow(() -> new ResourceNotFoundException("County not found with id: " + countyId));
    }

    /**
     * Delete a county.
     *
     * @param id the id of the county to delete
     * @param recordVoidRequest the record void request
     */
    @Transactional
    @Override
    public void deleteCounty(int id, RecordVoidRequest recordVoidRequest) {
        countyRepository.findById(id)
                .map(county -> {
                    try {
                        county.setVoided(true);
                        county.setVoidedAt(LocalDateTime.now());
                        county.setVoidedBy(CurrentUser.getCurrentUser().getPersonId());
                        county.setVoidReason(recordVoidRequest.getVoidReason());
                        return countyMapper.toDto(countyRepository.save(county));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to delete county: " + e.getMessage(), e);
                    }
                }).orElseThrow(() -> new ResourceNotFoundException("County not found with id: " + id));
    }

    /**
     * Get a county by its id.
     *
     * @param id the id of the county to get
     * @return the county
     */
    @Transactional
    @Override
    public CountyDto getCounty(int id) {
        return countyMapper.toDto(countyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("County not found with id: " + id)));
    }

    /**
     * Get all counties.
     *
     * @return a list of counties
     */
    @Transactional
    @Override
    public List<CountyDto> getAllCounties() {
        return countyRepository.findAll().stream().map(countyMapper::toDto).toList();
    }
} 