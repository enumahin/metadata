package com.alienworkspace.cdr.metadata.model.mapper;

import com.alienworkspace.cdr.metadata.model.Country;
import com.alienworkspace.cdr.metadata.model.audit.AuditTrailMapper;
import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for converting between Country and CountryDto.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Mapper(componentModel = "spring")
public interface CountryMapper {

    CountryMapper INSTANCE = Mappers.getMapper(CountryMapper.class);

    /**
     * Convert a Country entity to a CountryDto.
     *
     * @param country the Country entity
     * @return the CountryDto
     */
    default CountryDto toDto(Country country) {
        CountryDto countryDto = CountryDto.builder()
            .countryId(country.getCountryId())
            .countryCode(country.getCountryCode())
            .countryName(country.getCountryName())
            .countryPhoneCode(country.getCountryPhoneCode())
            .states(country.getStates().stream().map(StateMapper.INSTANCE::toDto).collect(Collectors.toSet()))
            .currencyCode(country.getCurrencyCode())
            .currencySymbol(country.getCurrencySymbol())
            .currencyName(country.getCurrencyName())
            .countryGeoCode(country.getCountryGeoCode())
            .locale(country.getLocale())
            .localePreferred(country.isLocalePreferred())
            .build();

        AuditTrailMapper.mapToDto(country, countryDto);

        return countryDto;
    }

    /**
     * Convert a CountryDto to a Country entity.
     *
     * @param countryDto the CountryDto
     * @return the Country entity
     */
    default Country toEntity(CountryDto countryDto) {
        Country country = Country.builder()
                .countryId(countryDto.getCountryId())
                .countryCode(countryDto.getCountryCode())
                .countryName(countryDto.getCountryName())
                .countryPhoneCode(countryDto.getCountryPhoneCode())
                .locale(countryDto.getLocale())
                .states(countryDto.getStates().stream().map(StateMapper.INSTANCE::toEntity).collect(Collectors.toSet()))
                .currencyCode(countryDto.getCurrencyCode())
                .currencySymbol(countryDto.getCurrencySymbol())
                .currencyName(countryDto.getCurrencyName())
                .localePreferred(countryDto.isLocalePreferred())
                .countryGeoCode(countryDto.getCountryGeoCode())
                .build();

        AuditTrailMapper.mapFromDto(countryDto, country);

        return country;
    }
} 