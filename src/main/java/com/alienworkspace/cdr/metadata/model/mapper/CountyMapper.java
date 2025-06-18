package com.alienworkspace.cdr.metadata.model.mapper;

import com.alienworkspace.cdr.metadata.model.County;
import com.alienworkspace.cdr.metadata.model.audit.AuditTrailMapper;
import com.alienworkspace.cdr.model.dto.metadata.CountyDto;
import com.alienworkspace.cdr.model.dto.metadata.StateDto;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for County entity and CountyDto.
 * Provides methods to map between County and CountyDto.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Mapper(componentModel = "spring")
public interface CountyMapper {
    /**
     * Singleton instance for manual usage.
     */
    CountyMapper INSTANCE = Mappers.getMapper(CountyMapper.class);

    /**
     * Maps a CountyDto to a County entity.
     *
     * @param dto the CountyDto to map
     * @return the mapped County entity
     */
    default County toEntity(CountyDto dto) {
        County entity = County.builder()
                .countyId(dto.getCountyId())
                .countyName(dto.getCountyName())
                .locale(dto.getLocale())
                .state(dto.getState() != null ? StateMapper.INSTANCE.toEntity(dto.getState()) : null)
                .cities(dto.getCities().stream()
                        .map(CityMapper.INSTANCE::toEntity).collect(Collectors.toSet()))
                .localePreferred(dto.isLocalePreferred())
                .countyCode(dto.getCountyCode())
                .countyGeoCode(dto.getCountyGeoCode())
                .countyPhoneCode(dto.getCountyPhoneCode())
                .build();
        AuditTrailMapper.mapFromDto(dto, entity);
        return entity;
    }

    /**
     * Maps a County entity to a CountyDto.
     *
     * @param entity the County entity to map
     * @return the mapped CountyDto
     */
    default CountyDto toDto(County entity) {
        CountyDto dto = CountyDto.builder()
                .countyId(entity.getCountyId())
                .countyName(entity.getCountyName())
                .locale(entity.getLocale())
                .state(entity.getState() != null ? StateDto.builder()
                        .stateId(entity.getState().getStateId())
                        .stateName(entity.getState().getStateName())
                        .locale(entity.getState().getLocale())
                        .localePreferred(entity.getState().isLocalePreferred())
                        .stateCode(entity.getState().getStateCode())
                        .stateGeoCode(entity.getState().getStateGeoCode())
                        .statePhoneCode(entity.getState().getStatePhoneCode())
                        .build() : null)
                .cities(entity.getCities().stream()
                        .map(CityMapper.INSTANCE::toDto).collect(Collectors.toSet()))
                .localePreferred(entity.isLocalePreferred())
                .countyCode(entity.getCountyCode())
                .countyGeoCode(entity.getCountyGeoCode())
                .countyPhoneCode(entity.getCountyPhoneCode())
                .build();
        AuditTrailMapper.mapToDto(entity, dto);
        return dto;
    }
} 