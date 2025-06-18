package com.alienworkspace.cdr.metadata.model.mapper;

import com.alienworkspace.cdr.metadata.model.State;
import com.alienworkspace.cdr.metadata.model.audit.AuditTrailMapper;
import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
import com.alienworkspace.cdr.model.dto.metadata.StateDto;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for State entity and StateDto.
 * Provides methods to map between State and StateDto.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Mapper(componentModel = "spring")
public interface StateMapper {
    /**
     * Singleton instance for manual usage.
     */
    StateMapper INSTANCE = Mappers.getMapper(StateMapper.class);

    /**
     * Maps a StateDto to a State entity.
     *
     * @param dto the StateDto to map
     * @return the mapped State entity
     */
    default State toEntity(StateDto dto) {
        State entity = State.builder()
                .stateId(dto.getStateId())
                .stateName(dto.getStateName())
                .locale(dto.getLocale())
                .localePreferred(dto.isLocalePreferred())
                .stateCode(dto.getStateCode())
                .stateGeoCode(dto.getStateGeoCode())
                .statePhoneCode(dto.getStatePhoneCode())
                .country(dto.getCountry() != null ? CountryMapper.INSTANCE.toEntity(dto.getCountry()) : null)
                .counties(dto.getCounties().stream().map(CountyMapper.INSTANCE::toEntity).collect(Collectors.toSet()))
                .build();
        AuditTrailMapper.mapFromDto(dto, entity);
        return entity;
    }

    /**
     * Maps a State entity to a StateDto.
     *
     * @param entity the State entity to map
     * @return the mapped StateDto
     */
    default StateDto toDto(State entity) {
        StateDto dto = StateDto.builder()
                .stateId(entity.getStateId())
                .stateName(entity.getStateName())
                .locale(entity.getLocale())
                .localePreferred(entity.isLocalePreferred())
                .stateCode(entity.getStateCode())
                .stateGeoCode(entity.getStateGeoCode())
                .statePhoneCode(entity.getStatePhoneCode())
                .country(entity.getCountry() != null ? CountryDto.builder()
                        .countryId(entity.getCountry().getCountryId())
                        .countryCode(entity.getCountry().getCountryCode())
                        .countryName(entity.getCountry().getCountryName())
                        .build() : null)
                .counties(entity.getCounties().stream().map(CountyMapper.INSTANCE::toDto).collect(Collectors.toSet()))
                .build();
        AuditTrailMapper.mapToDto(entity, dto);
        return dto;
    }
} 