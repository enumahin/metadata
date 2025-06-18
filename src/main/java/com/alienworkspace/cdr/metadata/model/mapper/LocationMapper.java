package com.alienworkspace.cdr.metadata.model.mapper;

import com.alienworkspace.cdr.metadata.model.Location;
import com.alienworkspace.cdr.metadata.model.audit.AuditTrailMapper;
import com.alienworkspace.cdr.model.dto.metadata.CommunityDto;
import com.alienworkspace.cdr.model.dto.metadata.LocationDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for Location entity and LocationDto.
 * Provides methods to map between Location and LocationDto.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Mapper(componentModel = "spring")
public interface LocationMapper {
    /**
     * Singleton instance for manual usage.
     */
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    /**
     * Maps a LocationDto to a Location entity.
     *
     * @param dto the LocationDto to map
     * @return the mapped Location entity
     */
    default Location toEntity(LocationDto dto) {
        Location entity = Location.builder()
                .locationId(dto.getLocationId())
                .locationName(dto.getLocationName())
                .locale(dto.getLocale())
                .localePreferred(dto.isLocalePreferred())
                .locationCode(dto.getLocationCode())
                .locationGeoCode(dto.getLocationGeoCode())
                .locationPhoneCode(dto.getLocationPhoneCode())
                .community(CommunityMapper.INSTANCE.toEntity(dto.getCommunity())) // Map if needed
                .build();
        AuditTrailMapper.mapFromDto(dto, entity);
        return entity;
    }

    /**
     * Maps a Location entity to a LocationDto.
     *
     * @param entity the Location entity to map
     * @return the mapped LocationDto
     */
    default LocationDto toDto(Location entity) {
        LocationDto dto = LocationDto.builder()
                .locationId(entity.getLocationId())
                .locationName(entity.getLocationName())
                .locale(entity.getLocale())
                .localePreferred(entity.isLocalePreferred())
                .locationCode(entity.getLocationCode())
                .locationGeoCode(entity.getLocationGeoCode())
                .locationPhoneCode(entity.getLocationPhoneCode())
                .community(entity.getCommunity() != null ? CommunityDto.builder()
                        .communityId(entity.getCommunity().getCommunityId())
                        .communityName(entity.getCommunity().getCommunityName())
                        .locale(entity.getCommunity().getLocale())
                        .localePreferred(entity.getCommunity().isLocalePreferred())
                        .communityCode(entity.getCommunity().getCommunityCode())
                        .communityGeoCode(entity.getCommunity().getCommunityGeoCode())
                        .communityPhoneCode(entity.getCommunity().getCommunityPhoneCode())
                        .build() : null) // Map if needed
                .build();
        AuditTrailMapper.mapToDto(entity, dto);
        return dto;
    }
} 