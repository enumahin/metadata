package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.metadata.model.City;
import com.alienworkspace.cdr.metadata.model.mapper.CityMapper;
import com.alienworkspace.cdr.metadata.repository.CityRepository;
import com.alienworkspace.cdr.metadata.service.impl.CityServiceImpl;
import com.alienworkspace.cdr.model.dto.metadata.CityDto;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {
    @Mock
    CityRepository cityRepository;
    @Mock
    CityMapper cityMapper;
    @InjectMocks
    CityServiceImpl cityService;

    private final CityDto.CityDtoBuilder cityDtoBuilder = CityDto.builder();
    private final City.CityBuilder cityBuilder = City.builder();

    @BeforeEach
    public void setUp() {
        cityDtoBuilder
                .cityId(1)
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .cityCode("CIT123")
                .cityGeoCode("GEO123")
                .cityPhoneCode(234);
        cityBuilder
                .cityId(1)
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .cityCode("CIT123")
                .cityGeoCode("GEO123")
                .cityPhoneCode(234);
    }

    @DisplayName("Test Create City")
    @Test
    public void testCreateCity() {
        CityDto cityDto = cityDtoBuilder.build();
        City city = cityBuilder.build();
        City savedCity = cityBuilder.build();
        when(cityMapper.toEntity(cityDto)).thenReturn(city);
        when(cityRepository.save(city)).thenReturn(savedCity);
        when(cityMapper.toDto(savedCity)).thenReturn(cityDto);
        CityDto actual = cityService.createCity(cityDto);
        assertNotNull(actual);
        assertEquals(cityDto.getCityId(), actual.getCityId());
        verify(cityRepository).save(cityMapper.toEntity(cityDto));
        verifyNoMoreInteractions(cityRepository);
    }

    @DisplayName("Test Update City")
    @Test
    public void testUpdateCity() {
        CityDto cityDto = cityDtoBuilder.build();
        City existingCity = cityBuilder.build();
        City updatedCity = cityBuilder.build();
        updatedCity.setCityName("Updated");
        CityDto expectedDto = cityDtoBuilder.cityName("Updated").build();
        when(cityRepository.findById(cityDto.getCityId())).thenReturn(Optional.of(existingCity));
        when(cityRepository.save(existingCity)).thenReturn(updatedCity);
        when(cityMapper.toDto(updatedCity)).thenReturn(expectedDto);
        CityDto actual = cityService.updateCity(cityDto.getCityId(), cityDtoBuilder.cityName("Updated").build());
        assertNotNull(actual);
        assertEquals("Updated", actual.getCityName());
        verify(cityRepository).save(existingCity);
        verifyNoMoreInteractions(cityRepository);
    }

    @DisplayName("Test Delete City")
    @Test
    public void testDeleteCity() {
        RecordVoidRequest request = new RecordVoidRequest();
        request.setVoidReason("Test");
        City existingCity = cityBuilder.build();
        City voidedCity = cityBuilder.build();
        voidedCity.setVoided(true);
        voidedCity.setVoidedBy(3L);
        voidedCity.setVoidedAt(LocalDateTime.now());
        when(cityRepository.findById(1)).thenReturn(Optional.of(existingCity));
        when(cityRepository.save(existingCity)).thenReturn(voidedCity);
        when(cityMapper.toDto(voidedCity)).thenReturn(cityDtoBuilder.build());
        cityService.deleteCity(1, request);
        verify(cityRepository).save(existingCity);
        verifyNoMoreInteractions(cityRepository);
        assertTrue(existingCity.isVoided());
        assertNotNull(existingCity.getVoidedAt());
    }

    @DisplayName("Test Get City")
    @Test
    public void testGetCity() {
        int id = 1;
        City existingCity = cityBuilder.build();
        CityDto expectedDto = cityDtoBuilder.build();
        when(cityRepository.findById(id)).thenReturn(Optional.of(existingCity));
        when(cityMapper.toDto(existingCity)).thenReturn(expectedDto);
        CityDto actual = cityService.getCity(id);
        assertNotNull(actual);
        assertEquals(expectedDto, actual);
        verify(cityRepository).findById(id);
        verifyNoMoreInteractions(cityRepository);
    }

    @DisplayName("Test Get All Cities")
    @Test
    public void testGetAllCities() {
        List<City> cities = List.of(cityBuilder.build());
        List<CityDto> expectedDtos = List.of(cityDtoBuilder.build());
        when(cityRepository.findAll()).thenReturn(cities);
        when(cityMapper.toDto(any(City.class))).thenReturn(cityDtoBuilder.build());
        List<CityDto> actual = cityService.getAllCities();
        assertNotNull(actual);
        assertEquals(expectedDtos.size(), actual.size());
        verify(cityRepository).findAll();
        verifyNoMoreInteractions(cityRepository);
    }
} 