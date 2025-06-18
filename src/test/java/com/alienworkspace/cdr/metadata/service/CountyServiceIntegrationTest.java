package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.Country;
import com.alienworkspace.cdr.metadata.model.State;
import com.alienworkspace.cdr.metadata.model.mapper.StateMapper;
import com.alienworkspace.cdr.metadata.repository.*;
import com.alienworkspace.cdr.model.dto.metadata.CountyDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CountyServiceIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private CountyService countyService;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountyRepository countyRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private LocationRepository locationRepository;

    Country country;
    State state;

    @BeforeEach
    void setUp() {
        locationRepository.deleteAllInBatch();
        communityRepository.deleteAllInBatch();
        cityRepository.deleteAllInBatch();
        countyRepository.deleteAllInBatch();
        stateRepository.deleteAllInBatch();
        countryRepository.deleteAllInBatch();

        country = countryRepository.save(Country.builder().countryName("Test Country").countryCode("CN123")
                .currencyCode("USD").currencySymbol("$").build());
        state = stateRepository.save(State.builder().stateName("Test State").stateCode("ST123").country(country).build());

    }

    @DisplayName("Test Create County")
    @Test
    public void testCreateCounty() {
        // Arrange
        CountyDto countyDto = CountyDto.builder()
                .countyName("Test County")
                .locale("en_US")
                .localePreferred(true)
                .state(StateMapper.INSTANCE.toDto(state))
                .countyCode("COUNTY128993")
                .countyGeoCode("GEO123")
                .countyPhoneCode(234)
                .build();

        // Act
        CountyDto actualCountyDto = countyService.createCounty(countyDto);

        // Assert
        assertNotNull(actualCountyDto);
        assertEquals(countyDto.getCountyName(), actualCountyDto.getCountyName());
        assertEquals(countyDto.getLocale(), actualCountyDto.getLocale());
        assertEquals(countyDto.isLocalePreferred(), actualCountyDto.isLocalePreferred());
        assertEquals(countyDto.getCountyCode(), actualCountyDto.getCountyCode());
        assertEquals(countyDto.getCountyGeoCode(), actualCountyDto.getCountyGeoCode());
        assertEquals(countyDto.getCountyPhoneCode(), actualCountyDto.getCountyPhoneCode());
        assertNotNull(actualCountyDto.getCreatedAt());
        assertNotNull(actualCountyDto.getCreatedBy());
    }

    @DisplayName("Test Update County")
    @Test
    public void testUpdateCounty() {
        // Arrange
        CountyDto countyDto = CountyDto.builder()
                .countyName("Test County")
                .locale("en_US")
                .localePreferred(true)
                .state(StateMapper.INSTANCE.toDto(state))
                .countyCode("COUNTY12093")
                .countyGeoCode("GEO123")
                .countyPhoneCode(234)
                .build();

        // Act
        CountyDto newCountyDto = countyService.createCounty(countyDto);
        newCountyDto.setCountyName("Updated County");
        newCountyDto.setLocale("fr_FR");
        newCountyDto.setLocalePreferred(false);
        newCountyDto.setCountyCode("COUNTY456");
        newCountyDto.setCountyGeoCode("GEO456");
        newCountyDto.setCountyPhoneCode(567);
        newCountyDto.setLastModifiedBy(1L);
        CountyDto actualCountyDto = countyService.updateCounty(newCountyDto.getCountyId(), newCountyDto);

        // Assert
        assertNotNull(actualCountyDto);
        assertEquals("Updated County", actualCountyDto.getCountyName());
        assertEquals("fr_FR", actualCountyDto.getLocale());
        assertFalse(actualCountyDto.isLocalePreferred());
        assertEquals("COUNTY456", actualCountyDto.getCountyCode());
        assertEquals("GEO456", actualCountyDto.getCountyGeoCode());
        assertEquals(567, actualCountyDto.getCountyPhoneCode());
        assertNotNull(actualCountyDto.getLastModifiedAt());
        assertNotNull(actualCountyDto.getLastModifiedBy());
    }

    @DisplayName("Test Update Non Existing County")
    @Test
    public void testUpdateNonExistingCounty() {
        // Arrange
        CountyDto countyDto = CountyDto.builder()
                .countyId(1)
                .countyName("Test County")
                .locale("en_US")
                .localePreferred(true)
                .countyCode("COUNTY76123")
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> countyService.updateCounty(countyDto.getCountyId(), countyDto));
    }

    @DisplayName("Test Get County")
    @Test
    public void testGetCounty() {
        // Arrange
        CountyDto countyDto = CountyDto.builder()
                .countyName("Test County")
                .locale("en_US")
                .localePreferred(true)
                .countyCode("COUNTY56123")
                .state(StateMapper.INSTANCE.toDto(state))
                .countyGeoCode("GEO123")
                .countyPhoneCode(234)
                .build();

        // Act
        CountyDto newCountyDto = countyService.createCounty(countyDto);
        CountyDto actualCountyDto = countyService.getCounty(newCountyDto.getCountyId());

        // Assert
        assertNotNull(actualCountyDto);
        assertEquals(newCountyDto.getCountyName(), actualCountyDto.getCountyName());
        assertEquals(newCountyDto.getLocale(), actualCountyDto.getLocale());
        assertEquals(newCountyDto.isLocalePreferred(), actualCountyDto.isLocalePreferred());
        assertEquals(newCountyDto.getCountyCode(), actualCountyDto.getCountyCode());
        assertEquals(newCountyDto.getCountyGeoCode(), actualCountyDto.getCountyGeoCode());
        assertEquals(newCountyDto.getCountyPhoneCode(), actualCountyDto.getCountyPhoneCode());
    }

    @DisplayName("Test Get All Counties")
    @Test
    public void testGetAllCounties() {
        // Arrange
        CountyDto.CountyDtoBuilder countyDtoBuilder = CountyDto.builder()
                .countyName("Test County")
                .locale("en_US")
                .localePreferred(true)
                .state(StateMapper.INSTANCE.toDto(state))
                .countyCode("COUNTY1239010")
                .countyGeoCode("GEO123")
                .countyPhoneCode(234);
        CountyDto countyDto = countyDtoBuilder.build();


        // Act
        countyService.createCounty(countyDto);
        countyDto.setCountyName("Test County 2");
        countyService.createCounty(countyDtoBuilder.countyName("Test County 5").countyCode("COUNTY1239011").build());
        List<CountyDto> actualCountyDtos = countyService.getAllCounties();

        // Assert
        assertNotNull(actualCountyDtos);
        assertEquals(2, actualCountyDtos.size());
    }

    @DisplayName("Test Delete County")
    @Test
    public void testDeleteCounty() {
        // Arrange
        CountyDto countyDto = CountyDto.builder()
                .countyName("Test County")
                .locale("en_US")
                .localePreferred(true)
                .state(StateMapper.INSTANCE.toDto(state))
                .countyCode("COUNTY125643")
                .countyGeoCode("GEO123")
                .countyPhoneCode(234)
                .build();

        // Act
        CountyDto newCountyDto = countyService.createCounty(countyDto);
        RecordVoidRequest request = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        countyService.deleteCounty(newCountyDto.getCountyId(), request);

        // Assert
        CountyDto actualCountyDto = countyService.getCounty(newCountyDto.getCountyId());
        assertTrue(actualCountyDto.getVoided());
    }

    @DisplayName("Test Void Non Existing County")
    @Test
    public void testVoidNonExistingCounty() {
        // Arrange
        RecordVoidRequest request = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> countyService.deleteCounty(1, request));
    }
} 