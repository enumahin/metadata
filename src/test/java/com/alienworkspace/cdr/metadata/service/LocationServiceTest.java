package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.metadata.model.Location;
import com.alienworkspace.cdr.metadata.model.mapper.LocationMapper;
import com.alienworkspace.cdr.metadata.repository.LocationRepository;
import com.alienworkspace.cdr.metadata.service.impl.LocationServiceImpl;
import com.alienworkspace.cdr.model.dto.metadata.LocationDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {
    @Mock
    LocationRepository locationRepository;
    @Mock
    LocationMapper locationMapper;
    @InjectMocks
    LocationServiceImpl locationService;

    private final LocationDto.LocationDtoBuilder locationDtoBuilder = LocationDto.builder();
    private final Location.LocationBuilder locationBuilder = Location.builder();

    @BeforeEach
    public void setUp() {
        locationDtoBuilder
                .locationId(1)
                .locationName("Test Location")
                .locale("en_US")
                .localePreferred(true)
                .locationCode("LOC123")
                .locationGeoCode("GEO123")
                .locationPhoneCode(234);
        locationBuilder
                .locationId(1)
                .locationName("Test Location")
                .locale("en_US")
                .localePreferred(true)
                .locationCode("LOC123")
                .locationGeoCode("GEO123")
                .locationPhoneCode(234);
    }

    @DisplayName("Test Create Location")
    @Test
    public void testCreateLocation() {
        LocationDto locationDto = locationDtoBuilder.build();
        Location location = locationBuilder.build();
        Location savedLocation = locationBuilder.build();
        when(locationMapper.toEntity(locationDto)).thenReturn(location);
        when(locationRepository.save(location)).thenReturn(savedLocation);
        when(locationMapper.toDto(savedLocation)).thenReturn(locationDto);
        LocationDto actual = locationService.createLocation(locationDto);
        assertNotNull(actual);
        assertEquals(locationDto.getLocationId(), actual.getLocationId());
        verify(locationRepository).save(locationMapper.toEntity(locationDto));
        verifyNoMoreInteractions(locationRepository);
    }

    @DisplayName("Test Update Location")
    @Test
    public void testUpdateLocation() {
        LocationDto locationDto = locationDtoBuilder.build();
        Location existingLocation = locationBuilder.build();
        Location updatedLocation = locationBuilder.build();
        updatedLocation.setLocationName("Updated");
        LocationDto expectedDto = locationDtoBuilder.locationName("Updated").build();
        when(locationRepository.findById(locationDto.getLocationId())).thenReturn(Optional.of(existingLocation));
        when(locationRepository.save(existingLocation)).thenReturn(updatedLocation);
        when(locationMapper.toDto(updatedLocation)).thenReturn(expectedDto);
        LocationDto actual = locationService.updateLocation(locationDto.getLocationId(), locationDtoBuilder.locationName("Updated").build());
        assertNotNull(actual);
        assertEquals("Updated", actual.getLocationName());
        verify(locationRepository).save(existingLocation);
        verifyNoMoreInteractions(locationRepository);
    }

    @DisplayName("Test Delete Location")
    @Test
    public void testDeleteLocation() {
        RecordVoidRequest request = new RecordVoidRequest();
        request.setVoidReason("Test");
        Location existingLocation = locationBuilder.build();
        Location voidedLocation = locationBuilder.build();
        voidedLocation.setVoided(true);
        voidedLocation.setVoidedBy(3L);
        voidedLocation.setVoidedAt(LocalDateTime.now());
        when(locationRepository.findById(1)).thenReturn(Optional.of(existingLocation));
        when(locationRepository.save(existingLocation)).thenReturn(voidedLocation);
        when(locationMapper.toDto(voidedLocation)).thenReturn(locationDtoBuilder.build());
        locationService.deleteLocation(1, request);
        verify(locationRepository).save(existingLocation);
        verifyNoMoreInteractions(locationRepository);
        assertTrue(existingLocation.isVoided());
        assertNotNull(existingLocation.getVoidedAt());
    }

    @DisplayName("Test Get Location")
    @Test
    public void testGetLocation() {
        int id = 1;
        Location existingLocation = locationBuilder.build();
        LocationDto expectedDto = locationDtoBuilder.build();
        when(locationRepository.findById(id)).thenReturn(Optional.of(existingLocation));
        when(locationMapper.toDto(existingLocation)).thenReturn(expectedDto);
        LocationDto actual = locationService.getLocation(id);
        assertNotNull(actual);
        assertEquals(expectedDto, actual);
        verify(locationRepository).findById(id);
        verifyNoMoreInteractions(locationRepository);
    }

    @DisplayName("Test Get All Locations")
    @Test
    public void testGetAllLocations() {
        List<Location> locations = List.of(locationBuilder.build());
        List<LocationDto> expectedDtos = List.of(locationDtoBuilder.build());
        when(locationRepository.findAll()).thenReturn(locations);
        when(locationMapper.toDto(any(Location.class))).thenReturn(locationDtoBuilder.build());
        List<LocationDto> actual = locationService.getAllLocations();
        assertNotNull(actual);
        assertEquals(expectedDtos.size(), actual.size());
        verify(locationRepository).findAll();
        verifyNoMoreInteractions(locationRepository);
    }
} 