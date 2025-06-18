package com.alienworkspace.cdr.metadata.model.mapper;

import com.alienworkspace.cdr.metadata.model.Community;
import com.alienworkspace.cdr.metadata.model.audit.AuditTrailMapper;
import com.alienworkspace.cdr.model.dto.metadata.CityDto;
import com.alienworkspace.cdr.model.dto.metadata.CommunityDto;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for Community entity and CommunityDto.
 * Provides methods to map between Community and CommunityDto.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Mapper(componentModel = "spring")
public interface CommunityMapper {
    /**
     * Singleton instance for manual usage.
     */
    CommunityMapper INSTANCE = Mappers.getMapper(CommunityMapper.class);

    /**
     * Maps a CommunityDto to a Community entity.
     *
     * @param dto the CommunityDto to map
     * @return the mapped Community entity
     */
    default Community toEntity(CommunityDto dto) {
        Community entity = Community.builder()
                .communityId(dto.getCommunityId())
                .communityName(dto.getCommunityName())
                .locale(dto.getLocale())
                .localePreferred(dto.isLocalePreferred())
                .communityCode(dto.getCommunityCode())
                .communityGeoCode(dto.getCommunityGeoCode())
                .communityPhoneCode(dto.getCommunityPhoneCode())
                .city(dto.getCity() != null ? CityMapper.INSTANCE.toEntity(dto.getCity()) : null) // Map if needed
                .locations(dto.getLocations().stream()
                        .map(LocationMapper.INSTANCE::toEntity).collect(Collectors.toSet()))
                .build();
        AuditTrailMapper.mapFromDto(dto, entity);
        return entity;
    }

    /**
     * Maps a Community entity to a CommunityDto.
     *
     * @param entity the Community entity to map
     * @return the mapped CommunityDto
     */
    default CommunityDto toDto(Community entity) {
        CommunityDto dto = CommunityDto.builder()
                .communityId(entity.getCommunityId())
                .communityName(entity.getCommunityName())
                .locale(entity.getLocale())
                .localePreferred(entity.isLocalePreferred())
                .communityCode(entity.getCommunityCode())
                .communityGeoCode(entity.getCommunityGeoCode())
                .communityPhoneCode(entity.getCommunityPhoneCode())
                .city(entity.getCity() != null ? CityDto.builder()
                        .cityId(entity.getCity().getCityId()).cityName(entity.getCity().getCityName())
                        .locale(entity.getCity().getLocale())
                        .localePreferred(entity.getCity().isLocalePreferred())
                        .cityCode(entity.getCity().getCityCode())
                        .cityGeoCode(entity.getCity().getCityGeoCode())
                        .cityPhoneCode(entity.getCity().getCityPhoneCode())
                        .build() : null) // Map if needed
                .locations(entity.getLocations().stream()
                        .map(LocationMapper.INSTANCE::toDto).collect(Collectors.toSet()))
                .build();
        AuditTrailMapper.mapToDto(entity, dto);
        return dto;
    }
} 