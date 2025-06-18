package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.metadata.model.Country;
import com.alienworkspace.cdr.metadata.model.mapper.CountryMapper;
import com.alienworkspace.cdr.metadata.repository.CountryRepository;
import com.alienworkspace.cdr.metadata.service.impl.CountryServiceImpl;
import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
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
public class CountryServiceTest {
    @Mock
    CountryRepository countryRepository;
    @Mock
    CountryMapper countryMapper;
    @InjectMocks
    CountryServiceImpl countryService;

    private final CountryDto.CountryDtoBuilder countryDtoBuilder = CountryDto.builder();
    private final Country.CountryBuilder countryBuilder = Country.builder();

    @BeforeEach
    public void setUp() {
        countryDtoBuilder
                .countryId(1)
                .countryName("Test Country")
                .countryCode("CTR123")
                .locale("en_US")
                .localePreferred(true)
                .countryPhoneCode(234)
                .currencyCode("USD")
                .currencySymbol("$")
                .currencyName("Dollar")
                .countryGeoCode("GEO123");
        countryBuilder
                .countryId(1)
                .countryName("Test Country")
                .countryCode("CTR123")
                .locale("en_US")
                .localePreferred(true)
                .countryPhoneCode(234)
                .currencyCode("USD")
                .currencySymbol("$")
                .currencyName("Dollar")
                .countryGeoCode("GEO123");
    }

    @DisplayName("Test Create Country")
    @Test
    public void testCreateCountry() {
        CountryDto countryDto = countryDtoBuilder.build();
        Country country = countryBuilder.build();
        Country savedCountry = countryBuilder.build();
        when(countryMapper.toEntity(countryDto)).thenReturn(country);
        when(countryRepository.save(country)).thenReturn(savedCountry);
        when(countryMapper.toDto(savedCountry)).thenReturn(countryDto);
        CountryDto actual = countryService.createCountry(countryDto);
        assertNotNull(actual);
        assertEquals(countryDto.getCountryId(), actual.getCountryId());
        verify(countryRepository).save(countryMapper.toEntity(countryDto));
        verifyNoMoreInteractions(countryRepository);
    }

    @DisplayName("Test Update Country")
    @Test
    public void testUpdateCountry() {
        CountryDto countryDto = countryDtoBuilder.build();
        Country existingCountry = countryBuilder.build();
        Country updatedCountry = countryBuilder.build();
        updatedCountry.setCountryName("Updated");
        CountryDto expectedDto = countryDtoBuilder.countryName("Updated").build();
        when(countryRepository.findById(countryDto.getCountryId())).thenReturn(Optional.of(existingCountry));
        when(countryRepository.save(existingCountry)).thenReturn(updatedCountry);
        when(countryMapper.toDto(updatedCountry)).thenReturn(expectedDto);
        CountryDto actual = countryService.updateCountry(countryDto.getCountryId(), countryDtoBuilder.countryName("Updated").build());
        assertNotNull(actual);
        assertEquals("Updated", actual.getCountryName());
        verify(countryRepository).save(existingCountry);
        verifyNoMoreInteractions(countryRepository);
    }

    @DisplayName("Test Delete Country")
    @Test
    public void testDeleteCountry() {
        RecordVoidRequest request = new RecordVoidRequest();
        request.setVoidReason("Test");
        Country existingCountry = countryBuilder.build();
        Country voidedCountry = countryBuilder.build();
        voidedCountry.setVoided(true);
        voidedCountry.setVoidedBy(3L);
        voidedCountry.setVoidedAt(LocalDateTime.now());
        when(countryRepository.findById(1)).thenReturn(Optional.of(existingCountry));
        when(countryRepository.save(existingCountry)).thenReturn(voidedCountry);
        countryService.deleteCountry(1, request);
        verify(countryRepository).save(existingCountry);
        verifyNoMoreInteractions(countryRepository);
        assertTrue(existingCountry.isVoided());
        assertNotNull(existingCountry.getVoidedAt());
    }

    @DisplayName("Test Get Country")
    @Test
    public void testGetCountry() {
        int id = 1;
        Country existingCountry = countryBuilder.build();
        CountryDto expectedDto = countryDtoBuilder.build();
        when(countryRepository.findById(id)).thenReturn(Optional.of(existingCountry));
        when(countryMapper.toDto(existingCountry)).thenReturn(expectedDto);
        CountryDto actual = countryService.getCountry(id);
        assertNotNull(actual);
        assertEquals(expectedDto, actual);
        verify(countryRepository).findById(id);
        verifyNoMoreInteractions(countryRepository);
    }

    @DisplayName("Test Get Country By Code")
    @Test
    public void testFindByCountryCode() {
        String code = "CTR123";
        Country existingCountry = countryBuilder.build();
        CountryDto expectedDto = countryDtoBuilder.build();
        when(countryRepository.findByCountryCode(code)).thenReturn(Optional.of(existingCountry));
        when(countryMapper.toDto(existingCountry)).thenReturn(expectedDto);
        CountryDto actual = countryService.findByCountryCode(code);
        assertNotNull(actual);
        assertEquals(expectedDto, actual);
        verify(countryRepository).findByCountryCode(code);
        verifyNoMoreInteractions(countryRepository);
    }

    @DisplayName("Test Get All Countries")
    @Test
    public void testGetAllCountries() {
        List<Country> countries = List.of(countryBuilder.build());
        List<CountryDto> expectedDtos = List.of(countryDtoBuilder.build());
        when(countryRepository.findAll()).thenReturn(countries);
        when(countryMapper.toDto(any(Country.class))).thenReturn(countryDtoBuilder.build());
        List<CountryDto> actual = countryService.getAllCountries();
        assertNotNull(actual);
        assertEquals(expectedDtos.size(), actual.size());
        verify(countryRepository).findAll();
        verifyNoMoreInteractions(countryRepository);
    }
} 