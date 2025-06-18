package com.alienworkspace.cdr.metadata.model.mapper;

import com.alienworkspace.cdr.metadata.model.City;
import com.alienworkspace.cdr.metadata.model.audit.AuditTrailMapper;
import com.alienworkspace.cdr.model.dto.metadata.CityDto;
import com.alienworkspace.cdr.model.dto.metadata.CountyDto;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for City entity and CityDto.
 * Provides methods to map between City and CityDto.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Mapper(componentModel = "spring")
public interface CityMapper {
    /**
     * Singleton instance for manual usage.
     */
    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    /**
     * Maps a CityDto to a City entity.
     *
     * @param dto the CityDto to map
     * @return the mapped City entity
     */
    default City toEntity(CityDto dto) {
        City entity = City.builder()
                .cityId(dto.getCityId())
                .cityName(dto.getCityName())
                .locale(dto.getLocale())
                .localePreferred(dto.isLocalePreferred())
                .cityCode(dto.getCityCode())
                .cityGeoCode(dto.getCityGeoCode())
                .cityPhoneCode(dto.getCityPhoneCode())
                .county(dto.getCounty() != null ? CountyMapper.INSTANCE.toEntity(dto.getCounty()) : null)
                .communities(dto.getCommunities().stream()
                        .map(CommunityMapper.INSTANCE::toEntity).collect(Collectors.toSet()))
                .build();
        AuditTrailMapper.mapFromDto(dto, entity);
        return entity;
    }

    /**
     * Maps a City entity to a CityDto.
     *
     * @param entity the City entity to map
     * @return the mapped CityDto
     */
    default CityDto toDto(City entity) {
        CityDto dto = CityDto.builder()
                .cityId(entity.getCityId())
                .cityName(entity.getCityName())
                .locale(entity.getLocale())
                .localePreferred(entity.isLocalePreferred())
                .cityCode(entity.getCityCode())
                .cityGeoCode(entity.getCityGeoCode())
                .cityPhoneCode(entity.getCityPhoneCode())
                .county(entity.getCounty() != null ? CountyDto.builder()
                        .countyId(entity.getCounty().getCountyId())
                        .countyName(entity.getCounty().getCountyName())
                        .locale(entity.getCounty().getLocale())
                        .localePreferred(entity.getCounty().isLocalePreferred())
                        .countyCode(entity.getCounty().getCountyCode())
                        .countyGeoCode(entity.getCounty().getCountyGeoCode())
                        .countyPhoneCode(entity.getCounty().getCountyPhoneCode())
                        .build() : null) // Map if needed
                .communities(entity.getCommunities().stream()
                        .map(CommunityMapper.INSTANCE::toDto).collect(Collectors.toSet()))
                .build();
        AuditTrailMapper.mapToDto(entity, dto);
        return dto;
    }
} 