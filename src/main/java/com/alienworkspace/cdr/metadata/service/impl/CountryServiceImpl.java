package com.alienworkspace.cdr.metadata.service.impl;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.helpers.CurrentUser;
import com.alienworkspace.cdr.metadata.model.mapper.CountryMapper;
import com.alienworkspace.cdr.metadata.repository.CountryRepository;
import com.alienworkspace.cdr.metadata.service.CountryService;
import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Country service implementation.
 *
 * @author Ikenumah
 * @version 1.0
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    /**
     * Find a country by its country code.
     *
     * @param countryCode the country code
     * @return the country
     */
    @Transactional
    @Override
    public CountryDto findByCountryCode(String countryCode) {
        return countryRepository.findByCountryCode(countryCode)
                .map(countryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));
    }

    /**
     * Create a new country.
     *
     * @param countryDto the country to create
     * @return the created country
     */
    @Transactional
    @Override
    public CountryDto createCountry(CountryDto countryDto) {
        return countryMapper.toDto(countryRepository.save(countryMapper.toEntity(countryDto)));
    }

    /**
     * Update an existing country.
     *
     * @param countryDto the country to update
     * @return the updated country
     */
    @Transactional
    @Override
    public CountryDto updateCountry(int countryId, CountryDto countryDto) {
        return countryRepository.findById(countryId)
                .map(country -> {
                    country.setCountryCode(countryDto.getCountryCode());
                    country.setCountryName(countryDto.getCountryName());
                    country.setCountryPhoneCode(countryDto.getCountryPhoneCode());
                    country.setLocale(countryDto.getLocale());
                    country.setCurrencyCode(countryDto.getCurrencyCode());
                    country.setCurrencySymbol(countryDto.getCurrencySymbol());
                    country.setCurrencyName(countryDto.getCurrencyName());
                    country.setCountryGeoCode(countryDto.getCountryGeoCode());
                    country.setLocalePreferred(countryDto.isLocalePreferred());
                    country.setLastModifiedBy(CurrentUser.getCurrentUser().getPersonId());
                    country.setLastModifiedAt(LocalDateTime.now());
                    return countryRepository.save(country);
                })
                .map(countryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));
    }

    /**
     * Get a country by its id.
     *
     * @param countryId the id of the country to get
     * @return the country
     */
    @Transactional
    @Override
    public CountryDto getCountry(int countryId) {
        return countryRepository.findById(countryId)
                .map(countryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));
    }

    /**
     * Get all countries.
     *
     * @return the list of countries
     */
    @Transactional
    @Override
    public List<CountryDto> getAllCountries() {
        return countryRepository.findAll().stream().map(countryMapper::toDto).toList();
    }

    /**
     * Delete an existing country.
     *
     * @param countryId the id of the country to delete
     */
    @Transactional
    @Override
    public void deleteCountry(int countryId, RecordVoidRequest request) {
        countryRepository.findById(countryId)
                .map(country -> {
                    country.setVoided(true);
                    country.setVoidedAt(LocalDateTime.now());
                    country.setVoidedBy(CurrentUser.getCurrentUser().getPersonId());
                    country.setVoidReason(request.getVoidReason());
                    return countryRepository.save(country);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));
    }
}
