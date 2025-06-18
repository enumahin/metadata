package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.metadata.model.County;
import com.alienworkspace.cdr.metadata.model.mapper.CountyMapper;
import com.alienworkspace.cdr.metadata.repository.CountyRepository;
import com.alienworkspace.cdr.metadata.service.impl.CountyServiceImpl;
import com.alienworkspace.cdr.model.dto.metadata.CountyDto;
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
public class CountyServiceTest {
    @Mock
    CountyRepository countyRepository;
    @Mock
    CountyMapper countyMapper;
    @InjectMocks
    CountyServiceImpl countyService;

    private final CountyDto.CountyDtoBuilder countyDtoBuilder = CountyDto.builder();
    private final County.CountyBuilder countyBuilder = County.builder();

    @BeforeEach
    public void setUp() {
        countyDtoBuilder
                .countyId(1)
                .countyName("Test County")
                .locale("en_US")
                .localePreferred(true)
                .countyCode("COU123")
                .countyGeoCode("GEO123")
                .countyPhoneCode(234);
        countyBuilder
                .countyId(1)
                .countyName("Test County")
                .locale("en_US")
                .localePreferred(true)
                .countyCode("COU123")
                .countyGeoCode("GEO123")
                .countyPhoneCode(234);
    }

    @DisplayName("Test Create County")
    @Test
    public void testCreateCounty() {
        CountyDto countyDto = countyDtoBuilder.build();
        County county = countyBuilder.build();
        County savedCounty = countyBuilder.build();
        when(countyMapper.toEntity(countyDto)).thenReturn(county);
        when(countyRepository.save(county)).thenReturn(savedCounty);
        when(countyMapper.toDto(savedCounty)).thenReturn(countyDto);
        CountyDto actual = countyService.createCounty(countyDto);
        assertNotNull(actual);
        assertEquals(countyDto.getCountyId(), actual.getCountyId());
        verify(countyRepository).save(countyMapper.toEntity(countyDto));
        verifyNoMoreInteractions(countyRepository);
    }

    @DisplayName("Test Update County")
    @Test
    public void testUpdateCounty() {
        CountyDto countyDto = countyDtoBuilder.build();
        County existingCounty = countyBuilder.build();
        County updatedCounty = countyBuilder.build();
        updatedCounty.setCountyName("Updated");
        CountyDto expectedDto = countyDtoBuilder.countyName("Updated").build();
        when(countyRepository.findById(countyDto.getCountyId())).thenReturn(Optional.of(existingCounty));
        when(countyRepository.save(existingCounty)).thenReturn(updatedCounty);
        when(countyMapper.toDto(updatedCounty)).thenReturn(expectedDto);
        CountyDto actual = countyService.updateCounty(countyDto.getCountyId(), countyDtoBuilder.countyName("Updated").build());
        assertNotNull(actual);
        assertEquals("Updated", actual.getCountyName());
        verify(countyRepository).save(existingCounty);
        verifyNoMoreInteractions(countyRepository);
    }

    @DisplayName("Test Delete County")
    @Test
    public void testDeleteCounty() {
        RecordVoidRequest request = new RecordVoidRequest();
        request.setVoidReason("Test");
        County existingCounty = countyBuilder.build();
        County voidedCounty = countyBuilder.build();
        voidedCounty.setVoided(true);
        voidedCounty.setVoidedBy(3L);
        voidedCounty.setVoidedAt(LocalDateTime.now());
        when(countyRepository.findById(1)).thenReturn(Optional.of(existingCounty));
        when(countyRepository.save(existingCounty)).thenReturn(voidedCounty);
        when(countyMapper.toDto(voidedCounty)).thenReturn(countyDtoBuilder.build());
        countyService.deleteCounty(1, request);
        verify(countyRepository).save(existingCounty);
        verifyNoMoreInteractions(countyRepository);
        assertTrue(existingCounty.isVoided());
        assertNotNull(existingCounty.getVoidedAt());
    }

    @DisplayName("Test Get County")
    @Test
    public void testGetCounty() {
        int id = 1;
        County existingCounty = countyBuilder.build();
        CountyDto expectedDto = countyDtoBuilder.build();
        when(countyRepository.findById(id)).thenReturn(Optional.of(existingCounty));
        when(countyMapper.toDto(existingCounty)).thenReturn(expectedDto);
        CountyDto actual = countyService.getCounty(id);
        assertNotNull(actual);
        assertEquals(expectedDto, actual);
        verify(countyRepository).findById(id);
        verifyNoMoreInteractions(countyRepository);
    }

    @DisplayName("Test Get All Counties")
    @Test
    public void testGetAllCounties() {
        List<County> counties = List.of(countyBuilder.build());
        List<CountyDto> expectedDtos = List.of(countyDtoBuilder.build());
        when(countyRepository.findAll()).thenReturn(counties);
        when(countyMapper.toDto(any(County.class))).thenReturn(countyDtoBuilder.build());
        List<CountyDto> actual = countyService.getAllCounties();
        assertNotNull(actual);
        assertEquals(expectedDtos.size(), actual.size());
        verify(countyRepository).findAll();
        verifyNoMoreInteractions(countyRepository);
    }
} 